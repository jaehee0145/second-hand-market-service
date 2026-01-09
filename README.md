## itemBay Backend Assignment
- 게임 아이템 거래 상품 등록/목록 API 구현

# 기술 스택
- Java 17
- Spring Boot 3.2.2
- JPA/Hibernate
- H2 DB

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

기능 요구사항
A. 상품 목록 조회 API (GET /api/items)
- [ ] 페이지네이션(page, size)
- [ ] 검색 기능 (상품명 키워드 검색) (comment : 부분 일치?)
- [ ] 필터링 1개 이상 (itemType, server, minPrice ~ maxPrice)
- [ ] 정렬 (createdAt, price)
- [ ] 로딩/에러/빈 경과에 대한 응답

응답 예시
```text
{
"content": [
{
"id": 1,
"server": "라엘08",
"sellerName": "아리",
"itemType": "GAME_MONEY",
"title": "다야 팝니다 필요하신만큼 신청해주세요",
"price": 25470,
"quantity": 3000,
"createdAt": "2025-01-15T10:30:00"
}
],
"page": 0,
"size": 20,
"totalElements": 150,
"totalPages": 8
}
```

B. 상품 등록 API (POST /api/items)
- [ ] 요청 필드 검증 (필수 필드 누락, 수량/가격은 양수만 허용, 상품 종류는 enum만 허용)
- [ ] 검증 실패 시 400 Bad Request + 상세 에러 메세지
- [ ] 등록 성공 시 201 Created + 상품 정보 반환

요청 예시
```text
{
"server": "라엘08",
"sellerName": "아리",
"itemType": "GAME_MONEY",
"title": "1,000,000다이아 일괄 판매합니다.",
"price": 100000,
"quantity": 1000000
}
```

C. 테스트코드 
- [ ] 단위테스트 또는 통합
- [ ] 상품 등록 API 테스트 (성공/실패 케이스)
- [ ] 상품 목록 조회 API 테스트 (검색 / 필터/ 페이징)
- [ ] `./gradlew test` or `./mvnw test` 로 실행

