package store;

import java.util.Map;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class PurchaseTest {
    @DisplayName("구매 상품과 수량을 입력받는다.")
    @Test
    void 구매_상품과_수량을_입력받는다() {
        String input = "[콜라-10],[오렌지주스-5]";
        PurchaseParser purchaseParser = new PurchaseParser();
        Map<String, Integer> purchaseInfo = purchaseParser.execute(input);

        Purchase purchase = new Purchase(purchaseInfo);

        Map<String, Integer> purchaseProduct = purchase.getPurchaseProduct();
        Assertions.assertThat(purchaseProduct)
                .containsEntry("콜라", 10)
                .containsEntry("오렌지주스", 5);
    }
}
