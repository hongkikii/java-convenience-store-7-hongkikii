package store;

import java.util.HashMap;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class DesiredProductTest {
    @DisplayName("입력한 상품이 없을 경우 예외가 발생한다.")
    @Test
    void 입력한_상품이_없을_경우_예외가_발생한다() {
        assertPurchaseThrownBy("없는 상품", 1,
                "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

    @DisplayName("입력한 상품의 재고가 부족한 경우 예외가 발생한다.")
    @Test
    void 입력한_상품의_재고가_부족한_경우_예외가_발생한다() {
        assertPurchaseThrownBy("콜라", 100,
                "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }

    void assertPurchaseThrownBy(String productName, int quantity, String errorMessage) {
        MockStock stock = new MockStock();

        Assertions.assertThatThrownBy(() -> createDesiredProduct(productName, quantity, stock))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(errorMessage);
    }

    void createDesiredProduct(String productName, int quantity, Stock stock) {
        Map<String, Integer> desiredProducts = new HashMap<>();
        desiredProducts.put(productName, quantity);
        new DesiredProduct(stock, desiredProducts);
    }
}
