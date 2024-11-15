package store.cart;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class CartParserTest {
    private static final String INVALID_FORMAT_ERROR = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";

    @DisplayName("상품명에 특수기호가 입력될 경우 예외가 발생한다.")
    @Test
    void 상품명에_알파벳과_특수기호가_입력될_경우_예외가_발생한다() {
        assertInvalidCart("[*라-10],[오렌지주스-5]");
    }

    @DisplayName("상품명에 공백문자가 입력될 경우 예외가 발생한다.")
    @Test
    void 상품명에_알파벳과_공백문자가_입력될_경우_예외가_발생한다() {
        assertInvalidCart("[콜 라-10],[오렌지주스-5]");
    }

    @DisplayName("상품명에 숫자가 입력될 경우 예외가 발생한다.")
    @Test
    void 상품명에_알파벳과_숫자가_입력될_경우_예외가_발생한다() {
        assertInvalidCart("[1-10],[오렌지주스-5]");
    }

    @DisplayName("수량이 0일 경우 예외가 발생한다.")
    @Test
    void 수량이_영일_경우_예외가_발생한다() {
        assertInvalidCart("[콜라-0],[오렌지주스-5]");
    }

    @DisplayName("수량이 음의 정수일 경우 예외가 발생한다.")
    @Test
    void 수량이_음의_정수일_경우_예외가_발생한다() {
        assertInvalidCart("[콜라--10],[오렌지주스-5]");
    }

    @DisplayName("수량이 1000개를 초과할 경우 예외가 발생한다.")
    @Test
    void 수량이_1000개를_초과할_경우_예외가_발생한다() {
        assertInvalidCart("[콜라-1001],[오렌지주스-5]");
    }

    @DisplayName("null이 입력될 경우 예외가 발생한다.")
    @Test
    void 널이_입력될_경우_예외가_발생한다() {
        assertInvalidCart(null);
    }

    @DisplayName("아무것도 입력되지 않을 경우 예외가 발생한다.")
    @Test
    void 아무것도_입력되지_않을_경우_예외가_발생한다() {
        assertInvalidCart(" ");
    }

    @DisplayName("입력 형식에 맞지 않게 입력될 경우 예외가 발생한다.")
    @Test
    void 입력_형식에_맞지_않게_입력될_경우_예외가_발생한다() {
        assertInvalidCart("콜라-10,오렌지주스-5");
    }

    @DisplayName("상품명이 맞지 않게 입력될 경우 예외가 발생한다.")
    @Test
    void 상품명이_맞지_않게_입력될_경우_예외가_발생한다() {
        assertInvalidCart("[-10],[오렌지주스-5]");
    }

    private void assertInvalidCart(String input) {
        CartParser cartParser = new CartParser();
        Assertions.assertThatThrownBy(() -> cartParser.execute(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_FORMAT_ERROR);
    }
}
