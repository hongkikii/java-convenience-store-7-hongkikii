package store;

import store.inventory.Stock;
import store.inventory.product.ProductProcessor;
import store.inventory.promotion.PromotionProcessor;
import store.view.InputView;
import store.view.OutputView;

public class ApplicationContext {
    private final InputView inputView;
    private final OutputView outputView;
    private final Stock stock;

    public ApplicationContext() {
        inputView = new InputView();
        outputView = new OutputView();
        stock = new Stock(new ProductProcessor(new PromotionProcessor()));
    }

    public InputView getInputView() {
        return inputView;
    }

    public OutputView getOutputView() {
        return outputView;
    }

    public Stock getStock() {
        return stock;
    }
}
