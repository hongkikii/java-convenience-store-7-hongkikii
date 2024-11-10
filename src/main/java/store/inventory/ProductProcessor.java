package store.inventory;

import static store.common.Constants.EXCEPT_LINE_COUNT;
import static store.common.Constants.INVALID_INPUT_ERROR;
import static store.common.Constants.WORD_DELIMITER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

public class ProductProcessor {
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final String NOT_PRODUCTION = "null";
    private static final int PRODUCT_NAME_INDEX = 0;
    private static final int PRODUCT_PRICE_INDEX = 1;
    private static final int PRODUCT_QUANTITY_INDEX = 2;
    private static final int PROMOTION_NAME_INDEX = 3;

    private final PromotionProcessor promotionProcessor;
    private final List<Product> products = new ArrayList<>();

    public ProductProcessor(PromotionProcessor promotionProcessor) {
        this.promotionProcessor = promotionProcessor;
        load();
    }

    public List<Product> getProducts() {
        return Collections.unmodifiableList(products);
    }

    private void load() {
        try (Stream<String> lines = Files.lines(Paths.get(PRODUCTS_FILE_PATH))) {
            List<String[]> productInfos = getProductInfosBy(lines);
            for (int i = 0; i < productInfos.size(); i++) {
                Product product = addProductBy(productInfos.get(i));
                addGeneralProductIfNotExisted(product, productInfos, i);
            }
        }
        catch (IOException e) {
            throw new IllegalArgumentException(INVALID_INPUT_ERROR);
        }
    }

    private Product addProductBy(String[] productInfo) {
        Product product = createProduct(productInfo);
        promotionProcessor.validateDuplicated(product);
        products.add(product);
        return product;
    }

    private void addGeneralProductIfNotExisted(Product product, List<String[]> productInfos, int idx) {
        if(product.getPromotionType() == PromotionType.NONE) return;
        int productCount = productInfos.size();
        boolean isLastLine = idx == productCount - 1;
        boolean hasDifferentNextName = !isLastLine
                && !productInfos.get(idx + 1)[PRODUCT_NAME_INDEX].equals(productInfos.get(idx)[PRODUCT_NAME_INDEX]);

        if (isLastLine || hasDifferentNextName) {
            products.add(new Product(product.getName(), product.getPrice(),
                    0, PromotionType.NONE, NOT_PRODUCTION));
        }
    }

    private List<String[]> getProductInfosBy(Stream<String> lines) {
        return lines.skip(EXCEPT_LINE_COUNT)
                .map(this::parse)
                .filter(this::isCurrentPromotion)
                .toList();
    }

    private String[] parse(String line) {
        return line.split(WORD_DELIMITER);
    }

    private boolean isCurrentPromotion(String[] productInfo) {
        String promotionName = productInfo[PROMOTION_NAME_INDEX];
        return promotionProcessor.isCurrentPromotion(promotionName);
    }

    private Product createProduct(String[] productInfo) {
        String promotionName = productInfo[PROMOTION_NAME_INDEX];
        PromotionType promotionType = promotionProcessor.getPromotionType(promotionName);
        return new Product(productInfo[PRODUCT_NAME_INDEX],
                Integer.parseInt(productInfo[PRODUCT_PRICE_INDEX]),
                Integer.parseInt(productInfo[PRODUCT_QUANTITY_INDEX]),
                promotionType, promotionName);
    }
}
