package store;

import java.util.Map;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        outputView.showStartPrompt();

        outputView.showStockPrompt();
        Stock stock = new Stock();
        outputView.show(stock);

        Purchase purchase = null;
        while (purchase == null) {
            try {
                outputView.showPurchasePrompt();
                String productInfo = inputView.readLine();
                PurchaseParser purchaseParser = new PurchaseParser();
                Map<String, Integer> purchaseProducts = purchaseParser.execute(productInfo);
                purchase = new Purchase(inputView, stock, purchaseProducts);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println();
            }
        }

        Membership membership = null;
        while (membership == null) {
            try {
                outputView.showMembershipPrompt();
                String answer = inputView.readLine();
                boolean isMembershipApplied = AnswerValidator.validate(answer);
                membership = new Membership(isMembershipApplied, purchase);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println();
            }
        }
        outputView.show(purchase.getInfo(membership));
    }
}
