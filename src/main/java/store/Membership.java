package store;

public class Membership {
    private final int price;

    public Membership(boolean isMemberShipApplied, Purchase purchase) {
        if(isMemberShipApplied) {
            this.price = calculate(purchase);
            return;
        }
        this.price = 0;
    }

    public int getPrice() {
        return this.price;
    }

    private int calculate(Purchase purchase) {
        int discount = (int) (purchase.getGeneralPrice() * 0.3);
        if (discount > 8000) {
            return 8000;
        }
        return discount;
    }
}
