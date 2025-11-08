# LLM 토큰 사용량 조회 프로그램
포지큐브 백엔드 엔지니어 채용 과제로 제작된 LLM 토큰 사용량 조회 및 관리 시스템입니다.
사용자별 요금제에 따라 LLM API 사용량을 추적하고, Rate Limiting과 토큰 할당량을 관리합니다.
<br>
<br>

## 목차
|번호|섹션|설명|
|---|---|---|
|1|[프로젝트 개요](https://github.com/jamm0316/llm-token-manager?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)|프로젝트 소개 및 개발 목적|
|2|[주요 기능](https://github.com/jamm0316/llm-token-manager?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)|핵심 기능 및 특징|
|3|[기술적 의사결정](https://github.com/jamm0316/llm-token-manager?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)|주요 기술 선택 이유와 트레이드오프|
|4|[시스템 아키텍처](https://github.com/jamm0316/llm-token-manager?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)|전체 시스템 구조 및 기술 스택|
|5|[ERD](https://github.com/jamm0316/llm-token-manager?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)|엔티티 관계 다이어그램|
|6|[API 명세](<br>
)|REST API 엔드포인트 상세|
|7|[프로젝트 구조](https://github.com/jamm0316/llm-token-manager?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)|코드 구조 및 패키지 구성|
|8|[실행 방법](https://github.com/jamm0316/llm-token-manager?tab=readme-ov-file#%ED%94%84%EB%A1%9C%EC%A0%9D%ED%8A%B8-%EA%B0%9C%EC%9A%94)|로컬 환경 실행 가이드|
|9|[테스트](https://github.com/jamm0316/llm-token-manager?tab=readme-ov-file#%ED%85%8C%EC%8A%A4%ED%8A%B8)|테스트 전략 및 커버리지|
<br>

> 각각의 자세한 내용은 Wiki로 이동하는 링크에서 확인 할 수 있습니다.
<br>
<br>

## 프로젝트 개요

### 개발 목적

이 프로젝트는 포지큐브 백엔드 엔지니어 채용 과제로, LLM API 사용량을 효율적으로 관리하고 추적하는 시스템을 구현합니다.

### 개발 목표
```
- ✅ 헥사고날 아키텍처 적용으로 비즈니스 로직과 인프라 계층 분리
- ✅ 동시성 제어 - 낙관적 락을 통한 토큰 차감의 정확성 보장
- ✅ Rate Limiting - Sliding Window 알고리즘 기반 분당 요청 제한
- ✅ 스케줄링 - 매일 자정 자동 토큰 초기화
- ✅ 캐싱 최적화 - Caffeine Cache를 활용한 Rate Limiter 성능 개선
```
**개발 기간**: 2025.11.27 ~ 2025.11.08 (6일)  
**참여 인원**: 개인 프로젝트

<br>
<br>

## 주요 기능
1. 사용자 관리: 계정 생성 및 요금제(LITE/PRO) 선택
2. LLM 질의 처리: 제공된 LlmClient를 통한 외부 API 호출
3. 사용량 조회: gpt-5, gpt-4o-mini 모델별 각각의 토큰 사용량 및 비용 집계
4. 토큰 초기화: 매일 00:00 전체 사용자 토큰 리셋

<br>
<br>

## 트러블 슈팅
| Category         | Topic                                                                         | Detailed Wiki Link |
| ---------------- | ----------------------------------------------------------------------------- | ------------------ |
| **Concurrency**  | 동시성 문제 (로그인 & 회원 조회의 Race Condition) | [메인 문서로 이동](https://github.com/jamm0316/dailyGrowth/wiki/%5BConcurrency%5D-%EB%8F%99%EC%8B%9C%EC%84%B1-%EB%AC%B8%EC%A0%9C-(%EB%A1%9C%EA%B7%B8%EC%9D%B8-&-%ED%9A%8C%EC%9B%90-%EC%A1%B0%ED%9A%8C%EC%9D%98-Race-Condition)) |
| **Security**     | Refresh Token 재발급 보안 강화 아키텍처 설계 | [메인 문서로 이동](https://github.com/jamm0316/dailyGrowth/wiki/%5BSecurity%5D-Refresh-Token-%ED%83%88%EC%B7%A8-%EB%B3%B4%EC%95%88-%EB%8C%80%EC%9D%91(UA---IP-%EC%A7%80%EB%AC%B8-%EB%B0%94%EC%9D%B8%EB%94%A9).md) |
| **Architecture** | OAuth 모듈 리팩토링(헥사고날 아키텍처 적용) | [메인 문서로 이동](https://github.com/jamm0316/dailyGrowth/wiki/%5BArchitecture%5D-OAuth-%EB%AA%A8%EB%93%88-%EB%A6%AC%ED%8C%A9%ED%86%A0%EB%A7%81(%ED%97%A5%EC%82%AC%EA%B3%A0%EB%82%A0-%EC%95%84%ED%82%A4%ED%85%8D%EC%B2%98-%EC%A0%81%EC%9A%A9)) |

<br>
<br>

## API 명세

### 1. 사용자 추가 API

```http
POST /users
Content-Type: application/json

{
  "account": "hysic88",
  "password": "123456",
  "name": "현식",
  "plan": "LITE"
}
```

**응답 (201 Created):**

```json
{
  "isSuccess": true,
  "message": "요청에 성공하였습니다.",
  "code": "BASE-1000",
  "httpStatus": "CREATED",
  "result": {
    "account": "hysic88",
    "name": "현식",
    "plan": "LITE",
    "tokens": {
      "quota": 10000,
      "usedTokens": 0,
      "remainingTokens": 10000
    }
  }
}
```

### 2. 질의 API

```http
POST /query
Content-Type: application/json
X-User-Id: 1

{
  "q": "채용 과제를 아래 내용을 참고해서 구현해줘.",
  "model": "gpt-5"
}
```

**응답 (200 OK):**

```json
{
  "isSuccess": true,
  "message": "요청에 성공하였습니다.",
  "code": "BASE-1000",
  "httpStatus": "OK",
  "result": {
    "userName": "현식",
    "answer": "모델 gpt-5로부터의 응답...",
    "model": "gpt-5",
    "usedToken": 25,
    "remainingToken": 9975
  }
}
```

**제약 사항:**

- 분당 30회 요청 제한 (초과 시 429 TOO_MANY_REQUESTS)
- 질의 내용 800자 제한
- 잔여 토큰 부족 시 400 BAD_REQUEST

### 3. 사용량 조회 API

```http
POST /usage
X-User-Id: 1
```

**응답 (200 OK):**

```json
{
  "isSuccess": true,
  "message": "요청에 성공하였습니다.",
  "code": "BASE-1000",
  "httpStatus": "OK",
  "result": {
    "plan": "PRO",
    "quota": 50000,
    "usedTokens": 30000,
    "remainingTokens": 20000,
    "totalPrice": 4.00,
    "models": [
      {
        "name": "gpt-4o-mini",
        "tokens": 10000,
        "price": 1.50
      },
      {
        "name": "gpt-5",
        "tokens": 20000,
        "price": 5.00
      }
    ]
  }
}
```

<br>
<br>

## 시스템 아키텍처
<img width="2200" height="1000" alt="System Archtecture" src="https://github.com/user-attachments/assets/f09c4016-d87f-4a56-aa96-0de40b210322" />

<br>
<br>

| 카테고리         | 기술              | 버전        | 용도              |
| ------------ | --------------- | --------- | --------------- |
| **Backend**  | Java            | 21        | 메인 언어           |
|              | Spring Boot     | 3.5.5     | 애플리케이션 프레임워크    |
|              | Spring Data JPA | 3.5.5     | ORM             |
| **Database** | H2              | In-Memory | 개발/테스트 DB       |
| **Cache**    | Caffeine        | 3.2.3     | Rate Limiter 캐싱 |
| **Build**    | Gradle          | 8.14.3    | 빌드 도구           |
| **Test**     | JUnit 5         | -         | 단위/통합 테스트       |
|              | Mockito         | -         | Mocking 프레임워크   |

<br>
<br>

## ERD
<img width="2450" height="1924" alt="image" src="https://github.com/user-attachments/assets/de50e6b8-4c3b-41c6-90ff-3c340b5d4f4a" />
**설계 포인트:**

- `USERS.version`: 낙관적 락을 위한 버전 필드
- `USERS.account`: 중복 방지를 위한 Unique 제약
- `QUERY_LOG.user_id`: Cascade 삭제 설정 (사용자 삭제 시 로그도 삭제)

<br>
<br>

## 프로젝트 구조
```
src/main/java/com/posicube/assignment/
│
├── AssignmentApplication.java
├── common
│   ├── baseResponse
│   ├── DataInitializer.java      # Plan 초기 데이터
│   └── exception
│
├── plan                          # 요금제 도메인
│   ├── adapter
│   ├── application
│   ├── domain
│   ├── exception
│   └── port
│
├── querylog                      # 질의 로그 도메인
│   ├── adapter
│   ├── application
│   ├── domain
│   ├── exception
│   └── port
│
└── users                          # 사용자 도메인
    ├── adapter
    ├── application
    ├── domain
    ├── exception
    └── port
```
**패키지 설명:**

- `adapter`: 외부 세계와의 인터페이스 (Web, JPA)
- `application`: 유즈케이스 조율 (Service, Facade)
- `domain`: 비즈니스 핵심 로직 (Entity, Policy)
- `port`: 의존성 역전을 위한 인터페이스

<br>
<br>

## 실행 방법

#### 1. 필수 요구사항

- Java 21
- Gradle 8.x

#### 2. 프로젝트 클론 및 빌드

```bash
git clone [repository-url]
cd assignment
./gradlew build
```

#### 3. 애플리케이션 실행

```bash
./gradlew bootRun
```

#### 4. 테스트 실행

```bash
# 전체 테스트
./gradlew test

# 특정 테스트 클래스만
./gradlew test --tests UserServiceConcurrencyTest
```

<br>
<br>

## 테스트

### 테스트 전략

#### 1. 단위 테스트 (Unit Tests)

- **도메인 로직 검증**: Users, QueryLog, Plan 등 핵심 도메인 모델
- **정책 검증**: TokenCalculator, UsageCalculator
- **격리된 테스트**: Mockito를 활용한 의존성 Mock

#### 2. 통합 테스트 (Integration Tests)

- **Service 계층 테스트**: 실제 Repository 연동
- **동시성 테스트**: ExecutorService를 활용한 Race Condition 재현
- **스케줄러 테스트**: TokenResetScheduler 동작 확인
