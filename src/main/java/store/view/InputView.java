package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.common.AnswerValidator;

public class InputView {
    private static final String NON_PROMOTION_PROMPT_FORMAT =
            "현재 %s %d개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N)";
    private static final String ADDITIONAL_PROMOTION_PROMPT_FORMAT =
            "현재 %s은(는) %d개를 무료로 더 받을 수 있습니다. 추가하시겠습니까? (Y/N)";

    public String readLine() {
        return Console.readLine();
    }

    public boolean isPositiveToGeneral(String productName, int generalCount) {
        String message = String.format(NON_PROMOTION_PROMPT_FORMAT, productName, generalCount);
        return isPositiveAnswerTo(message);
    }

    public boolean isPositiveToAdd(String productName, int freeCount) {
        String message = String.format(ADDITIONAL_PROMOTION_PROMPT_FORMAT, productName, freeCount);
        return isPositiveAnswerTo(message);
    }

    private boolean isPositiveAnswerTo(String message) {
        do {
            try {
                System.out.println();
                System.out.println(message);
                String answer = readLine();
                return AnswerValidator.validate(answer);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }
}
