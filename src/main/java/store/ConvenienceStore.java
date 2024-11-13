package store;

import java.util.Map;
import store.common.AnswerValidator;
import store.inventory.Stock;
import store.cart.Cart;
import store.purchase.Membership;
import store.cart.CartParser;
import store.purchase.Purchase;
import store.receipt.Receipt;
import store.view.InputView;
import store.view.OutputView;

public class ConvenienceStore {
    private final InputView inputView;
    private final OutputView outputView;
    private final Stock stock;

    public ConvenienceStore(ApplicationContext applicationContext) {
        this.inputView = applicationContext.getInputView();
        this.outputView = applicationContext.getOutputView();
        this.stock = applicationContext.getStock();
    }

    public void run() {
        do {
            showStartScreen();
            Cart cart = addItemToCart();
            Purchase purchase = purchase(cart);
            Membership membership = applyMembership(purchase);
            displayReceipt(purchase, membership);
        } while (askForAdditionalPurchase());
    }

    private void showStartScreen() {
        outputView.showStartPrompt();
        outputView.showStockPrompt();
        outputView.show(stock);
    }

    private Cart addItemToCart() {
        while (true) {
            try {
                outputView.showPurchasePrompt();
                Map<String, Integer> desiredProducts = new CartParser().execute(inputView.readLine());
                return new Cart(stock, desiredProducts);
            }
            catch (IllegalArgumentException e) {
                outputView.show(e.getMessage());
            }
        }
    }

    private Purchase purchase(Cart cart) {
        return new Purchase(inputView, stock, cart);
    }

    private Membership applyMembership(Purchase purchase) {
        while (true) {
            try {
                outputView.showMembershipPrompt();
                boolean isMembershipApplied = AnswerValidator.validate(inputView.readLine());
                return new Membership(isMembershipApplied, purchase.getNonPromotionPurchaseItem());
            }
            catch (IllegalArgumentException e) {
                outputView.show(e.getMessage());
            }
        }
    }

    private void displayReceipt(Purchase purchase, Membership membership) {
        outputView.show(Receipt.of(purchase, membership));
    }

    private boolean askForAdditionalPurchase() {
        while (true) {
            try {
                outputView.showAdditionalPurchasePrompt();
                String input = inputView.readLine();
                return AnswerValidator.validate(input);
            }
            catch (IllegalArgumentException e) {
                outputView.show(e.getMessage());
            }
        }
    }
}
