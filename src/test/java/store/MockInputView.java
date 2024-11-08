package store;

public class MockInputView extends InputView {

    public String answer = "Y";

    @Override
    public String readPromotionAnswer(String productName, int freeCount) {
        return answer;
    }

    @Override
    public String readGeneralAnswer(String productName, int freeCount) {
        return answer;
    }
}
