package store;

public class Application {
    public static void main(String[] args) {
        OutputView outputView = new OutputView();
        outputView.showStartPrompt();

        outputView.showStockPrompt();
        Stock stock = new Stock();
        outputView.show(stock);


    }
}
