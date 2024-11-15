package store.cart;

import java.util.LinkedHashMap;
import java.util.Map;

public class CartParser {
    private static final String INVALID_FORMAT_ERROR = "[ERROR] 올바르지 않은 형식으로 입력했습니다. 다시 입력해 주세요.";
    private static final String INPUT_PATTERN = "^\\[(?=.*-)[^\\]]*\\](,\\[(?=.*-)[^\\]]*\\])*$";
    private static final String INPUT_NAME_PATTERN = "^[a-zA-Z가-힣]+$";
    private static final String PRODUCT_DELIMITER = ",";
    private static final String INFO_TYPE_DELIMITER = "-";
    private static final int INPUT_MIN_QUANTITY = 1;
    private static final int INPUT_MAX_QUANTITY = 1000;

    public Map<String, Integer> execute(String input) {
        validate(input);
        Map<String, Integer> desiredProducts = new LinkedHashMap<>();
        String[] productInfos = input.split(PRODUCT_DELIMITER);
        for(String productInfo : productInfos) {
            String[] productNameAndQuantity = removeBrackets(productInfo).split(INFO_TYPE_DELIMITER);
            validate(productNameAndQuantity);
            desiredProducts.put(productNameAndQuantity[0], Integer.parseInt(productNameAndQuantity[1]));
        }
        return desiredProducts;
    }

    private void validate(String input) {
        if(input == null || input.isBlank()) {
            throw new IllegalArgumentException(INVALID_FORMAT_ERROR);
        }
        if (!input.matches(INPUT_PATTERN)) {
            throw new IllegalArgumentException(INVALID_FORMAT_ERROR);
        }
    }

    private String removeBrackets(String productInfo) {
        return productInfo.substring(1, productInfo.length() - 1);
    }

    private void validate(String[] productNameAndQuantity) {
        validateProductName(productNameAndQuantity[0]);
        validateProductQuantity(productNameAndQuantity[1]);
    }

    private void validateProductName(String input) {
        if (!input.matches(INPUT_NAME_PATTERN)) {
            throw new IllegalArgumentException(INVALID_FORMAT_ERROR);
        }
    }

    private void validateProductQuantity(String input) {
        try {
            int quantity = Integer.parseInt(input);
            if(quantity < INPUT_MIN_QUANTITY || quantity > INPUT_MAX_QUANTITY) {
                throw new IllegalArgumentException(INVALID_FORMAT_ERROR);
            }
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(INVALID_FORMAT_ERROR);
        }
    }
}
