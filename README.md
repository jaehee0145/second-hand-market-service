## itemBay Backend Assignment
- 게임 아이템 거래 상품 등록/목록 API 구현

### 실행방법 (H2콘솔 접근 방법 포함)



### 기술 스택
- Java 17
- Spring Boot 3.2.2
- JPA/Hibernate
- H2 DB
- 

### 기능 요구사항 (구현 완료/ 미완료 항목)
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

D. 선택 구현
- [x] 상품 수정 API (테스트 코드 포함)
- [ ] 상품 삭제 API (테스트 코드 포함)
- [x] 목록 조회 캐싱 적용
- [ ] 동일 상품 동시에 수정하는 경우 처리 방안 

### 주요 의사 결정 (아키텍처/ DB 설계 / 예외처리 등)

### AI 도구 사용 및 검색 


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