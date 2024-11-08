package store;

public class MockInputView extends InputView {

    public String answer = "Y";

    @Override
    public String readPromotionAnswer(String productName, int freeCount) {
        return answer;
    }
}
