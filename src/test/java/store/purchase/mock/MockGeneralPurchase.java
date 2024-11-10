package store.purchase.mock;

import store.inventory.ProductProcessor;
import store.inventory.PromotionProcessor;
import store.inventory.Stock;
import store.purchase.GeneralPurchase;

public class MockGeneralPurchase extends GeneralPurchase {
    private int generalPrice = 10000;

    public MockGeneralPurchase() {
        super(new Stock(new ProductProcessor(new PromotionProcessor())));
    }

    public void setGeneralPrice(int generalPrice) {
        this.generalPrice = generalPrice;
    }

    @Override
    public int getTotalPrice() {
        return this.generalPrice;
    }
}
