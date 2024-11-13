package store.purchase.mock;

import store.inventory.product.ProductProcessor;
import store.inventory.promotion.PromotionProcessor;
import store.inventory.Stock;
import store.purchase.item.GeneralPurchaseItem;

public class MockGeneralPurchaseItem extends GeneralPurchaseItem {
    public int generalPrice = 10000;

    public MockGeneralPurchaseItem() {
        super(new Stock(new ProductProcessor(new PromotionProcessor())));
    }

    @Override
    public int getTotalPrice() {
        return this.generalPrice;
    }
}
