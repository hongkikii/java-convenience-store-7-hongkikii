package store;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class Purchase {
    private final Stock stock;
    private final Map<String, Integer> purchaseProducts;

    public Purchase(Stock stock, Map<String, Integer> purchaseProducts) {
        this.stock = stock;
        validate(purchaseProducts);
        this.purchaseProducts = purchaseProducts;
    }

    public Map<String, Integer> getPurchaseProducts() {
        return purchaseProducts;
    }

    private void validate(Map<String, Integer> purchaseProducts) {
        List<Product> products = stock.get();
        for (Entry<String, Integer> entry : purchaseProducts.entrySet()) {
            System.out.println(entry.getKey());
            if (!products.contains(entry.getKey())) {
                throw new IllegalArgumentException("[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.");
            }
        }
    }
}
