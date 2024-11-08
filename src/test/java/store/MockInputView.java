package store;

public class MockInputView extends InputView {

    public String answer = "Y";

    @Override
    public String readLine() {
        return answer;
    }
}
