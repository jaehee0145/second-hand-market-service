## itemBay Backend Assignment
- 게임 아이템 거래 상품 등록/목록 API 구현

### 실행 방법
1. 환경 요구 사항
   - Java 17
   - Gradle 8.14

2. 빌드 및 실행  
   - 터미널에서 프로젝트 루트 폴더로 이동한 뒤 아래 명령어를 입력  
        - macOS / Linux: `./gradlew bootRun`
        - Windows (CMD/PowerShell): `.\gradlew.bat bootRun`

3. 접속 및 확인
- API 조회: http://localhost:8080/api/items
- H2 DB 콘솔: http://localhost:8080/h2-console
  - JDBC URL: jdbc:h2:mem:testdb

```bash

# 아이템 등록
curl -X POST "http://localhost:8080/api/items" \
     -H "Content-Type: application/json" \
     -d '{
           "server": "라엘",
           "sellerName": "테스터01",
           "itemType": "ITEM",
           "title": "집행검",
           "price": 50000,
           "quantity": 10
         }'

# 1번 아이템 수정
curl -X PUT "http://localhost:8080/api/items/1" \
     -H "Content-Type: application/json" \
     -d '{
           "server": "데포",
           "sellerName": "수정판매자",
           "itemType": "ITEM",
           "title": "강화된 집행검",
           "price": 99900,
           "quantity": 7
         }'

# 2번 아이템 삭제
curl -X DELETE "http://localhost:8080/api/items/2" \
     -H "accept: application/json"           
```
![실행 결과](https://github.com/user-attachments/assets/06ba4049-2c09-4459-bd6d-6c6732d0d596)

### 기술 스택
- Java 17
- Spring Boot 3.2.2
- Spring Data JPA
- QueryDSL : 문자열 기반 JPQL 한계를 극복하고, 자바 코드로 쿼리를 짜기 때문에 컴파일 시점에 타입 체크 가능
- Spring Boot Validation : `@Valid` 를 이용해 controller 단에서 잘못된 파라미터를 체크
- H2 Database : 인메모리 환경을 통한 빠른 테스트 및 독립적인 개발 환경 구성 
- Caffeine Cache : Spring Boot 3.x에서 권장하는 고성능 로컬 캐시, 단일 서버 환경이라 네트워크 I/O가 없어 응답 속도 최적화에 유리
- Lombok : 반복 코드 제거해서 도메인 로직 집중도 향상 
- MockMvc : 실제 서버 구동 없이 단위, 통합 테스트  

### 기능 요구사항 (구현 완료)
  A. 상품 목록 조회 API (GET /api/items)
- [x] 페이지네이션(page, size)
- [x] 검색 기능 (상품명 키워드 검색) **검색 키워드를 포함하는 상품명이 있으면 조회되도록 구현 - contains() 사용**
- [x] 필터링 1개 이상 (itemType, server, minPrice ~ maxPrice) **서버명은 일치 조건으로 조회되도록 구현**
- [x] 정렬 (createdAt, price)
- [x] 로딩/에러/빈 경과에 대한 응답


B. 상품 등록 API (POST /api/items)
- [x] 요청 필드 검증 (필수 필드 누락, 수량/가격은 양수만 허용, 상품 종류는 enum만 허용)
- [x] 검증 실패 시 400 Bad Request + 상세 에러 메세지
- [x] 등록 성공 시 201 Created + 상품 정보 반환


C. 테스트코드
- [x] 단위테스트 또는 통합
- [x] 상품 등록 API 테스트 (성공/실패 케이스)
- [x] 상품 목록 조회 API 테스트 (검색 / 필터/ 페이징)
- [x] `./gradlew test` or `./mvnw test` 로 실행  

![테스트코드 실행 결과](https://github.com/user-attachments/assets/4bfde03d-1af5-4529-a15a-241daa7aeeb2)

D. 선택 구현
- [x] 상품 수정 API (테스트 코드 포함)
- [x] 상품 삭제 API (테스트 코드 포함)
- [x] 목록 조회 캐싱 적용
- [x] 동일 상품 동시에 수정하는 경우 처리 방안 

### 주요 의사 결정 (아키텍처/ DB 설계 / 예외처리 등)
1. 동시성 제어 전략
   - 구현: 수량 차감 시 비관적 락(Pessimistic Lock) 도입
   - 이유: 아이템 수정 API에 대한 세부 요구 사항이 없어서 모든 필드를 업데이트하는 기능을 추가했다.
   아이템명을 동시에 수정하는 테스트를 작성했는데 아이템명이 동시에 수정되는 상황은 크게 의미가 없어서 아이템 수량만 차감하는 기능을 추가했다.  
   수량 데이터는 음수가 되어야 하지 않아야 한다는 요구 사항이 확실하기에 DB 수준에서 확실한 잠금을 선택했다.
   - 결과: 멀티스레드 테스트 환경에서 비관적 락이 동작해서 아이템 수량에 문제가 생기는 요청에 대해서는 예외 처리가 된다.
   - TODO: 현재는 비관적 락만 구현이 됐지만, 트래픽이 몰리는 경우에는 성능 저하 이슈가 있다. 기능에 따라 낙관적 락이나 redis 도입을 검토해 볼 수 있다.


2. 비즈니스 로직의 위치 (Domain Model Pattern)
   - 구현: Item 엔티티를 수정하고 수량을 감소하는 로직을 엔티티 내부에 구현
   - 이유: 서비스 레이어는 흐름 제어만 담당하고, 엔티티 내부에서 데이터를 처리하여 객체 지향적인 설계를 유지하고 서비스 로직 가독성을 높였다.
   - 결과: `decreaseQuantity` 수량 부족 검증 로직이 여러 서비스에 흩어지지 않고 Item 엔티티 내부에서 처리되어 다른 서비스에서도 재사용이 가능하다. 
   - TODO: 여러 엔티티가 상호작용하는 로직이 많은 경우에는 domain 전용 service 레이어를 추가해서 관리하는 것이 유지보수 측면에서 좋다. 


3. 예외 처리 전략 (Custom Exception & Global Handling)
   - 구현: CustomException, ExceptionHandler 구현해서 예외 처리
   - 이유: 발생 가능성 있는 예외에 대한 선제 작업. 응답 코드, 메세지를 직접 작성해서 문제 발생시 원인 파악이 쉽게 가능하다.
   - 결과: 요구 사항에 맞는 응답 코드, 상황에 맞는 에러 메세지를 리턴하도록 구현했다. 
   - TODO: CustomException을 구현할 때 단순 String 말고 상태 코드, 메세지를 포함한 객체를 만들어 응답 규격을 통합시키면 클라이언트에서 처리하기 편하다. 


4. 패키지 구조 (CQRS를 서비스 레이어에 적용)
   - 구현: 조회 기능은 ItemQueryService, 등록/수정/삭제 기능은 ItemCommandService로 구분
   - 이유: 서비스 클래스가 비대해지는 것을 막고, Query는 성능 최적화, Command는 데이터 정합성 처리에 집중하도록 구분했다.
   - 결과: 기능별로 클래스의 책임이 명확해져서 가독성이 좋고 유지보수하기 좋은 구조이다.
   - TODO: 현재는 서비스 레이어 수준의 분리이지만, 트래픽이 증가하는 경우 Read DB와 Write DB를 물리적으로 분리하는 식으로 발전이 가능하다. 

### AI 도구 사용 내용
1. 동시성 제어 전략 검증
   - `CompletableFuture`를 활용한 멀티스레드 테스트 코드의 가독성올 높이는 방법
   - 비동기 실행 중 발생하는 `CompletionException` 예외 처리 방식

2. Caffeine Cache 선정 근거 강화
   - Spring Boot 3.x에서 해당 라이브러리를 권장하는 근거와 내부 알고리즘의 성능 검토

3. 테스트 코드 데이터 작성 요청
   - 테스트 코드에 사용할 더미 데이터 작성을 요청함

4. 아이템 삭제 테스트 소요 시간
   - 삭제 로직 수행 시 JPA 영속성 컨텍스트 동작 방식, em.flush/clear가 성능에 미치는 영향

-------------------
Item 
- id 상품 고유 ID : Long
- server 서버명 : String
- sellerName 판매자 닉네임 : String (고려사항 : 한글? 글자수 제한)
- itemType 상품 종류 : ENUM(GAME_MONEY, ITEM, ACCOUNT, ETC)
- title 상품명 : String (고려사항 : 글자수 제한)
- price 거래가격 : BigDecimal
- quantity 판매수량 : int 
- createdAt 등록일시 : LocalDateTime
- (추가) updatedAt 수정일시 : LocalDateTime