package store;

import java.util.HashMap;

public class MockPurchase extends Purchase {
    private int generalPrice = 10000;

    public MockPurchase() {
        super(new InputView(), new Stock(), new HashMap<>());
    }

    public void setGeneralPrice(int generalPrice) {
        this.generalPrice = generalPrice;
    }

    @Override
    public int getGeneralPrice() {
        return this.generalPrice;
    }
}
