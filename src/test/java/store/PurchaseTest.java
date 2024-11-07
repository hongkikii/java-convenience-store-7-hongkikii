package store;

import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PurchaseTest {

//        - [ ] 입력한 상품이 없는 상품이 없을 경우 예외가 발생한다.
//            - [ ] 입력한 상품의 재고가 부족한 경우 예외가 발생한다.

    @DisplayName("입력한 상품이 없을 경우 예외가 발생한다.")
    @Test
    void 입력한_상품이_없을_경우_예외가_발생한다() {
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("없는 상품", 1);

        Assertions.assertThatThrownBy(() -> new Purchase(purchaseInfo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }
}
