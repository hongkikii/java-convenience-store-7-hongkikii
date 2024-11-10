package store.inventory;

import store.dto.PromotionDetails;

public class Product {
    private final String name;
    private final int price;
    private int quantity;
    private final PromotionType promotionType;
    private final String promotionName;

    public Product(String name, int price, int quantity, PromotionType promotionType, String promotionName) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
        this.promotionType = promotionType;
        this.promotionName = promotionName;
    }

    public String getName() {
        return this.name;
    }

    public int getPrice() {
        return this.price;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public PromotionType getPromotionType() {
        return this.promotionType;
    }

    public PromotionDetails getPromotionDetails(int desiredQuantity) {
        return this.promotionType.getDetails(desiredQuantity);
    }

    public String getPromotionName() {
        return this.promotionName;
    }

    public void deduct(int minusQuantity) {
        this.quantity -= minusQuantity;
    }
}
