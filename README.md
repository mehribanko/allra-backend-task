# 올라 마켓 백엔드 API

## 지원자 정보
**이름:** 메리

---

## 프로젝트 개요
올라 마켓의 백엔드 시스템입니다. 상품 조회, 장바구니 관리, 주문 및 결제 기능을 제공합니다.

---

## 기술 스택
- Language: Java 17
- Framework: Spring Boot 3.3.5
- Database: MySQL 8.0
- ORM: Spring Data JPA
- Query: QueryDSL 5.0.0
- Build Tool: Maven
- External API: WebClient (Spring WebFlux)

---

## 주요 기능

### 1. 상품 조회 API
- 다중 필터링 (카테고리, 상품명, 가격 범위, 품절 여부)
- 페이징 처리
- QueryDSL 동적 쿼리

### 2. 장바구니 API
- CRUD 작업 (추가, 수정, 삭제, 조회)
- 재고 검증
- 품절 상태 확인

### 3. 주문 및 결제 API
- 장바구니 기반 주문 생성
- 외부 결제 API 연동
- 재고 관리 (차감/복구)
- 주문 상태 관리
- 결제 이력 관리

---


---

## 실행 방법

**1. Prerequisites**
- Java 17+
- MySQL 8.0+
- Maven 3.6+

**2. Database 설정**
```sql
CREATE DATABASE allra_market CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```
**3. application.properties 설정**
   
```
spring.datasource.url=jdbc:mysql://localhost:3306/allra_market
spring.datasource.username=
spring.datasource.password=
```
**4.  실행**

   ```
mvn clean install
mvn spring-boot:run
```
서버가 http://localhost:8080 에서 실행됩니다.


## API 엔드포인트

### 상품 API

- `GET /api/v1/products` - 상품 검색 (필터링 + 페이징)
- `GET /api/v1/products/{id}` - 상품 상세 조회
- `GET /api/v1/products?categoryId={id}` 
- `GET /api/v1/products?name={name}`
- `GET /api/v1/products?minPrice=100000&maxPrice=500000`
- `GET /api/v1/products?isAvailable=true`

### 장바구니 API

- `GET /api/v1/carts?userId={id}` - 장바구니 조회
- `POST /api/v1/carts?userId={id}` - 상품 추가
- `PUT /api/v1/carts/items/{id}?userId={id}` - 수량 수정
- `DELETE /api/v1/carts/items/{id}?userId={id}` - item 삭제
- `DELETE /api/v1/carts?userId={id}` - 장바구니 비우기

### 주문 API

- `POST /api/v1/orders` - 주문 생성
- `GET /api/v1/orders/{id}` - 주문 상세 조회
- `GET /api/v1/orders?userId={id}` - 

---
## Database 스키마

### 테이블 목록

- `TB01CTGRY` - 카테고리
- `TB02USER` - 사용자
- `TB03PRDCT` - 상품
- `TB04CART` - 장바구니
- `TB05CARTITEM` - 장바구니 아이템
- `TB06ORDER` - 주문
- `TB07ORDERITEM` - 주문 아이템
- `TB08PAYMENT` - 결제

### **사용자, category, 상품 테이블에 추가할 수 있는 mock data용 sql query는 mockData/data.sql 파일에 있습니다.**

---

### 테스트

**시간 제약으로 인해 Order / Payment 기능에 대한 단위 테스트만 작성할 수 있었습니다.**

#### 테스트 범위
- **OrderService** 단위 테스트 (8개)
- **OrderRepository** 테스트 (4개)


#### 테스트 기술 스택
- **JUnit 5** 
- **AssertJ** 
- **Mockito** 
- **Spring Boot Test** 
- **H2** 

---

## API 사용 예시
**Postman으로 API 테스트하기**

### 1-1. 전체 상품 조회 (페이징)

**Request:**
```
GET http://localhost:8080/api/products?page=0&size=10
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "iPhone 15 Pro",
      "description": "Apple의 최신 스마트폰",
      "price": 1500000,
      "stockQuantity": 50,
      "isAvailable": true,
      "categoryName": "전자제품",
      "categoryId": 1
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 10
  },
  "totalPages": 2,
  "totalElements": 14,
  "first": true,
  "last": false
}
```

---

### 1-2. 상품 검색 (카테고리 필터)

**Request:**
```
GET http://localhost:8080/api/products?categoryId=1&page=0&size=5
```

**Response:**
```json
{
  "content": [
    {
      "id": 1,
      "name": "iPhone 15 Pro",
      "categoryName": "전자제품",
      "price": 1500000,
      "isAvailable": true
    },
    {
      "id": 2,
      "name": "MacBook Air M2",
      "categoryName": "전자제품",
      "price": 1690000,
      "isAvailable": true
    }
  ],
  "totalElements": 5
}
```

## Troubleshooting

### 문제 1: QueryDSL Q-class 인식 안됨

**해결:** `target/generated-sources/java`를 Generated Sources Root로 설정

### 문제 2: 빈 장바구니 조회 시 500 에러

**해결:** `findByUserIdWithItems` 대신 `findByUserId` 사용 + 빈 컬렉션 체크

### 문제 3: 비즈니스 로직 에러가 500으로 반환

**해결:** GlobalExceptionHandler로 IllegalStateException을 400으로 변환



