# 스위치원 과제 제출

---

## 환경구성

* java 17, Springboot 3.2.5

## 요구사항 구현여부 및 구현 방법

1. [GET] /api/payment/balance

* __Wallet__ 이라는 명칭의 테이블을 만들어 유저 각각의 지갑이 존재한다는 생각으로 설계를 하였습니다.

2. [POST] /api/payment/estimate

* 미리 결제 데이터를 생성 후 결제상태를 대기상태로 구분하도록 하였습니다.

3. [POST] /api/payment/approval

* 미리 생성된 결제 데이터를 기반으로 잔액에서 모자란 금액은 포인트 충전으로 생각하고 처리하였습니다.

## 테스트 코드 작성

* 테스트코드는 성공케이스, 실패케이스를 구분하여 작성하였습니다.

## Swagger 주소

* http://localhost:12000/swagger-ui/index.html
