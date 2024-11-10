package store.receipt;

import java.util.List;
import store.purchase.Cart;
import store.purchase.Membership;
import store.purchase.PurchaseProcessor;
import store.purchase.item.FreeGiftItem;

public class Receipt {
    private final List<ReceiptProduct> totalReceiptProduct;
    private final List<ReceiptProduct> freeReceiptProduct;
    private final ReceiptPrice receiptPrice;

    private Receipt(List<ReceiptProduct> totalReceiptProduct, List<ReceiptProduct> freeReceiptProduct, ReceiptPrice receiptPrice) {
        this.totalReceiptProduct = totalReceiptProduct;
        this.freeReceiptProduct = freeReceiptProduct;
        this.receiptPrice = receiptPrice;
    }

    public static Receipt of(PurchaseProcessor purchaseProcessor, Membership membership) {
        Cart cart = purchaseProcessor.getCart();
        FreeGiftItem freeGiftItem = purchaseProcessor.getFreeGiftItem();
        int purchasePrice = cart.getTotalPrice();
        int freePrice = freeGiftItem.getTotalPrice();
        int membershipPrice = membership.getPrice();
        int paymentPrice = purchasePrice - membershipPrice - freePrice;

        ReceiptPrice receiptPrice = new ReceiptPrice(purchasePrice, freePrice, membershipPrice, paymentPrice);
        return new Receipt(cart.getInfo(), freeGiftItem.getInfo(), receiptPrice);
    }

    public void printReceipt() {
        String title = "==============W 편의점================";
        String divider = "====================================";

        System.out.println(title);
        System.out.printf("%-10s %9s %8s\n", "상품명", "수량", "금액");

        int totalQuantity = 0;
        for (ReceiptProduct product : totalReceiptProduct) {
            totalQuantity += product.quantity();
            System.out.printf("%-10s %8d %,13d\n", product.productName(), product.quantity(), product.price());
        }

        System.out.println("=============증     정===============");

        for (ReceiptProduct product : freeReceiptProduct) {
            System.out.printf("%-10s %8d\n", product.productName(), product.quantity());
        }

        System.out.println(divider);

        System.out.printf("%-10s %8d %,13d\n", "총구매액", totalQuantity, receiptPrice.totalPrice());
        System.out.printf("%-10s %21s\n", "행사할인", formatWithNegativeSign(receiptPrice.promotionPrice()));
        System.out.printf("%-10s %21s\n", "멤버십할인", formatWithNegativeSign(receiptPrice.membershipPrice()));
        System.out.printf("%-10s %,21d\n", "내실돈", receiptPrice.paymentPrice());
    }

    private static String formatWithNegativeSign(int price) {
        return String.format("-%,d", Math.abs(price));
    }
}
