package store.purchase.mock;

import store.view.InputView;

public class MockInputView extends InputView {

    public String answer = "Y";

    @Override
    public boolean readPositiveToGeneral(String productName, int generalCount) {
        return answer.equals("Y");
    }

    @Override
    public boolean readPositiveToAdd(String productName, int freeCount) {
        return answer.equals("Y");
    }
}
