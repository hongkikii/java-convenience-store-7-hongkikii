package store.dto;

public class PurchasePriceInfo {
    private int totalPrice;
    private int promotionPrice;
    private int membershipPrice;
    private int paymentPrice;

    public PurchasePriceInfo(int totalPrice, int promotionPrice, int membershipPrice, int paymentPrice) {
        this.totalPrice = totalPrice;
        this.promotionPrice = promotionPrice;
        this.membershipPrice = membershipPrice;
        this.paymentPrice = paymentPrice;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public int getPromotionPrice() {
        return promotionPrice;
    }

    public int getMembershipPrice() {
        return membershipPrice;
    }

    public int getPaymentPrice() {
        return paymentPrice;
    }
}
