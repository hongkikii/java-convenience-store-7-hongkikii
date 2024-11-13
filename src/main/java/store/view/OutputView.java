package store.view;

import java.util.List;
import store.inventory.product.Product;
import store.inventory.promotion.PromotionType;
import store.inventory.Stock;
import store.receipt.Receipt;
import store.receipt.ReceiptPrice;
import store.receipt.ReceiptProduct;

public class OutputView {
    private static final String START_PROMPT = "안녕하세요. W편의점입니다.";
    private static final String STOCK_PROMPT = "현재 보유하고 있는 상품입니다.";
    private static final String PURCHASE_PROMPT = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String MEMBERSHIP_PROMPT = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String ADDITIONAL_PURCHASE_PROMPT = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    private static final String PRODUCT_PREFIX = "- ";
    private static final String PRICE_FORMAT = "%,d원 ";
    private static final String QUANTITY_SUFFIX = "개 ";
    private static final String OUT_OF_STOCK = "재고 없음 ";

    private static final String RECEIPT_TITLE = "==============W 편의점================";
    private static final String RECEIPT_DIVIDER = "====================================";
    private static final String FREE_ITEM_TITLE = "=============증     정===============";

    private static final String RECEIPT_COLUMN_FORMAT = "%-17s %-8s %-6s";
    private static final String RECEIPT_PRODUCT_FORMAT = "%-17s %-7d %-,6d";
    private static final String RECEIPT_FREE_ITEM_FORMAT = "%-17s %-7d";
    private static final String RECEIPT_PROMOTION_PRICE_FORMAT = "%-27s %-6s";
    private static final String RECEIPT_PAYMENT_PRICE_FORMAT = "%-28s %6s";
    private static final String DISCOUNT_PRICE_FORMAT = "%,d";

    private static final String COLUMN_PRODUCT_NAME = "상품명";
    private static final String COLUMN_QUANTITY = "수량";
    private static final String COLUMN_PRICE = "금액";
    private static final String TOTAL_PURCHASE = "총구매액";
    private static final String PROMOTION_DISCOUNT = "행사할인";
    private static final String MEMBERSHIP_DISCOUNT = "멤버십할인";
    private static final String FINAL_PAYMENT = "내실돈";

    private static final String WIDE_SPACE = "\u0020";
    private int totalQuantity = 0;

    public void showLine() {
        System.out.println();
    }

    public void showStartPrompt() {
        System.out.println(START_PROMPT);
    }

    public void showStockPrompt() {
        System.out.println(STOCK_PROMPT);
        showLine();
    }

    public void show(Stock stock) {
        stock.get().forEach(product -> System.out.println(formatProductInfo(product)));
    }

    public void showPurchasePrompt() {
        showLine();
        System.out.println(PURCHASE_PROMPT);
    }

    public void showMembershipPrompt() {
        showLine();
        System.out.println(MEMBERSHIP_PROMPT);
    }

    public void show(Receipt receipt) {
        showLine();
        printReceipt(receipt);
    }

    public void showAdditionalPurchasePrompt() {
        showLine();
        System.out.println(ADDITIONAL_PURCHASE_PROMPT);
    }

    public void show(String errorMessage) {
        System.out.println(errorMessage);
    }

    private String formatProductInfo(Product product) {
        return new StringBuilder(PRODUCT_PREFIX)
                .append(product.getName()).append(" ")
                .append(String.format(PRICE_FORMAT, product.getPrice()))
                .append(formatQuantity(product.getQuantity()))
                .append(formatPromotion(product.getPromotionType(), product.getPromotionName()))
                .toString();
    }

    private String formatQuantity(int quantity) {
        if(quantity > 0) return quantity + QUANTITY_SUFFIX;
        return OUT_OF_STOCK;
    }

    private String formatPromotion(PromotionType promotionType, String promotionName) {
        if (promotionType != PromotionType.NONE) return promotionName + " ";
        return "";
    }

    public void printReceipt(Receipt receipt) {
        printReceiptHeader();
        printProductDetails(receipt.getTotalReceiptProduct());
        printFreeItemDetails(receipt.getFreeReceiptProduct());
        printReceiptFooter(receipt.getReceiptPrice());
    }

    private void printReceiptHeader() {
        System.out.println(RECEIPT_TITLE);
        System.out.println(formatColumnHeader());
    }

    private String formatColumnHeader() {
        return String.format(RECEIPT_COLUMN_FORMAT, COLUMN_PRODUCT_NAME, COLUMN_QUANTITY, COLUMN_PRICE).replace(" ", WIDE_SPACE);
    }

    private void printProductDetails(List<ReceiptProduct> products) {
        totalQuantity = 0;
        products.forEach(product -> {
            totalQuantity += product.quantity();
            System.out.println(formatProductLine(product));
        });
    }

    private String formatProductLine(ReceiptProduct product) {
        String formattedLine = String.format(RECEIPT_PRODUCT_FORMAT, product.productName(), product.quantity(), product.price())
                .replace(" ", WIDE_SPACE);
        return addExtraSpacing(formattedLine, product.quantity());
    }

    private String addExtraSpacing(String line, int quantity) {
        int quantityStartIndex = line.indexOf(String.valueOf(quantity));
        int quantityEndIndex = quantityStartIndex + String.valueOf(quantity).length();
        return line.substring(0, quantityEndIndex + 1) + "  " + line.substring(quantityEndIndex + 1);
    }

    private void printFreeItemDetails(List<ReceiptProduct> freeProducts) {
        System.out.println(FREE_ITEM_TITLE);
        freeProducts.forEach(product -> System.out.println(formatFreeItemLine(product)));
    }

    private String formatFreeItemLine(ReceiptProduct product) {
        return String.format(RECEIPT_FREE_ITEM_FORMAT, product.productName(), product.quantity())
                .replace(" ", WIDE_SPACE);
    }

    private void printReceiptFooter(ReceiptPrice receiptPrice) {
        System.out.println(RECEIPT_DIVIDER);
        System.out.println(formatTotalPurchaseLine(receiptPrice));
        System.out.println(formatDiscountLine(PROMOTION_DISCOUNT, receiptPrice.promotionPrice()));
        System.out.println(formatDiscountLine(MEMBERSHIP_DISCOUNT, receiptPrice.membershipPrice()));
        System.out.println(formatFinalPaymentLine(receiptPrice.paymentPrice()));
    }

    private String formatTotalPurchaseLine(ReceiptPrice receiptPrice) {
        String formattedLine = String.format(
                RECEIPT_PRODUCT_FORMAT, TOTAL_PURCHASE, totalQuantity, receiptPrice.totalPrice())
                .replace(" ", WIDE_SPACE);
        return addExtraSpacing(formattedLine, totalQuantity);
    }

    private String formatDiscountLine(String discountType, int discountPrice) {
        return String.format(RECEIPT_PROMOTION_PRICE_FORMAT, discountType, formatNegativePrice(discountPrice))
                .replace(" ", WIDE_SPACE);
    }

    private String formatFinalPaymentLine(int paymentPrice) {
        return String.format(RECEIPT_PAYMENT_PRICE_FORMAT, FINAL_PAYMENT, formatPrice(paymentPrice))
                .replace(" ", WIDE_SPACE);
    }

    private String formatPrice(int price) {
        return String.format(DISCOUNT_PRICE_FORMAT, Math.abs(price));
    }

    private String formatNegativePrice(int price) {
        return "-" + formatPrice(price);
    }
}
