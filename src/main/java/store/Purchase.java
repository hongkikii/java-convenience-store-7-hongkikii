package store;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Purchase {
    private static final String PRODUCT_NAME_NOT_EXISTED = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String PRODUCT_QUANTITY_EXCEEDED = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private final Stock stock;
    private final Map<String, Integer> purchaseProducts;

    public Purchase(Stock stock, Map<String, Integer> purchaseProducts) {
        this.stock = stock;
        validate(purchaseProducts);
        this.purchaseProducts = purchaseProducts;
    }

    private void validate(Map<String, Integer> purchaseProducts) {
        validateProductName(purchaseProducts);
        validateProductQuantity(purchaseProducts);
    }

    private void validateProductName(Map<String, Integer> purchaseProducts) {
        List<Product> products = stock.get();
        for (Entry<String, Integer> entry : purchaseProducts.entrySet()) {
            System.out.println(entry.getKey());
            for(Product product : products) {
                if(product.getName().equals(entry.getKey())) {
                    return;
                }
            }
        }
        throw new IllegalArgumentException(PRODUCT_NAME_NOT_EXISTED);
    }

    private void validateProductQuantity(Map<String, Integer> purchaseProducts) {
        List<Product> products = stock.get();

        for (Map.Entry<String, Integer> entry : purchaseProducts.entrySet()) {
            String productName = entry.getKey();
            int requiredQuantity = entry.getValue();

            int availableQuantity = products.stream()
                    .filter(product -> product.getName().equals(productName))
                    .mapToInt(Product::getQuantity)
                    .sum();

            if (availableQuantity < requiredQuantity) {
                throw new IllegalArgumentException(PRODUCT_QUANTITY_EXCEEDED);
            }
        }
    }
}
