package store;

import static store.Constants.EXCEPT_LINE_COUNT;
import static store.Constants.INVALID_INPUT_ERROR;
import static store.Constants.WORD_DELIMITER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class ProductLoader {
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final String NOT_PRODUCTION = "null";
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int PRODUCT_PRICE_INDEX = 1;
    private static final int PRODUCT_QUANTITY_INDEX = 2;
    private static final int PROMOTION_NAME_INDEX = 3;

    private final Map<String, PromotionType> currentPromotions;
    private final Set<String> promotionProductNames = new HashSet<>();

    public ProductLoader() {
        PromotionLoader promotionLoader = new PromotionLoader();
        this.currentPromotions = promotionLoader.execute();
    }

    public List<Product> execute() {
        try (Stream<String> lines = Files.lines(Paths.get(PRODUCTS_FILE_PATH))) {
            List<Product> products = new ArrayList<>();
            List<String[]> productLines = lines.skip(EXCEPT_LINE_COUNT)
                    .map(this::parseLine)
                    .filter(this::isCurrentProduct)
                    .toList();

            for (int i = 0; i < productLines.size(); i++) {
                String[] currentLine = productLines.get(i);
                Product product = createProduct(currentLine);

                if (product.getPromotionType() != PromotionType.NONE) {
                    if (!promotionProductNames.add(product.getName())) {
                        throw new IllegalStateException(INVALID_INPUT_ERROR);
                    }
                }

                products.add(product);

                if (product.getPromotionType() != PromotionType.NONE) {
                    boolean isLastLine = i == productLines.size() - 1;
                    boolean hasDifferentNextName = !isLastLine &&
                            !productLines.get(i + 1)[PRODUCT_NAME_INDEX].equals(currentLine[PRODUCT_NAME_INDEX]);

                    if (isLastLine || hasDifferentNextName) {
                        products.add(new Product(
                                product.getName(),
                                product.getPrice(),
                                0,
                                PromotionType.NONE,
                                NOT_PRODUCTION
                        ));
                    }
                }
            }
            return products;
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
