package store;

import java.util.ArrayList;
import java.util.List;

public class MockStock extends Stock {
    private final List<Product> products;

    public MockStock() {
        super();
        Product product1 = new Product("콜라", 1000, 10, PromotionType.NONE, "");
        Product product2 = new Product("콜라", 1000, 10, PromotionType.TWO_PLUS_ONE, "탄산2+1");
        this.products = new ArrayList<>();
        products.add(product1);
        products.add(product2);
    }

    @Override
    public List<Product> get() {
        return this.products;
    }
}
