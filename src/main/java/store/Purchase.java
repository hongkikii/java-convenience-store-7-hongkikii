package store;

import java.util.Map;

public class Purchase {
    private final Map<String, Integer> purchaseProducts;

    public Purchase(Map<String, Integer> purchaseProducts) {
        this.purchaseProducts = purchaseProducts;
    }

    public Map<String, Integer> getPurchaseProducts() {
        return purchaseProducts;
    }
}
