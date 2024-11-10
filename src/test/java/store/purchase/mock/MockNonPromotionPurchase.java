package store.purchase.mock;

import store.inventory.Stock;
import store.purchase.NonPromotionPurchase;

public class MockNonPromotionPurchase extends NonPromotionPurchase {
    private int generalPrice = 10000;

    public MockNonPromotionPurchase() {
        super(new Stock());
    }

    public void setGeneralPrice(int generalPrice) {
        this.generalPrice = generalPrice;
    }

    @Override
    public int getPrice() {
        return this.generalPrice;
    }
}
