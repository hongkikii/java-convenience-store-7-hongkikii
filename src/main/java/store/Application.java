package store;

import java.util.Map;

public class Application {
    public static void main(String[] args) {
        OutputView outputView = new OutputView();
        outputView.showStartPrompt();

        outputView.showStockPrompt();
        Stock stock = new Stock();
        outputView.show(stock);

        Purchase purchase = null;
        while (purchase == null) {
            try {
                outputView.showPurchasePrompt();
                InputView inputView = new InputView();
                String productInfo = inputView.readLine();
                PurchaseParser purchaseParser = new PurchaseParser();
                Map<String, Integer> purchaseProducts = purchaseParser.execute(productInfo);
                purchase = new Purchase(inputView, stock, purchaseProducts);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
                System.out.println();
            }
        }
    }
}
