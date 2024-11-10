package store.purchase;

public class Membership {
    private final int price;

    public Membership(boolean isMemberShipApplied, NonPromotionPurchase nonPromotionPurchase) {
        if(isMemberShipApplied) {
            this.price = calculate(nonPromotionPurchase);
            return;
        }
        this.price = 0;
    }

    public int getPrice() {
        return this.price;
    }

    private int calculate(NonPromotionPurchase nonPromotionPurchase) {
        int discount = (int) (nonPromotionPurchase.getPrice() * 0.3);
        if (discount > 8000) {
            return 8000;
        }
        return discount;
    }
}
