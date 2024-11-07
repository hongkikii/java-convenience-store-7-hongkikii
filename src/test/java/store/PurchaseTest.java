package store;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PurchaseTest {
    @DisplayName("입력한 상품이 프로모션 상품일 경우 프로모션 재고에서 우선 차감된다.")
    @Test
    void 입력한_상품이_프로모션_상품일_경우_프로모션_재고에서_우선_차감된다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("콜라", 6);

        Purchase purchase = new Purchase(stock, purchaseInfo);
        List<Product> products = stock.get();
        Integer quantityAfterPurchased = products.stream()
                .filter(product -> product.getName().equals("콜라") &&
                        product.getPromotionType().equals(PromotionType.TWO_PLUS_ONE))
                .map(Product::getQuantity)
                .findAny()
                .get();

        assertEquals(quantityAfterPurchased, 4);
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
