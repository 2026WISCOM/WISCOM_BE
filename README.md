# WISCOM_BE

Java 21 / Spring Boot 기반 졸업전시 방명록 API입니다.

## 로컬 실행

MySQL에 `2026wiscom` 데이터베이스를 만든 뒤, Git에서 제외된
`src/main/resources/application.yml`을 아래와 같이 설정합니다.

```yaml
spring:
  application:
    name: backend
  datasource:
    url: jdbc:mysql://localhost:3306/2026wiscom
    username: ${DB_USERNAME:root}
    password: ${DB_PASSWORD:}
    driver-class-name: com.mysql.cj.jdbc.Driver
  jpa:
    hibernate:
      ddl-auto: update
    open-in-view: false
```

`ddl-auto: update`는 로컬 개발용 테이블 생성 설정입니다.

```sh
./gradlew bootRun
```

## 방명록 API

### 저장: `POST /api/guestbooks`

```json
{
  "teamId": "가디언즈",
  "writer": "김철수",
  "content": "졸업 전시 잘 봤어요!"
}
```

성공 시 HTTP 200과 공통 응답 형식으로 저장 결과를 반환합니다.

```json
{
  "isSuccess": true,
  "code": "COMMON200",
  "message": "성공입니다.",
  "result": {
    "id": 1,
    "teamId": "가디언즈",
    "writer": "김철수",
    "content": "졸업 전시 잘 봤어요!",
    "createdAt": "2026-09-29T22:00:00"
  }
}
```

### 조회: `GET /api/guestbooks`

쿼리 파라미터 없이 전체 팀의 방명록을 조회합니다.
`result`에 전체 방명록 배열을 `createdAt DESC` 순서로 반환합니다.
등록된 방명록이 없으면 빈 배열을 반환합니다.

저장 API는 `TeamId`에 등록된 실제 팀명 문자열과 `모두에게`를 허용합니다.
전체에게 남기는 방명록은 `"teamId": "모두에게"`로 요청합니다.
대소문자, 특수문자, 공백을 보정하지 않으며 잘못된 팀명이나 누락된
`teamId`는 HTTP 400을 반환합니다. JSON 숫자, null, 배열 등도 허용하지 않습니다.
Java enum constant 대신 실제 팀명을 요청·응답과 DB 저장에 사용합니다.
별도의 Team 테이블은 만들지 않습니다.

## 구조

FeetFit_Server의 계층 구조를 따릅니다.

- `domain`, `domain/enums`, `domain/common`: 엔티티, 팀 Enum, 생성·수정 시각
- `repository`: 전체 방명록 최신순 JPA 조회
- `service/GuestbookService`: 서비스 인터페이스와 구현
- `web/controller`, `web/dto/guestbook`: API와 요청·응답 DTO
- `converter`: DTO 변환, JPA 값 변환
- `apiPayload`: 공통 응답, 오류 코드, 예외와 전역 예외 처리

## 테스트

```sh
./gradlew test
```

테스트는 로컬 MySQL 대신 H2를 사용합니다. 12개 팀과 `모두에게`의 저장·조회와 DB 값,
잘못된 팀명·타입 거부, null 제약, 전체 팀 조회와 최신순 정렬을 검증합니다.
