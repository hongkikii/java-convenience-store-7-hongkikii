package store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PurchaseTest {

    private InputView positiveAnswerInputView;

    @BeforeEach
    void setUp() {
        positiveAnswerInputView = new MockInputView();
    }

    @DisplayName("입력한 상품이 프로모션 상품일 경우 프로모션 재고에서 우선 차감된다.")
    @Test
    void 입력한_상품이_프로모션_상품일_경우_프로모션_재고에서_우선_차감된다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("콜라", 6);

        Purchase purchase = new Purchase(positiveAnswerInputView, stock, purchaseInfo);
        List<Product> products = stock.get();
        Integer quantityAfterPurchased = products.stream()
                .filter(product -> product.getName().equals("콜라") &&
                        product.getPromotionType().equals(PromotionType.TWO_PLUS_ONE))
                .map(Product::getQuantity)
                .findAny()
                .get();

        assertEquals(quantityAfterPurchased, 4);
    }

    @DisplayName("고객에게 상품이 증정될 때마다, 해당 수량 만큼 프로모션 재고에서 차감한다.")
    @Test
    void 고객에게_상품이_증정될_때마다_해당_수량만큼_프로모션_재고에서_차감한다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("콜라", 6);

        Purchase purchase = new Purchase(positiveAnswerInputView, stock, purchaseInfo);
        Map<String, Integer> payProduct = purchase.getPayProducts();
        Map<String, Integer> freeProduct = purchase.getFreeProducts();

        assertTrue(payProduct.containsKey("콜라"));
        assertEquals(payProduct.get("콜라"), 4);
        assertTrue(freeProduct.containsKey("콜라"));
        assertEquals(freeProduct.get("콜라"), 2);
    }

    @DisplayName("프로모션 적용 가능 상품을 고객이 해당 수량보다 적게 가져온 경우 추가로 증정할 수 있다.")
    @Test
    void 프로모션_적용_가능_상품을_고객이_해당_수량보다_적게_가져온_경우_추가로_증정할_수_있다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("콜라", 5);

        Purchase purchase = new Purchase(positiveAnswerInputView, stock, purchaseInfo);

        Map<String, Integer> payProduct = purchase.getPayProducts();
        Map<String, Integer> freeProduct = purchase.getFreeProducts();

        assertTrue(payProduct.containsKey("콜라"));
        assertEquals(payProduct.get("콜라"), 4);
        assertTrue(freeProduct.containsKey("콜라"));
        assertEquals(freeProduct.get("콜라"), 2);
    }

    @DisplayName("프로모션 적용 가능 상품을 고객이 해당 수량보다 적게 가져온 경우 추가로 증정하지 않을 수 있다.")
    @Test
    void 프로모션_적용_가능_상품을_고객이_해당_수량보다_적게_가져온_경우_추가로_증정하지_않을_수_있다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("콜라", 5);

        MockInputView negativeInputView = new MockInputView();
        negativeInputView.answer = "N";
        Purchase purchase = new Purchase(negativeInputView, stock, purchaseInfo);

        Map<String, Integer> payProduct = purchase.getPayProducts();
        Map<String, Integer> freeProduct = purchase.getFreeProducts();

        assertTrue(payProduct.containsKey("콜라"));
        assertEquals(payProduct.get("콜라"), 4);
        assertTrue(freeProduct.containsKey("콜라"));
        assertEquals(freeProduct.get("콜라"), 1);
    }

    @DisplayName("프로모션 재고가 부족한 경우 사용자가 동의할 시 일반 재고에서 차감한다.")
    @Test
    void 프로모션_재고가_부족한_경우_일반_재고에서_차감한다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("콜라", 11);

        MockInputView negativeInputView = new MockInputView();
        negativeInputView.answer = "Y";
        Purchase purchase = new Purchase(negativeInputView, stock, purchaseInfo);

        Map<String, Integer> payProduct = purchase.getPayProducts();
        Map<String, Integer> freeProduct = purchase.getFreeProducts();

        assertTrue(payProduct.containsKey("콜라"));
        assertEquals(payProduct.get("콜라"), 8);
        assertTrue(freeProduct.containsKey("콜라"));
        assertEquals(freeProduct.get("콜라"), 3);
    }

    @DisplayName("프로모션 재고가 부족한 경우 사용자가 동의하지 않을 시 프로모션 수량만 차감한다.")
    @Test
    void 프로모션_재고가_부족한_경우_사용자가_동의하지_않을시_프로모션_수량만_차감한다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("콜라", 11);

        MockInputView negativeInputView = new MockInputView();
        negativeInputView.answer = "N";
        Purchase purchase = new Purchase(negativeInputView, stock, purchaseInfo);

        Map<String, Integer> payProduct = purchase.getPayProducts();
        Map<String, Integer> freeProduct = purchase.getFreeProducts();

        assertTrue(payProduct.containsKey("콜라"));
        assertEquals(payProduct.get("콜라"), 6);
        assertTrue(freeProduct.containsKey("콜라"));
        assertEquals(freeProduct.get("콜라"), 3);
    }


    @DisplayName("입력한 상품이 없을 경우 예외가 발생한다.")
    @Test
    void 입력한_상품이_없을_경우_예외가_발생한다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("없는 상품", 1);

        Assertions.assertThatThrownBy(() -> new Purchase(positiveAnswerInputView, stock, purchaseInfo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
    }

    @DisplayName("입력한 상품의 재고가 부족한 경우 예외가 발생한다.")
    @Test
    void 입력한_상품의_재고가_부족한_경우_예외가_발생한다() {
        MockStock stock = new MockStock();
        Map<String, Integer> purchaseInfo = new HashMap<>();
        purchaseInfo.put("콜라", 100);

        Assertions.assertThatThrownBy(() -> new Purchase(positiveAnswerInputView, stock, purchaseInfo))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.");
    }
}
