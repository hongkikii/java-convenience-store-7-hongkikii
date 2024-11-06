package store;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class StockTest {
    @DisplayName("일반 재고를 조회할 수 있다.")
    @Test
    public void 일반_재고를_조회할_수_있다() {
        Stock stock = new Stock();
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
        Stock stock = new Stock();
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
        Stock stock = new Stock();
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
