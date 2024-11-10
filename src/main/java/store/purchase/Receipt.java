package store.purchase;

import java.util.List;
import store.dto.PurchasePriceInfo;
import store.dto.ProductInfo;

public class Receipt {
    private final List<ProductInfo> totalProductInfo;
    private final List<ProductInfo> freeProductInfo;
    private final PurchasePriceInfo purchasePriceInfo;

    public Receipt(List<ProductInfo> totalProductInfo, List<ProductInfo> freeProductInfo, PurchasePriceInfo purchasePriceInfo) {
        this.totalProductInfo = totalProductInfo;
        this.freeProductInfo = freeProductInfo;
        this.purchasePriceInfo = purchasePriceInfo;
    }

    public void printReceipt() {
        String title = "==============W 편의점================";
        String divider = "====================================";

        System.out.println(title);
        System.out.printf("%-10s %9s %8s\n", "상품명", "수량", "금액");

        int totalQuantity = 0;
        for (ProductInfo product : totalProductInfo) {
            totalQuantity += product.getCount();
            System.out.printf("%-10s %8d %,13d\n", product.getProductName(), product.getCount(), product.getPrice());
        }

        System.out.println("=============증     정===============");

        for (ProductInfo product : freeProductInfo) {
            System.out.printf("%-10s %8d\n", product.getProductName(), product.getCount());
        }

        System.out.println(divider);

        System.out.printf("%-10s %8d %,13d\n", "총구매액", totalQuantity, purchasePriceInfo.getTotalPrice());
        System.out.printf("%-10s %21s\n", "행사할인", formatWithNegativeSign(purchasePriceInfo.getPromotionPrice()));
        System.out.printf("%-10s %21s\n", "멤버십할인", formatWithNegativeSign(purchasePriceInfo.getMembershipPrice()));
        System.out.printf("%-10s %,21d\n", "내실돈", purchasePriceInfo.getPaymentPrice());
    }

    private static String formatWithNegativeSign(int price) {
        return String.format("-%,d", Math.abs(price));
    }
}
