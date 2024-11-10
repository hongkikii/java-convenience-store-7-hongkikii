package store.view;

import camp.nextstep.edu.missionutils.Console;
import store.common.AnswerValidator;

public class InputView {
    public String readLine() {
        return Console.readLine();
    }

    public boolean readPositiveToGeneral(String productName, int generalCount) {
        do {
            try {
                System.out.println();
                System.out.println(
                        "현재 " + productName + " " +  generalCount +
                                "개는 프로모션 할인이 적용되지 않습니다. 그래도 구매하시겠습니까? (Y/N) "
                );
                String answer = readLine();
                return AnswerValidator.validate(answer);
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

    public boolean readPositiveToAdd(String productName, int freeCount) {
        do {
            try {
                System.out.println();
                System.out.println(
                        "현재 " + productName + "은(는) " + freeCount + "개를 무료로 더 받을 수 있습니다. "
                                + "추가하시겠습니까? (Y/N)");
                String answer = readLine();
                return AnswerValidator.validate(answer);
            }
            catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (true);
    }

}
