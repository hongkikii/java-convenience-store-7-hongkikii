package store;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PurchaseTest {
    @DisplayName("입력한 상품이 존재할 경우 저장이 완료된다.")
    @Test
    void 입력한_상품명과_재고가_존재할_경우_저장이_완료된다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("콜라", 5);

        assertDoesNotThrow(() -> new Purchase(stock, purchaseInfo));
    }

    @DisplayName("입력한 상품이 없을 경우 예외가 발생한다.")
    @Test
    void 입력한_상품이_없을_경우_예외가_발생한다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("없는 상품", 1);

        Assertions.assertThatThrownBy(() -> new Purchase(stock, purchaseInfo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

    @DisplayName("입력한 상품의 재고가 부족한 경우 예외가 발생한다.")
    @Test
    void 입력한_상품의_재고가_부족한_경우_예외가_발생한다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("콜라", 100);

        Assertions.assertThatThrownBy(() -> new Purchase(stock, purchaseInfo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }
}
