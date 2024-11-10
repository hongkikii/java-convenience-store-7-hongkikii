package store.purchase;

import java.util.LinkedHashMap;
import java.util.Map;

public class PurchaseParser {
    private static final String INVALID_INPUT = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final String INPUT_PATTERN = "^\\[(?=.*-)[^\\]]*\\](,\\[(?=.*-)[^\\]]*\\])*$";
    private static final String INPUT_NAME_PATTERN = "^[a-zA-Z가-힣]+$";
    private static final String PRODUCT_DELIMITER = ",";
    private static final String INFO_TYPE_DELIMITER = "-";
    private static final int INPUT_MIN_QUANTITY = 1;
    private static final int INPUT_MAX_QUANTITY = 1000;

    public Map<String, Integer> execute(String input) {
        validate(input);
        Map<String, Integer> result = new LinkedHashMap<>();
        String[] productInfos = input.split(PRODUCT_DELIMITER);
        for(String productInfo : productInfos) {
            String[] productNameAndQuantity = removeBrackets(productInfo).split(INFO_TYPE_DELIMITER);
            validateProductNameAndQuantity(productNameAndQuantity);
            result.put(productNameAndQuantity[0], Integer.parseInt(productNameAndQuantity[1]));
        }
        return result;
    }

    private void validate(String input) {
        if(input == null || input.isBlank()) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
        if (!input.matches(INPUT_PATTERN)) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }

    private String removeBrackets(String productInfo) {
        return productInfo.substring(1, productInfo.length() - 1);
    }

    private void validateProductNameAndQuantity(String[] productNameAndQuantity) {
        validateProductName(productNameAndQuantity[0]);
        validateProductQuantity(productNameAndQuantity[1]);
    }

    private void validateProductName(String input) {
        if (!input.matches(INPUT_NAME_PATTERN)) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }

    private void validateProductQuantity(String input) {
        try {
            int quantity = Integer.parseInt(input);
            if(quantity < INPUT_MIN_QUANTITY || quantity > INPUT_MAX_QUANTITY) {
                throw new IllegalArgumentException(INVALID_INPUT);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_INPUT);
        }
    }
}
