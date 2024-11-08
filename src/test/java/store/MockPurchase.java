package store;

import java.util.HashMap;

public class MockPurchase extends Purchase {
    public MockPurchase() {
        super(new InputView(), new Stock(), new HashMap<>());
    }
}
