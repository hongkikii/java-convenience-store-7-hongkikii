package store;

import java.util.Map;
import store.common.AnswerValidator;
import store.inventory.Stock;
import store.purchase.DesiredProduct;
import store.purchase.Membership;
import store.purchase.Purchase;
import store.purchase.PurchaseParser;
import store.view.InputView;
import store.view.OutputView;

public class Application {
    public static void main(String[] args) {
        InputView inputView = new InputView();
        OutputView outputView = new OutputView();
        Stock stock = new Stock();

        while (true) {
            outputView.showStartPrompt();
            outputView.showStockPrompt();
            outputView.show(stock);

            Purchase purchase = null;
            while (purchase == null) {
                try {
                    outputView.showPurchasePrompt();
                    String productInfo = inputView.readLine();
                    PurchaseParser purchaseParser = new PurchaseParser();
                    Map<String, Integer> desiredProducts = purchaseParser.execute(productInfo);
                    DesiredProduct desiredProduct = new DesiredProduct(stock, desiredProducts);
                    purchase = new Purchase(inputView, stock, desiredProduct);
                }
                catch (IllegalArgumentException e) {
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
                }
                catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
            outputView.show(purchase.getInfo(membership));

            while (true) {
                try {
                    outputView.showAdditionalPurchasePrompt();
                    String answer = inputView.readLine();
                    boolean isMorePurchased = AnswerValidator.validate(answer);

                    if (isMorePurchased) {
                        System.out.println();
                        break;
                    }
                    return;
                }
                catch (IllegalArgumentException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }
}
