package store.purchase;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.inventory.product.Product;
import store.inventory.Stock;
import store.purchase.cart.Cart;
import store.purchase.mock.MockInputView;
import store.purchase.mock.MockStock;
import store.view.InputView;

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
        createPurchase("콜라", 6, stock);
        Product promotionProduct = stock.getPromotionProduct("콜라");

        assertEquals(promotionProduct.getQuantity(), 4);
    }

    @DisplayName("입력한 상품이 프로모션 미적용 상품일 경우 일반 재고에서 차감한다.")
    @Test
    void 입력한_상품이_프로모션_미적용_상품일_경우_일반_재고에서_차감한다() {
        MockStock stock = new MockStock();
        createPurchase("오렌지주스", 6, stock);
        Product generalProduct = stock.getGeneralProduct("오렌지주스");

        assertEquals(generalProduct.getQuantity(), 4);
    }

    @DisplayName("고객에게 상품이 증정될 때마다, 해당 수량 만큼 프로모션 재고에서 차감한다.")
    @Test
    void 고객에게_상품이_증정될_때마다_해당_수량만큼_프로모션_재고에서_차감한다() {
        Purchase purchase = createPurchase("콜라", 6);
        Map<String, Integer> payProduct = purchase.getPromotionPurchaseItem().getValue();
        Map<String, Integer> freeProduct = purchase.getFreeGiftItem().getValue();

        assertTrue(payProduct.containsKey("콜라"));
        assertEquals(payProduct.get("콜라"), 4);
        assertTrue(freeProduct.containsKey("콜라"));
        assertEquals(freeProduct.get("콜라"), 2);
    }

    @DisplayName("프로모션 적용 가능 상품을 고객이 해당 수량보다 적게 가져온 경우 추가로 증정할 수 있다.")
    @Test
    void 프로모션_적용_가능_상품을_고객이_해당_수량보다_적게_가져온_경우_추가로_증정할_수_있다() {
        Purchase purchase = createPurchase("콜라", 5);

        Map<String, Integer> payProduct = purchase.getPromotionPurchaseItem().getValue();
        Map<String, Integer> freeProduct = purchase.getFreeGiftItem().getValue();

        assertTrue(payProduct.containsKey("콜라"));
        assertEquals(payProduct.get("콜라"), 4);
        assertTrue(freeProduct.containsKey("콜라"));
        assertEquals(freeProduct.get("콜라"), 2);
    }

    @DisplayName("프로모션 적용 가능 상품을 고객이 해당 수량보다 적게 가져온 경우 추가로 증정하지 않을 수 있다.")
    @Test
    void 프로모션_적용_가능_상품을_고객이_해당_수량보다_적게_가져온_경우_추가로_증정하지_않을_수_있다() {
        Purchase purchase = createPurchase("콜라", 5, "N");

        Map<String, Integer> payProduct = purchase.getPromotionPurchaseItem().getValue();
        Map<String, Integer> freeProduct = purchase.getFreeGiftItem().getValue();

        assertTrue(payProduct.containsKey("콜라"));
        assertEquals(payProduct.get("콜라"), 4);
        assertTrue(freeProduct.containsKey("콜라"));
        assertEquals(freeProduct.get("콜라"), 1);
    }

    @DisplayName("프로모션 재고가 부족한 경우 사용자가 동의할 시 일반 재고에서 차감한다.")
    @Test
    void 프로모션_재고가_부족한_경우_일반_재고에서_차감한다() {
        Purchase purchase = createPurchase("콜라", 11, "Y");

        Map<String, Integer> promotionProduct = purchase.getPromotionPurchaseItem().getValue();
        Map<String, Integer> freeProduct = purchase.getFreeGiftItem().getValue();
        Map<String, Integer> generalProduct = purchase.getNonPromotionPurchaseItem().getValue();

        assertTrue(promotionProduct.containsKey("콜라"));
        assertEquals(promotionProduct.get("콜라"), 6);
        assertTrue(freeProduct.containsKey("콜라"));
        assertEquals(freeProduct.get("콜라"), 3);
        assertTrue(generalProduct.containsKey("콜라"));
        assertEquals(generalProduct.get("콜라"), 2);
    }

    @DisplayName("프로모션 재고가 부족한 경우 사용자가 동의하지 않을 시 프로모션 수량만 차감한다.")
    @Test
    void 프로모션_재고가_부족한_경우_사용자가_동의하지_않을시_프로모션_수량만_차감한다() {
        Purchase purchase = createPurchase("콜라", 11, "N");

        Map<String, Integer> payProduct = purchase.getPromotionPurchaseItem().getValue();
        Map<String, Integer> freeProduct = purchase.getFreeGiftItem().getValue();

        assertTrue(payProduct.containsKey("콜라"));
        assertEquals(payProduct.get("콜라"), 6);
        assertTrue(freeProduct.containsKey("콜라"));
        assertEquals(freeProduct.get("콜라"), 3);
    }

    Purchase createPurchase(String productName, int quantity, String generalPurchaseAnswer) {
        MockStock stock = new MockStock();
        Cart cart = createDesiredProduct(productName, quantity, stock);

        MockInputView inputView = new MockInputView();
        inputView.answer = generalPurchaseAnswer;

        return new Purchase(inputView, stock, cart);
    }

    void createPurchase(String productName, int quantity, Stock stock) {
        Cart cart = createDesiredProduct(productName, quantity, stock);
        new Purchase(positiveAnswerInputView, stock, cart);
    }

    Purchase createPurchase(String productName, int quantity) {
        MockStock stock = new MockStock();
        Cart cart = createDesiredProduct(productName, quantity, stock);
        return new Purchase(positiveAnswerInputView, stock, cart);
    }

    Cart createDesiredProduct(String productName, int quantity, Stock stock) {
        Map<String, Integer> desiredProducts = new HashMap<>();
        desiredProducts.put(productName, quantity);
        return new Cart(stock, desiredProducts);
    }
}
