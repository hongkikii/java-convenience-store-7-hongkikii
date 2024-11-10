package store;

public class MockInputView extends InputView {

    public String answer = "Y";

    @Override
    public boolean isPositiveToGeneral(String productName, int generalCount) {
        return answer.equals("Y");
    }

    @Override
    public boolean isPositiveToAdd(String productName, int freeCount) {
        return answer.equals("Y");
    }
}
