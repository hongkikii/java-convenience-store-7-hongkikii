package store.purchase.mock;

import java.util.ArrayList;
import java.util.List;
import store.inventory.product.Product;
import store.inventory.product.ProductProcessor;
import store.inventory.promotion.PromotionProcessor;
import store.inventory.promotion.PromotionType;
import store.inventory.Stock;

public class MockStock extends Stock {
    private static final int MAX_PROMOTION_COUNT = 1;

    private final List<Product> products;

    public MockStock() {
        super(new ProductProcessor(new PromotionProcessor()));
        Product product1 = new Product("콜라", 1000, 10, PromotionType.NONE, "");
        Product product2 = new Product("콜라", 1000, 10, PromotionType.TWO_PLUS_ONE, "탄산2+1");
        Product product3 = new Product("오렌지주스", 1000, 10, PromotionType.NONE, "");
        this.products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
        products.add(product3);
    }

    @Override
    public List<Product> get() {
        return this.products;
    }

    @Override
    public boolean hasPromotion(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .count() > MAX_PROMOTION_COUNT;
    }

    @Override
    public Product getGeneralProduct(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName)
                        && product.getPromotionType().equals(PromotionType.NONE))
                .findAny()
                .get();
    }

    @Override
    public Product getPromotionProduct(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName)
                        && !product.getPromotionType().equals(PromotionType.NONE))
                .findAny()
                .get();
    }
}
