# 편의점 서비스

## ✅ 서비스 소개
편의점에서 상품을 구매할 때 재고 및 프로모션 할인, 멤버십 혜택 등을 고려하여 결제하는 과정을 안내하고 처리하는 시스템

## ✅ 기능 목록

### 재고 안내

- [X] 시작 안내 문구를 보여준다.
    - [X] 안내 문구는 “`안녕하세요. W편의점입니다.`”이다.
- [X] 재고 안내 문구와 재고를 보여준다.
    - [X] 안내 문구는 “`현재 보유하고 있는 상품입니다.`”이다.
    - [X] 재고는 “`- {상품명} {가격}원 {수량}개 {행사이름}`” 형태로 보여준다.
    - [X] 오늘 날짜가 프로모션 기간 내에 포함된 경우 행사 상품을 보여준다.
    - [X] 가격은 천원 단위로 쉼표(,)를 찍어 보여준다.
    - [X] 만약 재고가 없을 시, “`재고 없음`”을 보여준다.
    - [X] 1+1 또는 2+1 프로모션에 맞지 않는 형태일 경우 예외가 발생한다.
    - [X] 동일 상품에 여러 프로모션이 적용될 경우 예외가 발생한다.
    - [X] 재고 예외의 경우 "`[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`" 안내 문구를 출력한다.

### 구매

- [X] 구매 상품 및 수량 입력 안내 문구를 보여준다.
    - [X] 안내 문구는 “`구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])`”이다.
- [X] 구매 상품과 수량을 입력받는다.
    - [X] 상품명, 수량은 하이픈(-)으로, 개별 상품은 대괄호([])로 묶어 쉼표(,)로 구분하는 입력 형식을 가진다.
    - [X] 상품명에 알파벳과 한글 이외의 입력이 들어올 경우 예외가 발생한다.
    - [X] 수량이 0 이하일 경우 예외가 발생한다.
    - [X] 수량이 1000개를 초과할 경우 예외가 발생한다.
    - [X] 입력 형식에 맞지 않게 입력될 경우 예외가 발생한다.
    - [X] 입력 형식에 대한 예외 문구는 "`[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.`"이다.
    - [X] 아무것도 입력되지 않을 경우(””) 예외가 발생한다.
    - [X] null이 입력될 경우 예외가 발생한다.
    - [X] 입력한 상품이 없을 경우 예외가 발생한다.
    - [X] 존재하지 않는 상품에 대한 예외 문구는 "`[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.`"이다.
    - [X] 입력한 상품의 재고가 부족한 경우 예외가 발생한다.
    - [X] 재고가 부족한 경우에 대한 예외 문구는 "`[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.`"이다.
    - [X] 예외 문구 출력 후 사용자로부터 다시 입력을 받는다.

### 일반 결제

- [X] 프로모션 할인이 적용되지 않는 상품을 구매할 때마다, 결제된 수량만큼 해당 상품의 일반 재고에서 차감한다.

### 프로모션 할인

- [X] 오늘 날짜가 프로모션 기간 내에 포함된 경우의 상품에 적용한다.
- [X] 고객이 상품을 구매할 때마다, 결제된 수량만큼 해당 상품을 프로모션 재고에서 우선 차감한다.
- [X] 고객에게 상품이 증정될 때마다, 증정 수량 만큼 프로모션 재고에서 차감한다.
- [X] 프로모션 적용이 가능한 상품에 대해 고객이 해당 수량보다 적게 가져온 경우, 그 수량만큼의 추가 여부 안내 문구를 보여준다.
    - [X] 안내 문구는 “`현재 {상품명}은(는) 1개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)`”이다.
    - [X] 증정 상품 추가 여부를 입력받는다.
        - [X] “Y” 입력 시 해당 수량만큼 증정 상품으로 추가하고, 프로모션 재고에서 차감한다.
        - [X] “N” 입력 시 증정 상품을 추가하지 않는다.
        - [X] Y와 N 이외의 문자 입력 시 예외가 발생한다.
        - [X] 아무것도 입력되지 않을 경우(””) 예외가 발생한다.
        - [X] null이 입력될 경우 예외가 발생한다.
        - [X] 입력 예외가 발생한 경우 "`[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`" 안내 문구를 출력한다.
        - [X] 예외 문구 출력 후 사용자로부터 다시 입력을 받는다.
- [X] 프로모션 재고가 부족한 경우 일반 재고에서 차감한다.
    - [X] 이 경우 일부 수량에 대한 정가 결제 여부 안내 문구를 보여준다.
        - [X] 안내 문구는 “`현재 {상품명} {수량}개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)`”이다.
    - [X] 일부 수량 정가 결제 여부를 입력받는다.
        - [X] “Y” 입력 시 해당 수량만큼 프로모션 없이 결제하고, 일반 재고에서 차감한다.
        - [X] “N” 입력 시 해당 수량만큼의 상품을 결제 내역에서 제외한다.
        - [X] Y와 N 이외의 문자 입력 시 예외가 발생한다.
        - [X] 아무것도 입력되지 않을 경우(””) 예외가 발생한다.
        - [X] null이 입력될 경우 예외가 발생한다.
        - [X] 입력 예외가 발생한 경우 "`[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`" 안내 문구를 출력한다.
        - [X] 예외 문구 출력 후 사용자로부터 다시 입력을 받는다.

### 멤버십 할인

- [X] 멤버십 할인 안내 문구를 보여준다.
    - [X] 안내 문구는 “`멤버십 할인을 받으시겠습니까? (Y/N)`”이다.
- [X] 멤버십 할인 유무를 입력받는다.
    - [X] “Y” 입력 시 멤버십 할인을 적용한다.
    - [X] “N” 입력 시 멤버십 할인을 적용하지 않는다.
    - [X] Y와 N 이외의 문자 입력 시 예외가 발생한다.
    - [X] 아무것도 입력되지 않을 경우(””) 예외가 발생한다.
    - [X] null이 입력될 경우 예외가 발생한다.
    - [X] 입력 예외가 발생한 경우 "`[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`" 안내 문구를 출력한다.
    - [X] 예외 문구 출력 후 사용자로부터 다시 입력을 받는다.
- [X] 멤버십 할인을 받을 경우 할인을 적용한다.
    - [X] 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
    - [X] 멤버십 할인의 최대 한도는 8,000원이다.

### 영수증 출력

- [X] 영수증을 보여준다.
    - [X] 영수증 항목은 아래와 같다.
        - [X] 구매 상품 내역: 구매한 상품명, 수량, 가격
        - [X] 증정 상품 내역: 프로모션에 의해 무료로 제공된 상품명, 수량
        - [X] 금액 정보
            - [X] 총구매액: 구매상품별 가격과 수량을 곱하여 계산
            - [X] 행사할인: 프로모션에 의해 할인된 금액
            - [X] 멤버십할인: 멤버십에 의해 추가로 할인된 금액
            - [X] 내실돈: 총구매액에서 행사 및 멤버십 할인 금액을 제외한 최종 결제 금액
    - [X] 영수증의 구성 요소를 보기 좋게 정렬한다.

### 추가 구매
- [X] 추가 구매 진행 안내 문구를 보여준다.
    - [X] 안내 문구는 “`감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)`”이다.
- [X] 추가 구매 진행 여부를 입력받는다.
  - [X] “Y” 입력 시 시작 안내 문구 출력부터 다시 시작한다.
  - [X] “N” 입력 시 프로그램을 종료한다.
  - [X] Y와 N 이외의 문자 입력 시 예외가 발생한다.
  - [X] 아무것도 입력되지 않을 경우(””) 예외가 발생한다.
  - [X] null이 입력될 경우 예외가 발생한다.
  - [X] 입력 예외가 발생한 경우 "`[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.`" 안내 문구를 출력한다.
  - [X] 예외 문구 출력 후 사용자로부터 다시 입력을 받는다.
