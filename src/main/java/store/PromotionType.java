package store;

public enum PromotionType {
    ONE_PLUS_ONE(1, 1),
    TWO_PLUS_ONE(2, 1),
    NONE(0, 0),
    NOT_NOW(0, 0);

    private final int buy;
    private final int get;

    PromotionType(int buy, int get) {
        this.buy = buy;
        this.get = get;
    }

    public static PromotionType from(int buy, int get) {
        for (PromotionType promotionType : PromotionType.values()) {
            if (promotionType.buy == buy && promotionType.get == get) {
                return promotionType;
            }
        }
        return NONE;
    }
}
