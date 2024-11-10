package store.purchase.item;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import store.inventory.Stock;

public class PromotionPurchaseItem {
    private final Stock stock;
    private Map<String, Integer> value;

    public PromotionPurchaseItem(Stock stock) {
        this.stock = stock;
        this.value = new HashMap<>();
    }

    public Map<String, Integer> getValue() {
        return Collections.unmodifiableMap(value);
    }

    public void add(String productName, int quantity) {
        value.put(productName, quantity);
    }
}
