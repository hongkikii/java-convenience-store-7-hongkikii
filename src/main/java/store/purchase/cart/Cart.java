package store.purchase.cart;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.receipt.ReceiptProduct;
import store.inventory.product.Product;
import store.inventory.Stock;

public class Cart {
    private static final String PRODUCT_NAME_NOT_EXISTED = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String PRODUCT_QUANTITY_EXCEEDED = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private final Stock stock;
    private final Map<String, Integer> value;

    public Cart(Stock stock, Map<String, Integer> desireProducts) {
        this.stock = stock;
        validate(desireProducts);
        this.value = desireProducts;
    }

    public Set<String> getProductNames() {
        return Collections.unmodifiableSet(value.keySet());
    }

    public int getQuantity(String productName) {
        return value.get(productName);
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (String productName : getProductNames()) {
            Product product = stock.getGeneralProduct(productName);
            int desiredQuantity = getQuantity(productName);
            if(desiredQuantity <= 0) continue;
            int price = product.getPrice() * desiredQuantity;
            totalPrice += price;
        }
        return totalPrice;
    }

    public List<ReceiptProduct> getInfo() {
        List<ReceiptProduct> desiredProducts = new ArrayList<>();
        for (String productName : getProductNames()) {
            Product product = stock.getGeneralProduct(productName);
            int desiredQuantity = getQuantity(productName);
            if(desiredQuantity <= 0) continue;
            int price = product.getPrice() * desiredQuantity;
            desiredProducts.add(new ReceiptProduct(productName, desiredQuantity, price));
        }
        return desiredProducts;
    }

    public void add(String productName, int quantity) {
        value.put(productName, value.getOrDefault(productName, 0) + quantity);
    }

    private void validate(Map<String, Integer> desiredProducts) {
        validateName(desiredProducts.keySet());
        validateQuantity(desiredProducts);
    }

    private void validateName(Set<String> desiredProductNames) {
        for (String productName : desiredProductNames) {
            boolean productExists = stock.isContained(productName);
            if (!productExists) {
                throw new IllegalArgumentException(PRODUCT_NAME_NOT_EXISTED);
            }
        }
    }

    private void validateQuantity(Map<String, Integer> desiredProducts) {
        desiredProducts.forEach((productName, desiredQuantity) -> {
            int availableQuantity = stock.getAvailableQuantity(productName);
            if (availableQuantity < desiredQuantity) {
                throw new IllegalArgumentException(PRODUCT_QUANTITY_EXCEEDED);
            }
        });
    }
}
