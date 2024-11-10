package store.inventory;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import store.inventory.product.Product;
import store.inventory.product.ProductProcessor;
import store.inventory.promotion.PromotionProcessor;
import store.inventory.promotion.PromotionType;

public class StockTest {
    private Stock stock;

    @BeforeEach
    public void setUp() {
        stock = new Stock(new ProductProcessor(new PromotionProcessor()));
    }

    @DisplayName("일반 재고를 조회할 수 있다.")
    @Test
    public void 일반_재고를_조회할_수_있다() {
        List<Product> products = stock.get();

        String name = "콜라";
        int price = 1000;
        int quantity = 10;
        PromotionType promotionType = PromotionType.NONE;

        boolean productExists = products.stream()
                .anyMatch(product -> product.getName().equals(name)
                        && product.getPrice() == price
                        && product.getQuantity() == quantity
                        && product.getPromotionType() == promotionType);
        assertTrue(productExists);
    }

    @DisplayName("프로모션 재고를 조회할 수 있다.")
    @Test
    public void 프로모션_재고를_조회할_수_있다() {
        List<Product> products = stock.get();

        // 2024년 12월 31일까지 적용되는 프로모션
        String name = "콜라";
        int price = 1000;
        int quantity = 10;
        PromotionType promotionType = PromotionType.TWO_PLUS_ONE;

        boolean productExists = products.stream()
                .anyMatch(product -> product.getName().equals(name)
                        && product.getPrice() == price
                        && product.getQuantity() == quantity
                        && product.getPromotionType() == promotionType);
        assertTrue(productExists);
    }

    @DisplayName("현재 프로모션 기간이 아닌 제품의 프로모션 재고를 조회할 수 없다.")
    @Test
    public void 현재_프로모션_기간이_아닌_제품의_프로모션_재고를_조회할_수_없단() {
        List<Product> products = stock.get();

        String name = "닭가슴살";
        int price = 12000;
        int quantity = 50;
        PromotionType promotionType = PromotionType.TWO_PLUS_ONE;

        boolean productExists = products.stream()
                .anyMatch(product -> product.getName().equals(name)
                        && product.getPrice() == price
                        && product.getQuantity() == quantity
                        && product.getPromotionType() == promotionType);
        assertFalse(productExists);
    }
}
