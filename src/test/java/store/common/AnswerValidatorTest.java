package store.common;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class AnswerValidatorTest {

    @DisplayName("답변이 Y나 N 이외의 문자일 경우 예외가 발생한다")
    @Test
    void 추가_증정에_대한_답변이_Y나_N_이외의_문자일_경우_예외가_발생한다() {
        Assertions.assertThatThrownBy(() -> AnswerValidator.validate("y"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        Assertions.assertThatThrownBy(() -> AnswerValidator.validate("n"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        Assertions.assertThatThrownBy(() -> AnswerValidator.validate("네"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        Assertions.assertThatThrownBy(() -> AnswerValidator.validate("아니오"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        Assertions.assertThatThrownBy(() -> AnswerValidator.validate("yes"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        Assertions.assertThatThrownBy(() -> AnswerValidator.validate("no"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        Assertions.assertThatThrownBy(() -> AnswerValidator.validate("YES"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        Assertions.assertThatThrownBy(() -> AnswerValidator.validate("NO"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        Assertions.assertThatThrownBy(() -> AnswerValidator.validate("*"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
    }

    @DisplayName("답변으로 아무것도 입력되지 않을 경우 예외가 발생한다")
    @Test
    void 추가_증정에_대한_답변으로_아무것도_입력되지_않을_경우_예외가_발생한다() {
        Assertions.assertThatThrownBy(() -> AnswerValidator.validate(""))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");

        Assertions.assertThatThrownBy(() -> AnswerValidator.validate(" "))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
    }

    @DisplayName("답변으로 null이 입력될 경우 예외가 발생한다")
    @Test
    void 추가_증정에_대한_답변으로_null이_입력될_경우_예외가_발생한다() {
        Assertions.assertThatThrownBy(() -> AnswerValidator.validate(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
    }
}
