package store.view;

import java.util.List;
import store.inventory.product.Product;
import store.inventory.promotion.PromotionType;
import store.inventory.Stock;
import store.receipt.Receipt;

public class OutputView {
    private static final String START_PROMPT = "안녕하세요. W편의점입니다.";
    private static final String STOCK_PROMPT = "현재 보유하고 있는 상품입니다.";
    private static final String PURCHASE_PROMPT = "구매하실 상품명과 수량을 입력해 주세요. (예: [사이다-2],[감자칩-1])";
    private static final String MEMBERSHIP_PROMPT = "멤버십 할인을 받으시겠습니까? (Y/N)";
    private static final String ADDITIONAL_PURCHASE_PROMPT = "감사합니다. 구매하고 싶은 다른 상품이 있나요? (Y/N)";

    public void showStartPrompt() {
        System.out.println(START_PROMPT);
    }

    public void showStockPrompt() {
        System.out.println(STOCK_PROMPT);
        System.out.println();
    }

    public void show(Stock stock) {
        List<Product> products = stock.get();
        for (Product product : products) {
            StringBuilder productInfo = new StringBuilder("- ");
            productInfo.append(product.getName() + " ");
            productInfo.append(String.format("%,d", product.getPrice()) + "원 ");
            if (product.getQuantity() > 0) {
                productInfo.append(product.getQuantity() + "개 ");
            }
            if (product.getQuantity() <= 0) {
                productInfo.append("재고 없음 ");
            }
            if (product.getPromotionType() != PromotionType.NONE) {
                productInfo.append(product.getPromotionName() + " ");
            }
            System.out.println(productInfo);
        }
        System.out.println();
    }

    public void showPurchasePrompt() {
        System.out.println(PURCHASE_PROMPT);
    }

    public void showMembershipPrompt() {
        System.out.println();
        System.out.println(MEMBERSHIP_PROMPT);
    }

    public void show(Receipt receipt) {
        System.out.println();
        receipt.printReceipt();
    }

    public void showAdditionalPurchasePrompt() {
        System.out.println();
        System.out.println(ADDITIONAL_PURCHASE_PROMPT);
    }
}
