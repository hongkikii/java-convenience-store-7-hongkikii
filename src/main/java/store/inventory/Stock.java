package store.inventory;

import java.util.Collections;
import java.util.List;

public class Stock {
    private final List<Product> products;

    public Stock(ProductProcessor productProcessor) {
        this.products = productProcessor.getProducts();
    }

    public List<Product> get() {
        return Collections.unmodifiableList(products);
    }

    public boolean isContained(String productName) {
        return products.stream()
                .anyMatch(product -> product.getName().equals(productName));
    }

    public boolean hasPromotion(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .count() >= 2;
    }

    public boolean isPromotionStockNotEnough(String productName, int desiredQuantity) {
        return getPromotionProduct(productName).getQuantity() < desiredQuantity;
    }

    public Product getGeneralProduct(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName)
                        && product.getPromotionType().equals(PromotionType.NONE))
                .findAny()
                .get();
    }

    public Product getPromotionProduct(String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName)
                        && !product.getPromotionType().equals(PromotionType.NONE))
                .findAny()
                .get();
    }
}
