package store;

public class PromotionResult {
    private int payCount;
    private int freeCount;
    private int remainder;

    public PromotionResult(int payCount, int freeCount, int remainder) {
        this.payCount = payCount;
        this.freeCount = freeCount;
        this.remainder = remainder;
    }


    public int getPayCount() {
        return payCount;
    }

    public int getFreeCount() {
        return freeCount;
    }

    public int getRemainder() {
        return remainder;
    }

    public void exceptShortage(int requiredGeneralCount, int availablePromotionUnit) {
        this.payCount -= requiredGeneralCount;
        this.freeCount = availablePromotionUnit;
    }
}
