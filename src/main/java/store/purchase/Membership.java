package store.purchase;

public class Membership {
    private static final double DISCOUNT_PERCENTAGE = 0.3;
    private static final int MAX_DISCOUNT_PRICE = 8000;

    private final int price;

    public Membership(boolean isMembershipApplied, NonPromotionPurchase nonPromotionPurchase) {
        if(isMembershipApplied) {
            this.price = calculate(nonPromotionPurchase);
            return;
        }
        this.price = 0;
    }

    public int getPrice() {
        return this.price;
    }

    private int calculate(NonPromotionPurchase nonPromotionPurchase) {
        int discount = (int) (nonPromotionPurchase.getPrice() * DISCOUNT_PERCENTAGE);
        return Math.min(discount, MAX_DISCOUNT_PRICE);
    }
}
