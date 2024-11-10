package store.purchase;

import java.util.List;
import store.dto.PurchasePriceInfo;
import store.dto.PurchaseProductInfo;

public class Receipt {
    private final List<PurchaseProductInfo> totalPurchaseProductInfo;
    private final List<PurchaseProductInfo> freePurchaseProductInfo;
    private final PurchasePriceInfo purchasePriceInfo;

    private Receipt(List<PurchaseProductInfo> totalPurchaseProductInfo, List<PurchaseProductInfo> freePurchaseProductInfo, PurchasePriceInfo purchasePriceInfo) {
        this.totalPurchaseProductInfo = totalPurchaseProductInfo;
        this.freePurchaseProductInfo = freePurchaseProductInfo;
        this.purchasePriceInfo = purchasePriceInfo;
    }

    public static Receipt of(Purchase purchase, Membership membership) {
        Cart cart = purchase.getCart();
        FreeGiftItem freeGiftItem = purchase.getFreeGiftItem();
        int purchasePrice = cart.getTotalPrice();
        int freePrice = freeGiftItem.getTotalPrice();
        int membershipPrice = membership.getPrice();
        int paymentPrice = purchasePrice - membershipPrice - freePrice;

        PurchasePriceInfo purchasePriceInfo = new PurchasePriceInfo(purchasePrice, freePrice, membershipPrice, paymentPrice);
        return new Receipt(cart.getInfo(), freeGiftItem.getInfo(), purchasePriceInfo);
    }

    public void printReceipt() {
        String title = "==============W 편의점================";
        String divider = "====================================";

        System.out.println(title);
        System.out.printf("%-10s %9s %8s\n", "상품명", "수량", "금액");

        int totalQuantity = 0;
        for (PurchaseProductInfo product : totalPurchaseProductInfo) {
            totalQuantity += product.quantity();
            System.out.printf("%-10s %8d %,13d\n", product.productName(), product.quantity(), product.price());
        }

        System.out.println("=============증     정===============");

        for (PurchaseProductInfo product : freePurchaseProductInfo) {
            System.out.printf("%-10s %8d\n", product.productName(), product.quantity());
        }

        System.out.println(divider);

        System.out.printf("%-10s %8d %,13d\n", "총구매액", totalQuantity, purchasePriceInfo.totalPrice());
        System.out.printf("%-10s %21s\n", "행사할인", formatWithNegativeSign(purchasePriceInfo.promotionPrice()));
        System.out.printf("%-10s %21s\n", "멤버십할인", formatWithNegativeSign(purchasePriceInfo.membershipPrice()));
        System.out.printf("%-10s %,21d\n", "내실돈", purchasePriceInfo.paymentPrice());
    }

    private static String formatWithNegativeSign(int price) {
        return String.format("-%,d", Math.abs(price));
    }
}
