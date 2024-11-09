package store;

public class Membership {
    private final int price;

    public Membership(boolean isMemberShipApplied, Purchase purchase) {
        if(isMemberShipApplied) {
            this.price = (int) (purchase.getGeneralPrice() * 0.3);
            return;
        }
        this.price = 0;
    }

    public int getPrice() {
        return this.price;
    }
}
