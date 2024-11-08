package store;

public class AnswerValidator {
    public static boolean validate(String answer) {
        if (answer == null || answer.isEmpty()) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
        if (answer.equals("Y")) {
            return true;
        }
        if (answer.equals("N")) {
            return false;
        }
        throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
    }
}
