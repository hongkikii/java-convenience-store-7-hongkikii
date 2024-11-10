package store.purchase;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import store.inventory.Product;
import store.inventory.Stock;

public class NonPromotionPurchase {
    private final Stock stock;
    private final Map<String, Integer> value;

    public NonPromotionPurchase(Stock stock) {
        this.stock = stock;
        this.value = new HashMap<>();
    }

    public Map<String, Integer> getValue() {
        return Collections.unmodifiableMap(value);
    }

    public int getPrice() {
        int generalPrice = 0;
        for(String productName: value.keySet()) {
            Product generalProduct = stock.getGeneralProduct(productName);
            generalPrice += generalProduct.getPrice() * value.get(productName);
        }
        return generalPrice;
    }

    public void add(String productName, int quantity) {
        value.put(productName, quantity);
    }
}
