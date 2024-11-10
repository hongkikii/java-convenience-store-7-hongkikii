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

    private Receipt(List<ReceiptProduct> totalReceiptProduct,
                    List<ReceiptProduct> freeReceiptProduct,
                    ReceiptPrice receiptPrice) {
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

    public List<ReceiptProduct> getTotalReceiptProduct() {
        return totalReceiptProduct;
    }

    public List<ReceiptProduct> getFreeReceiptProduct() {
        return freeReceiptProduct;
    }

    public ReceiptPrice getReceiptPrice() {
        return receiptPrice;
    }
}
