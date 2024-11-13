package store.purchase;

import store.purchase.item.GeneralPurchaseItem;

public class Membership {
    private static final double DISCOUNT_PERCENTAGE = 0.3;
    private static final int MAX_DISCOUNT_PRICE = 8000;

    private final int price;

    public Membership(boolean isMembershipApplied, GeneralPurchaseItem generalPurchaseItem) {
        if(isMembershipApplied) {
            this.price = calculate(generalPurchaseItem);
            return;
        }
        this.price = 0;
    }

    public int getPrice() {
        return this.price;
    }

    private int calculate(GeneralPurchaseItem generalPurchaseItem) {
        int discount = (int) (generalPurchaseItem.getTotalPrice() * DISCOUNT_PERCENTAGE);
        return Math.min(discount, MAX_DISCOUNT_PRICE);
    }
}
