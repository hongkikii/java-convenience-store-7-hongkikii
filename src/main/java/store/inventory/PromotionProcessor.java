package store.inventory;

import static store.common.Constants.EXCEPT_LINE_COUNT;
import static store.common.Constants.INVALID_INPUT_ERROR;
import static store.common.Constants.WORD_DELIMITER;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

public class PromotionProcessor {
    private static final String PROMOTIONS_FILE_PATH = "src/main/resources/promotions.md";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String NOT_PRODUCTION = "null";
    private static final int PROMOTION_NAME_INDEX = 0;
    private static final int PROMOTION_BUY_INDEX = 1;
    private static final int PROMOTION_GET_INDEX = 2;
    private static final int PROMOTION_START_DATE_INDEX = 3;
    private static final int PROMOTION_END_DATE_INDEX = 4;

    private final Map<String, PromotionType> promotions = new HashMap<>();
    private final Set<String> promotionProducts = new HashSet<>();

    public PromotionProcessor() {
        load();
    }

    public boolean isCurrentPromotion(String promotionName) {
        return (promotions.containsKey(promotionName)
                && promotions.get(promotionName) != PromotionType.NOT_NOW)
                || promotionName.equals(NOT_PRODUCTION);
    }

    public PromotionType getPromotionType(String promotionName) {
        PromotionType promotionType = promotions.get(promotionName);
        if(promotionName.equals(NOT_PRODUCTION)) {
            promotionType = PromotionType.NONE;
        }
        return promotionType;
    }

    public void validateDuplicated(Product product) {
        if (product.getPromotionType() != PromotionType.NONE
                && !promotionProducts.add(product.getName())) {
            throw new IllegalStateException(INVALID_INPUT_ERROR);
        }
    }

    private void load() {
        try (Stream<String> lines = Files.lines(Paths.get(PROMOTIONS_FILE_PATH))) {
            lines.skip(EXCEPT_LINE_COUNT)
                    .map(this::parse)
                    .forEach(promotion ->
                            promotions.put(promotion.getName(), PromotionType.from(promotion))
                    );
        }
        catch (IOException e) {
            throw new IllegalArgumentException(INVALID_INPUT_ERROR);
        }
    }

    private Promotion parse(String line) {
        String[] promotionInfo = line.split(WORD_DELIMITER);
        return new Promotion(promotionInfo[PROMOTION_NAME_INDEX],
                Integer.parseInt(promotionInfo[PROMOTION_BUY_INDEX]),
                Integer.parseInt(promotionInfo[PROMOTION_GET_INDEX]),
                LocalDate.parse(promotionInfo[PROMOTION_START_DATE_INDEX], FORMATTER),
                LocalDate.parse(promotionInfo[PROMOTION_END_DATE_INDEX], FORMATTER));
    }
}
