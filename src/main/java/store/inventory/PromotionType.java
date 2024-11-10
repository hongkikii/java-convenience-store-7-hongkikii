package store.inventory;

import static store.common.Constants.INVALID_INPUT_ERROR;

import store.dto.PromotionDetails;

public enum PromotionType {
    ONE_PLUS_ONE(1, 1),
    TWO_PLUS_ONE(2, 1),
    NONE(0, 0),
    NOT_NOW(0, 0);

    private final int purchaseCount;
    private final int freeCount;

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
        throw new IllegalArgumentException(INVALID_INPUT_ERROR);
    }

    public int getPromotionUnit() {
        return purchaseCount + freeCount;
    }

    public int getPurchaseCount() {
        return this.purchaseCount;
    }

    public int getFreeCount() {
        return this.freeCount;
    }

    public PromotionDetails getDetails(int purchaseCount) {
        int unit = getPurchaseCount() + getFreeCount();
        int promotionUnit = purchaseCount / unit;
        int remainder = purchaseCount % unit;
        int payCount = promotionUnit * getPurchaseCount() + remainder;
        return new PromotionDetails(payCount, promotionUnit, remainder);
    }
}
