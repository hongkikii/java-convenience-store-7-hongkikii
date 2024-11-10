package store.purchase;

public class Membership {
    private static final double DISCOUNT_PERCENTAGE = 0.3;
    private static final int MAX_DISCOUNT_PRICE = 8000;

    private final int price;

    public Membership(boolean isMembershipApplied, GeneralPurchase generalPurchase) {
        if(isMembershipApplied) {
            this.price = calculate(generalPurchase);
            return;
        }
        this.price = 0;
    }

    public int getPrice() {
        return this.price;
    }

    private int calculate(GeneralPurchase generalPurchase) {
        int discount = (int) (generalPurchase.getTotalPrice() * DISCOUNT_PERCENTAGE);
        return Math.min(discount, MAX_DISCOUNT_PRICE);
    }
}
