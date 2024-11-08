package store;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MembershipTest {
//        - [ ] 멤버십 회원은 프로모션 미적용 금액의 30%를 할인받는다.
//            - [ ] 멤버십 할인의 최대 한도는 8,000원이다.

    @DisplayName("멤버십 회원은 프로모션 미적용 금액의 30%를 할인 받는다.")
    @Test
    void 멤버십_회원은_프로모션_미적용_금액의_삼십퍼센트를_할인_받는다() {
        MockPurchase purchase = new MockPurchase();
        purchase.setGeneralPrice(10000);
        MemberShip memberShip = new MemberShip(purchase);

        Assertions.assertEquals(memberShip.getPrice(), 3000);
    }
}
