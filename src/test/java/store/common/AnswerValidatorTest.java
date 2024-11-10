package store.common;

import static store.common.Constants.INVALID_INPUT_ERROR;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnswerValidatorTest {
    @DisplayName("답변이 Y나 N 이외의 문자일 경우 예외가 발생한다")
    @Test
    void 추가_증정에_대한_답변이_Y나_N_이외의_문자일_경우_예외가_발생한다() {
        assertInvalidAnswer("y");
        assertInvalidAnswer("n");
        assertInvalidAnswer("네");
        assertInvalidAnswer("아니오");
        assertInvalidAnswer("yes");
        assertInvalidAnswer("no");
        assertInvalidAnswer("YES");
        assertInvalidAnswer("NO");
        assertInvalidAnswer("*");
    }

    @DisplayName("답변으로 아무것도 입력되지 않을 경우 예외가 발생한다")
    @Test
    void 추가_증정에_대한_답변으로_아무것도_입력되지_않을_경우_예외가_발생한다() {
        assertInvalidAnswer("");
        assertInvalidAnswer(" ");
        assertInvalidAnswer("\t");
    }

    @DisplayName("답변으로 null이 입력될 경우 예외가 발생한다")
    @Test
    void 추가_증정에_대한_답변으로_null이_입력될_경우_예외가_발생한다() {
        assertInvalidAnswer(null);
    }

    private void assertInvalidAnswer(String input) {
        Assertions.assertThatThrownBy(() -> AnswerValidator.validate(input))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_INPUT_ERROR);
    }
}
