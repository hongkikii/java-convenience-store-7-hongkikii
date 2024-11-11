package store.purchase.item;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class PromotionPurchaseItem {
    private final Map<String, Integer> value;

    public PromotionPurchaseItem() {
        this.value = new HashMap<>();
    }

    public Map<String, Integer> getValue() {
        return Collections.unmodifiableMap(value);
    }

    public void add(String productName, int quantity) {
        value.put(productName, quantity);
    }
}
