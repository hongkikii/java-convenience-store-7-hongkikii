package store;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class Purchase {
    private static final String PRODUCT_NAME_NOT_EXISTED = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String PRODUCT_QUANTITY_EXCEEDED = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private final Stock stock;
    private final Map<String, Integer> purchaseProducts;

    public Purchase(Stock stock, Map<String, Integer> purchaseProducts) {
        this.stock = stock;
        validate(purchaseProducts);
        this.purchaseProducts = purchaseProducts;
        execute();
    }

    private void execute() {
        // 프로모션 상품일 경우 -> 프로모션 재고 먼저 차감
        for(String productName : purchaseProducts.keySet()) {
            List<Product> products = stock.get();
            List<Product> purchaseCandidatesProduct = products.stream()
                    .filter(product -> product.getName().equals(productName))
                    .toList();
            if (purchaseCandidatesProduct.size() == 2) {
                Product promotionProduct = purchaseCandidatesProduct.stream()
                        .filter(product -> !product.getPromotionType().equals(PromotionType.NONE))
                        .findAny()
                        .get();
                promotionProduct.deduct(purchaseProducts.get(productName));
            }
        }
    }

    private void validate(Map<String, Integer> purchaseProducts) {
        validateProductName(purchaseProducts.keySet());
        validateProductQuantity(purchaseProducts);
    }

    private void validateProductName(Set<String> purchaseProductNames) {
        List<Product> products = stock.get();
        for (String purchaseProductName : purchaseProductNames) {
            boolean productExists = products.stream()
                    .anyMatch(product -> product.getName().equals(purchaseProductName));
            if (!productExists) {
                throw new IllegalArgumentException(PRODUCT_NAME_NOT_EXISTED);
            }
        }
    }

    private void validateProductQuantity(Map<String, Integer> purchaseProducts) {
        List<Product> products = stock.get();
        purchaseProducts.forEach((productName, requiredQuantity) -> {
            int availableQuantity = getAvailableQuantity(products, productName);
            if (availableQuantity < requiredQuantity) {
                throw new IllegalArgumentException(PRODUCT_QUANTITY_EXCEEDED);
            }
        });
    }

    private int getAvailableQuantity(List<Product> products, String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .mapToInt(Product::getQuantity)
                .sum();
    }
}
