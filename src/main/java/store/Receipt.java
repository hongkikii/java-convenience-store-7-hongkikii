package store;

import java.util.List;

public class Receipt {
    private List<ProductInfo> totalProductInfo;
    private List<ProductInfo> freeProductInfo;
    private PriceInfo priceInfo;

    public Receipt(List<ProductInfo> totalProductInfo, List<ProductInfo> freeProductInfo, PriceInfo priceInfo) {
        this.totalProductInfo = totalProductInfo;
        this.freeProductInfo = freeProductInfo;
        this.priceInfo = priceInfo;
    }

    public List<ProductInfo> getTotalProductInfo() {
        return this.totalProductInfo;
    }

    public List<ProductInfo> getFreeProductInfo() {
        return this.freeProductInfo;
    }

    public PriceInfo getPriceInfo() {
        return this.priceInfo;
    }

    public void printReceipt() {
        String title = "===========W 편의점=============";
        String divider = "==============================";
        System.out.println(title);

        System.out.printf("%-8s %8s %10s\n", "상품명", "수량", "금액");

        int totalQuantity = 0;
        for (ProductInfo product : totalProductInfo) {
            totalQuantity += product.getCount();
            System.out.printf("%-8s %8d %,10d\n", product.getProductName(), product.getCount(), product.getPrice());
        }

        System.out.println("===========증 정=============");

        for (ProductInfo product : freeProductInfo) {
            System.out.printf("%-8s %8d\n", product.getProductName(), product.getCount());
        }

        System.out.println(divider);

        System.out.printf("%-8s %8d %,10d\n", "총구매액", totalQuantity, priceInfo.getTotalPrice());
        System.out.printf("%-8s %18s\n", "행사할인", OutputFormatter.formatWithNegativeSign(priceInfo.getPromotionPrice()));
        System.out.printf("%-8s %18s\n", "멤버십할인", OutputFormatter.formatWithNegativeSign(priceInfo.getMembershipPrice()));
        System.out.printf("%-8s %,18d\n", "내실돈", priceInfo.getPaymentPrice());
    }

    static class ProductInfo {
        private String productName;
        private int count;
        private int price;

        public ProductInfo(String productName, int count, int price) {
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

    static class PriceInfo {
        private int totalPrice;
        private int promotionPrice;
        private int membershipPrice;
        private int paymentPrice;

        public PriceInfo(int totalPrice, int promotionPrice, int membershipPrice, int paymentPrice) {
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
