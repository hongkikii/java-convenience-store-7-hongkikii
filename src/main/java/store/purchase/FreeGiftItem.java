package store.purchase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.dto.PurchaseProductInfo;
import store.inventory.Product;
import store.inventory.Stock;

public class FreeGiftItem {
    private Stock stock;
    private final Map<String, Integer> value;

    public FreeGiftItem(Stock stock) {
        this.stock = stock;
        this.value = new HashMap<>();
    }

    public Map<String, Integer> getValue() {
        return Collections.unmodifiableMap(value);
    }

    public int getTotalPrice() {
        int totalPrice = 0;
        for (String productName : value.keySet()) {
            Product product = stock.getPromotionProduct(productName);
            int count = value.get(productName);
            if(count <= 0) continue;
            int price = product.getPrice();
            totalPrice += count * price;
        }
        return totalPrice;
    }

    public List<PurchaseProductInfo> getInfo() {
        List<PurchaseProductInfo> freeGiftItems = new ArrayList<>();
        for (String productName : value.keySet()) {
            Product product = stock.getPromotionProduct(productName);
            int count = value.get(productName);
            if(count <= 0) continue;
            int price = product.getPrice();
            freeGiftItems.add(new PurchaseProductInfo(productName, count, price));
        }
        return freeGiftItems;
    }

    public void add(String productName, int quantity) {
        value.put(productName, value.getOrDefault(productName, 0) + quantity);
    }

    public int getQuantity(String productName) {
        return value.get(productName);
    }
}
