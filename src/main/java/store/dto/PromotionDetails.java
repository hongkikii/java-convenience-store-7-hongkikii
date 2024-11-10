package store.dto;

public class PromotionDetails {
    private int payQuantity;
    private int freeQuantity;
    private int remainder;

    public PromotionDetails(int payQuantity, int freeQuantity, int remainder) {
        this.payQuantity = payQuantity;
        this.freeQuantity = freeQuantity;
        this.remainder = remainder;
    }

    public int getPayQuantity() {
        return payQuantity;
    }

    public int getFreeQuantity() {
        return freeQuantity;
    }

    public int getRemainder() {
        return remainder;
    }

    public void modify(int shortageQuantity, int availablePromotionUnit) {
        this.payQuantity -= shortageQuantity;
        this.freeQuantity = availablePromotionUnit;
    }
}