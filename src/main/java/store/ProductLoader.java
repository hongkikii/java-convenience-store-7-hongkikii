package store;

import static store.Constants.EXCEPT_LINE_COUNT;
import static store.Constants.INVALID_INPUT_ERROR;
import static store.Constants.WORD_DELIMITER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ProductLoader {
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final String NOT_PRODUCTION = "null";
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int PRODUCT_PRICE_INDEX = 1;
    private static final int PRODUCT_QUANTITY_INDEX = 2;
    private static final int PROMOTION_NAME_INDEX = 3;

    private final Map<String, PromotionType> currentPromotions;

    public ProductLoader() {
        PromotionLoader promotionLoader = new PromotionLoader();
        this.currentPromotions = promotionLoader.execute();
    }

    public List<Product> execute() {
        try (Stream<String> lines = Files.lines(Paths.get(PRODUCTS_FILE_PATH))) {
            return lines.skip(EXCEPT_LINE_COUNT)
                    .map(this::parseLine)
                    .filter(this::isCurrentProduct)
                    .map(this::createProduct)
                    .collect(Collectors.toList());
        } catch (IOException e) {
            throw new IllegalArgumentException(INVALID_INPUT_ERROR);
        }
    }

    private String[] parseLine(String line) {
        return line.split(WORD_DELIMITER);
    }

    private boolean isCurrentProduct(String[] productInfo) {
        String promotionName = productInfo[PROMOTION_NAME_INDEX];
        return (currentPromotions.containsKey(promotionName)
                && currentPromotions.get(promotionName) != PromotionType.NOT_NOW) ||
                promotionName.equals(NOT_PRODUCTION);
    }

    private Product createProduct(String[] productInfo) {
        String promotionName = productInfo[PROMOTION_NAME_INDEX];
        PromotionType promotionType = currentPromotions.get(promotionName);
        if(promotionName.equals(NOT_PRODUCTION)) {
            promotionType = PromotionType.NONE;
        }
        return new Product(productInfo[PRODUCT_NAME_INDEX],
                Integer.parseInt(productInfo[PRODUCT_PRICE_INDEX]),
                Integer.parseInt(productInfo[PRODUCT_QUANTITY_INDEX]),
                promotionType, promotionName);
    }
}
