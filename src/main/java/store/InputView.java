package store;

import camp.nextstep.edu.missionutils.Console;

public class InputView {
    public String readLine() {
        return Console.readLine();
    }

    public String readPromotionAnswer(String productName, int freeCount) {
        System.out.println(
                "현재 " + productName + "은(는) " + freeCount + "개를 무료로 더 받을 수 있습니다. "
                        + "추가하시겠습니까? (Y/N)");
        return readLine();
    }
}
