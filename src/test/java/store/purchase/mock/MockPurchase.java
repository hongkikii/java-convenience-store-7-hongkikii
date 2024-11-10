package store.purchase.mock;

import java.util.HashMap;
import store.inventory.Stock;
import store.purchase.DesiredProduct;
import store.purchase.Purchase;
import store.view.InputView;

public class MockPurchase extends Purchase {
    private int generalPrice = 10000;

    public MockPurchase() {
        super(new InputView(), new Stock(), new DesiredProduct(new Stock(), new HashMap<>()));
    }

    public void setGeneralPrice(int generalPrice) {
        this.generalPrice = generalPrice;
    }

    @Override
    public int getGeneralPrice() {
        return this.generalPrice;
    }
}
