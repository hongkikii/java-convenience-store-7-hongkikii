package store.purchase;

import java.util.HashMap;
import java.util.Map;
import store.inventory.Stock;

public class PromotionPurchase {
    private final Stock stock;
    private Map<String, Integer> value;

    public PromotionPurchase(Stock stock) {
        this.stock = stock;
        this.value = new HashMap<>();
    }

    public Map<String, Integer> getValue() {
        return this.value;
    }

    public void add(String productName, int quantity) {
        value.put(productName, quantity);
    }
}
