package store.purchase;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.purchase.mock.MockGeneralPurchase;

public class MembershipTest {
    @DisplayName("멤버십 회원은 프로모션 미적용 금액의 30%를 할인 받는다.")
    @Test
    void 멤버십_회원은_프로모션_미적용_금액의_삼십퍼센트를_할인_받는다() {
        assertMembershipApplied(10000, 3000, true);
    }

    @DisplayName("멤버십 할인을 선택하지 않을 경우 할인 금액은 0원이다.")
    @Test
    void 멤버십_할인을_선택하지_않을_경우_할인_금액은_영원이다() {
        assertMembershipApplied(10000, 0, false);
    }

    @DisplayName("멤버십 할인의 최대 한도는 8,000원이다.")
    @Test
    void 멤버십_할인의_최대_한도는_팔천원이다() {
        assertMembershipApplied(100000, 8000, true);
    }

    void assertMembershipApplied(int generalPrice, int expectedDiscount, boolean isMembershipApplied) {
        MockGeneralPurchase generalPurchaseProduct = new MockGeneralPurchase();
        generalPurchaseProduct.setGeneralPrice(generalPrice);
        Membership memberShip = new Membership(isMembershipApplied, generalPurchaseProduct);

        Assertions.assertEquals(memberShip.getPrice(), expectedDiscount);
    }
}
