package store;

public enum PromotionType {
    ONE_PLUS_ONE(1, 1) {
        @Override
        public PromotionResult getResult(int purchaseCount) {
            int promotionUnit = purchaseCount / 2;
            int remainder = purchaseCount % 2;
            int payCount = promotionUnit + remainder;
            return new PromotionResult(payCount, promotionUnit, remainder);
        }
    },
    TWO_PLUS_ONE(2, 1){
        @Override
        public PromotionResult getResult(int purchaseCount) {
            int promotionUnit = purchaseCount / 3;
            int remainder = purchaseCount % 3;
            int payCount = promotionUnit * 2 + remainder;
            return new PromotionResult(payCount, promotionUnit, remainder);
        }
    },
    NONE(0, 0),
    NOT_NOW(0, 0);

    private final int purchaseCount;
    private final int freeCount;

    public PromotionResult getResult(int purchaseCount) {
        return null;
    }

    PromotionType(int purchaseCount, int freeCount) {
        this.purchaseCount = purchaseCount;
        this.freeCount = freeCount;
    }

    public static PromotionType from(Promotion promotion) {
        if(promotion.isNotNow()) {
            return NOT_NOW;
        }
        for (PromotionType promotionType : PromotionType.values()) {
            if (promotionType.purchaseCount == promotion.getPurchaseCount()
                    && promotionType.freeCount == promotion.getFreeCount()) {
                return promotionType;
            }
        }
        return NONE;
    }

    public int getPurchaseCount() {
        return this.purchaseCount;
    }

    public int getFreeCount() {
        return this.freeCount;
    }
}
