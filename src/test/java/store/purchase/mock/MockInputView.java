package store.purchase.mock;

import store.view.InputView;

public class MockInputView extends InputView {
    private static final String POSITIVE_ANSWER = "Y";

    public String answer = POSITIVE_ANSWER;

    @Override
    public boolean isPositiveToGeneral(String productName, int generalCount) {
        return answer.equals(POSITIVE_ANSWER);
    }

    @Override
    public boolean isPositiveToAdd(String productName, int freeCount) {
        return answer.equals(POSITIVE_ANSWER);
    }
}
