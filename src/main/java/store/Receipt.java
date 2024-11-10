package store;

import java.util.List;

public class Receipt {
    private final List<PurchaseProductInfo> totalPurchaseProductInfo;
    private final List<PurchaseProductInfo> freePurchaseProductInfo;
    private final PurchasePriceInfo purchasePriceInfo;

    public Receipt(List<PurchaseProductInfo> totalPurchaseProductInfo, List<PurchaseProductInfo> freePurchaseProductInfo, PurchasePriceInfo purchasePriceInfo) {
        this.totalPurchaseProductInfo = totalPurchaseProductInfo;
        this.freePurchaseProductInfo = freePurchaseProductInfo;
        this.purchasePriceInfo = purchasePriceInfo;
    }

    public void printReceipt() {
        String title = "==============W 편의점================";
        String divider = "====================================";

        System.out.println(title);
        System.out.printf("%-10s %9s %8s\n", "상품명", "수량", "금액");

        int totalQuantity = 0;
        for (PurchaseProductInfo product : totalPurchaseProductInfo) {
            totalQuantity += product.getCount();
            System.out.printf("%-10s %8d %,13d\n", product.getProductName(), product.getCount(), product.getPrice());
        }

        System.out.println("=============증     정===============");

        for (PurchaseProductInfo product : freePurchaseProductInfo) {
            System.out.printf("%-10s %8d\n", product.getProductName(), product.getCount());
        }

        System.out.println(divider);

        System.out.printf("%-10s %8d %,13d\n", "총구매액", totalQuantity, purchasePriceInfo.getTotalPrice());
        System.out.printf("%-10s %21s\n", "행사할인", OutputFormatter.formatWithNegativeSign(purchasePriceInfo.getPromotionPrice()));
        System.out.printf("%-10s %21s\n", "멤버십할인", OutputFormatter.formatWithNegativeSign(purchasePriceInfo.getMembershipPrice()));
        System.out.printf("%-10s %,21d\n", "내실돈", purchasePriceInfo.getPaymentPrice());
    }

    static class PurchaseProductInfo {
        private String productName;
        private int count;
        private int price;

        public PurchaseProductInfo(String productName, int count, int price) {
            this.productName = productName;
            this.count = count;
            this.price = price;
        }

        public String getProductName() {
            return productName;
        }

        public int getCount() {
            return count;
        }

        public int getPrice() {
            return price;
        }
    }

    static class PurchasePriceInfo {
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
}
