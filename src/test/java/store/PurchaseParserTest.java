package store;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PurchaseParserTest {
    @DisplayName("구매 상품과 수량을 입력받는다.")
    @Test
    void 구매_상품과_수량을_입력받는다() {
        String input = "[콜라-10],[오렌지주스-5]";

        PurchaseParser purchaseParser = new PurchaseParser();
        Map<String, Integer> purchaseInfo = purchaseParser.execute(input);

        Assertions.assertThat(purchaseInfo)
                .containsEntry("콜라", 10)
                .containsEntry("오렌지주스", 5);
    }

    @DisplayName("상품명에 특수기호가 입력될 경우 예외가 발생한다.")
    @Test
    void 상품명에_알파벳과_특수기호가_입력될_경우_예외가_발생한다() {
        String input = "[*라-10],[오렌지주스-5]";

        PurchaseParser purchaseParser = new PurchaseParser();
        Assertions.assertThatThrownBy(() -> purchaseParser.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    }

    @DisplayName("상품명에 공백문자가 입력될 경우 예외가 발생한다.")
    @Test
    void 상품명에_알파벳과_공백문자가_입력될_경우_예외가_발생한다() {
        String input = "[콜 라-10],[오렌지주스-5]";

        PurchaseParser purchaseParser = new PurchaseParser();
        Assertions.assertThatThrownBy(() -> purchaseParser.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    }

    @DisplayName("상품명에 숫자가 입력될 경우 예외가 발생한다.")
    @Test
    void 상품명에_알파벳과_숫자가_입력될_경우_예외가_발생한다() {
        String input = "[1-10],[오렌지주스-5]";

        PurchaseParser purchaseParser = new PurchaseParser();
        Assertions.assertThatThrownBy(() -> purchaseParser.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    }

    @DisplayName("수량이 0일 경우 예외가 발생한다.")
    @Test
    void 수량이_영일_경우_예외가_발생한다() {
        String input = "[콜라-0],[오렌지주스-5]";

        PurchaseParser purchaseParser = new PurchaseParser();
        Assertions.assertThatThrownBy(() -> purchaseParser.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    }

    @DisplayName("수량이 음의 정수일 경우 예외가 발생한다.")
    @Test
    void 수량이_음의_정수일_경우_예외가_발생한다() {
        String input = "[콜라--10],[오렌지주스-5]";

        PurchaseParser purchaseParser = new PurchaseParser();
        Assertions.assertThatThrownBy(() -> purchaseParser.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    }

    @DisplayName("수량이 1000개를 초과할 경우 예외가 발생한다.")
    @Test
    void 수량이_1000개를_초과할_경우_예외가_발생한다() {
        String input = "[콜라-1001],[오렌지주스-5]";

        PurchaseParser purchaseParser = new PurchaseParser();
        Assertions.assertThatThrownBy(() -> purchaseParser.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    }

    @DisplayName("null이 입력될 경우 예외가 발생한다.")
    @Test
    void 널이_입력될_경우_예외가_발생한다() {
        String input = null;

        PurchaseParser purchaseParser = new PurchaseParser();
        Assertions.assertThatThrownBy(() -> purchaseParser.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    }

    @DisplayName("아무것도 입력되지 않을 경우 예외가 발생한다.")
    @Test
    void 아무것도_입력되지_않을_경우_예외가_발생한다() {
        String input = " ";

        PurchaseParser purchaseParser = new PurchaseParser();
        Assertions.assertThatThrownBy(() -> purchaseParser.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.");
    }

//    @DisplayName("입력 형식에 포함되지 않는 문자가 입력될 경우 예외가 발생한다.")
//    @Test
//    void 입력_형식에_포함되지_않는_문자가_입력될_경우_예외가_발생한다() {
//        String input = ""
//
//        PurchaseParser purchaseParser = new PurchaseParser();
//        purchaseParser.execute()
//    }
}
