package com.posicube.assignment.domain.users;

import com.posicube.assignment.LlmClient;
import com.posicube.assignment.plan.domain.model.Plan;
import com.posicube.assignment.plan.domain.model.PlanType;
import com.posicube.assignment.querylog.application.commandquery.QueryRequest;
import com.posicube.assignment.querylog.application.facade.QueryLogFacade;
import com.posicube.assignment.users.domain.model.Users;
import com.posicube.assignment.users.port.UsersRepository;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UserServiceConcurrencyTest {
    private static final Logger log = LoggerFactory.getLogger(UserServiceConcurrencyTest.class);
    @Autowired
    private QueryLogFacade queryLogFacade;

    @Autowired
    private UsersRepository usersRepository;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private PlatformTransactionManager transactionManager;

    @MockitoBean
    private LlmClient llmClient;

    private Users testUser;

    @BeforeEach
    void setUp() {
        // LLM 클라이언트 Mock 설정
        when(llmClient.query(any(), any())).thenReturn("i".repeat(100));  // 100자(문자길이) * 0.75 = 75토큰

        TransactionStatus transaction = transactionManager.getTransaction(
                new DefaultTransactionDefinition()
        );
        try {
            Users users = Users.create("test1", "pass1234", "evan", Plan.create(PlanType.LITE));
            testUser = usersRepository.save(users);

            entityManager.flush();

            entityManager.createQuery(
                            "UPDATE UsersJpaEntity u SET u.remainingTokens = :tokens WHERE u.id = :id"
                    )
                    .setParameter("tokens", 7500L)
                    .setParameter("id", testUser.getId())
                    .executeUpdate();

            transactionManager.commit(transaction);
        } catch (Exception e) {
            transactionManager.rollback(transaction);
            throw e;
        }

        entityManager.clear();
    }

    @Test
    @DisplayName("낙관 락 적용: 동시 요청 시에도 토큰 차감을 정확하게 처리한다.")
    public void token_deduction_race_condition_test() throws Exception {
        // given
        int concurrentUsers = 100;
        long expectedTokensPerRequest = 75;
        ExecutorService executorService = Executors.newFixedThreadPool(32);
        CountDownLatch latch = new CountDownLatch(concurrentUsers);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failCount = new AtomicInteger(0);

        Users verifyUser = usersRepository.findUserById(testUser.getId())
                .orElseThrow(() -> new RuntimeException("테스트 사용자가 존재하지 않습니다"));
        long initialTokens = verifyUser.getTokens().getRemainingTokens();

//        log.info("테스트 시작 - 사용자 ID: {}, 초기 토큰: {}", verifyUser.getId(), initialTokens);

        QueryRequest queryRequest = new QueryRequest("i".repeat(100), "gpt-5");

        // when: 100개 동시 요청 실행
        for (int i = 0; i < concurrentUsers; i++) {
            final int reqNum = i;
            executorService.submit(() -> {
                try {
                    queryLogFacade.submitQuery(testUser.getId(), queryRequest);
                    successCount.incrementAndGet();
                } catch (Exception e) {
                    failCount.incrementAndGet();
//                    log.error("Thread {} - Request {}: 최종 실패 - {}", Thread.currentThread().getName(), reqNum, e.getMessage());
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();
        boolean terminated = executorService.awaitTermination(10, TimeUnit.SECONDS);
        assertThat(terminated).isTrue();

        // then: 결과 검증
        Thread.sleep(100);
        Users userAfterEvent = usersRepository.findUserById(testUser.getId())
                .orElseThrow(() -> new RuntimeException("테스트 사용자가 존재하지 않습니다"));
        long finalTokens = userAfterEvent.getTokens().getRemainingTokens();
        long expectedFinalToken = initialTokens - (successCount.get() * expectedTokensPerRequest);

//        log.info("========== 테스트 결과 ==========");
//        log.info("총 요청 수: {}", concurrentUsers);
//        log.info("성공한 요청: {}", successCount.get());
//        log.info("실패한 요청: {}", failCount.get());
//        log.info("초기 토큰: {}", initialTokens);
//        log.info("최종 토큰: {}", finalTokens);
//        log.info("기대했던 최종 토큰: {}", expectedFinalToken);
//        log.info("================================");

        // 검증 1: 최종 토큰 수가 예상과 정확히 일치해야 한다.
        assertThat(finalTokens).isEqualTo(expectedFinalToken);

        // 검증 2: 추가 검증 - 성공+실패 = 총 요청
        assertThat(successCount.get() + failCount.get()).isEqualTo(concurrentUsers);

        // 검증 3: Rate Limiter 동작 검증
        assertThat(successCount.get()).isLessThanOrEqualTo(30);
    }
}
