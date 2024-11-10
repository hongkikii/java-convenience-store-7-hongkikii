package store.purchase.mock;

import store.inventory.product.ProductProcessor;
import store.inventory.promotion.PromotionProcessor;
import store.inventory.Stock;
import store.purchase.item.GeneralPurchaseItem;

public class MockGeneralPurchaseItem extends GeneralPurchaseItem {
    private int generalPrice = 10000;

    public MockGeneralPurchaseItem() {
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
