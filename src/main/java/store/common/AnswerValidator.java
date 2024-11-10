package store.common;

import static store.common.Constants.INVALID_INPUT_ERROR;

public class AnswerValidator {
    private static String POSITIVE_ANSWER = "Y";
    private static String NEGATIVE_ANSWER = "N";

    public static boolean validate(String answer) {
        if (answer == null || answer.isEmpty()) {
            throw new IllegalArgumentException(INVALID_INPUT_ERROR);
        }
        if (answer.equals(POSITIVE_ANSWER)) {
            return true;
        }
        if (answer.equals(NEGATIVE_ANSWER)) {
            return false;
        }
        throw new IllegalArgumentException(INVALID_INPUT_ERROR);
    }
}
