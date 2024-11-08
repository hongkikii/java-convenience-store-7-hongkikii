package store;

import java.util.ArrayList;
import java.util.List;

public class MockStock extends Stock {
    private final List<Product> products;

    public MockStock() {
        super();
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
                .count() >= 2;
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
