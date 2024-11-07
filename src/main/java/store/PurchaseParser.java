package store;

import java.util.HashMap;
import java.util.Map;

public class PurchaseParser {
    private static final String INVALID_INPUT = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";

    public Map<String, Integer> execute(String input) {
        if(input == null || input.isBlank()) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
        validate(input);
        Map<String, Integer> result = new HashMap<>();
        String[] productInfos = input.split(",");
        for(String productInfo : productInfos) {
            String[] info = productInfo.substring(1, productInfo.length() - 1).split("-");
            validateProductName(info[0]);
            validateProductQuantity(info[1]);
            result.put(info[0], Integer.parseInt(info[1]));
        }
        return result;
    }

    private void validate(String input) {
        String inputPattern = "^\\[(?=.*-)[^\\]]*\\](,\\[(?=.*-)[^\\]]*\\])*$";
        if (!input.matches(inputPattern)) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }

    private void validateProductName(String input) {
        if (!input.matches("^[a-zA-Z가-힣]+$")) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }

    private void validateProductQuantity(String input) {
        try {
            int quantity = Integer.parseInt(input);
            if(quantity <= 0 || quantity > 1000) {
                throw new IllegalArgumentException(INVALID_INPUT);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }
}
