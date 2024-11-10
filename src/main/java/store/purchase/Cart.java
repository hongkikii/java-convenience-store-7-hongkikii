package store.purchase;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.dto.ProductInfo;
import store.inventory.Product;
import store.inventory.Stock;

public class Cart {
    private static final String PRODUCT_NAME_NOT_EXISTED = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String PRODUCT_QUANTITY_EXCEEDED = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private final Stock stock;
    private final Map<String, Integer> value;

    public Cart(Stock stock, Map<String, Integer> value) {
        this.stock = stock;
        validate(value);
        this.value = value;
    }

    public Set<String> getProductNames() {
        return value.keySet();
    }

    public int getQuantity(String productName) {
        return value.get(productName);
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (String productName : value.keySet()) {
            Product product = stock.getGeneralProduct(productName);
            int count = value.get(productName);
            if(count <= 0) continue;
            int price = product.getPrice() * count;
            totalPrice += price;
        }
        return totalPrice;
    }

    public List<ProductInfo> getInfo() {
        List<ProductInfo> totalProductInfo = new ArrayList<>();
        for (String productName : value.keySet()) {
            Product product = stock.getGeneralProduct(productName);
            int count = value.get(productName);
            if(count <= 0) continue;
            int price = product.getPrice() * count;
            totalProductInfo.add(new ProductInfo(productName, count, price));
        }
        return totalProductInfo;
    }

    public void add(String productName, int quantity) {
        value.put(productName, value.getOrDefault(productName, 0) + quantity);
    }

    private void validate(Map<String, Integer> desiredProducts) {
        validateName(desiredProducts.keySet());
        validateQuantity(desiredProducts);
    }

    private void validateName(Set<String> desiredProductNames) {
        for (String desiredProductName : desiredProductNames) {
            boolean productExists = stock.isContained(desiredProductName);
            if (!productExists) {
                throw new IllegalArgumentException(PRODUCT_NAME_NOT_EXISTED);
            }
        }
    }

    private void validateQuantity(Map<String, Integer> purchaseProducts) {
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
