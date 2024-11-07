package store;

import java.util.Map;

public class Application {
    public static void main(String[] args) {
        OutputView outputView = new OutputView();
        outputView.showStartPrompt();

        outputView.showStockPrompt();
        Stock stock = new Stock();
        outputView.show(stock);

        outputView.showPurchasePrompt();
        InputView inputView = new InputView();
        String productInfo = inputView.readLine();
        PurchaseParser purchaseParser = new PurchaseParser();
        Map<String, Integer> purchaseProducts = purchaseParser.execute(productInfo);
        Purchase purchase = new Purchase(stock, purchaseProducts);
    }
}
