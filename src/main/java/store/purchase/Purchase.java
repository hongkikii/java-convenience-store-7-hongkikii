package store.purchase;

import store.cart.Cart;
import store.purchase.item.FreeGiftItem;
import store.purchase.item.GeneralPurchaseItem;
import store.purchase.item.PromotionPurchaseItem;
import store.view.InputView;
import store.inventory.product.Product;
import store.inventory.promotion.PromotionResult;
import store.inventory.Stock;

public class Purchase {
    private final InputView inputView;
    private final Stock stock;
    private final Cart cart;
    private final GeneralPurchaseItem generalPurchaseItem;
    private final PromotionPurchaseItem promotionPurchaseItem;
    private final FreeGiftItem freeGiftItem;

    public Purchase(InputView inputView, Stock stock, Cart cart) {
        this.stock = stock;
        this.cart = cart;
        this.inputView = inputView;
        this.generalPurchaseItem = new GeneralPurchaseItem(stock);
        this.promotionPurchaseItem = new PromotionPurchaseItem();
        this.freeGiftItem = new FreeGiftItem(stock);
        execute();
    }

    public Cart getCart() {
        return cart;
    }

    public GeneralPurchaseItem getNonPromotionPurchaseItem() {
        return generalPurchaseItem;
    }

    public PromotionPurchaseItem getPromotionPurchaseItem() {
        return promotionPurchaseItem;
    }

    public FreeGiftItem getFreeGiftItem() {
        return freeGiftItem;
    }

    private void execute() {
        for(String productName : cart.getProductNames()) {
            if(stock.hasPromotion(productName)) {
                applyPromotion(productName);
                continue;
            }
            applyGeneralPurchase(productName);
        }
    }

    private void applyPromotion(String productName) {
        Product promotionProduct = stock.getPromotionProduct(productName);
        Product generalProduct = stock.getGeneralProduct(productName);
        int desiredQuantity = cart.getQuantity(productName);

        PromotionResult promotionResult = promotionProduct.getPromotionDetails(desiredQuantity);
        if(stock.isPromotionStockNotEnough(productName, desiredQuantity)) {
            processShortage(promotionProduct, generalProduct, promotionResult);
        }
        record(promotionProduct, promotionResult);
        processAdditionalFreeGift(promotionProduct, promotionResult);
    }

    private void applyGeneralPurchase(String productName) {
        int desiredQuantity = cart.getQuantity(productName);
        Product generalProduct = stock.getGeneralProduct(productName);
        generalProduct.deduct(desiredQuantity);
        generalPurchaseItem.add(productName, desiredQuantity);
    }

    private void processShortage(Product promotionProduct, Product generalProduct, PromotionResult promotionResult) {
        String productName = promotionProduct.getName();
        int desiredQuantity = cart.getQuantity(productName);
        int shortageQuantity = calculateShortageQuantity(promotionProduct, desiredQuantity);

        promotionResult.modify(shortageQuantity, calculateAvailableFreeGiftQuantity(promotionProduct));
        if (inputView.isPositiveToGeneral(productName, shortageQuantity)) {
            generalPurchaseItem.replacePromotion(promotionProduct, generalProduct, shortageQuantity);
            return;
        }
        cart.add(productName, desiredQuantity - shortageQuantity);
    }

    private void record(Product promotionProduct, PromotionResult promotionResult) {
        String productName = promotionProduct.getName();
        promotionPurchaseItem.add(productName, promotionResult.getPayQuantity());
        freeGiftItem.add(productName, promotionResult.getFreeQuantity());
        promotionProduct.deduct(cart.getQuantity(productName));
    }

    private void processAdditionalFreeGift(Product promotionProduct, PromotionResult promotionResult) {
        if (promotionResult.getRemainder() == promotionProduct.getPromotionType().getPurchaseCount()
                && promotionProduct.getQuantity() >= promotionResult.getFreeQuantity()) {
            int freeCount = promotionProduct.getPromotionType().getFreeCount();
            if(inputView.isPositiveToAdd(promotionProduct.getName(), freeCount)) {
                addFreeProduct(promotionProduct);
            }
        }
    }

    private int calculateShortageQuantity(Product promotionProduct, int desiredQuantity) {
        int promotionUnit = promotionProduct.getPromotionType().getPromotionUnit();
        int availablePromotionUnit = calculateAvailableFreeGiftQuantity(promotionProduct);
        return desiredQuantity - (availablePromotionUnit * promotionUnit);
    }

    private int calculateAvailableFreeGiftQuantity(Product promotionProduct) {
        int promotionUnit = promotionProduct.getPromotionType().getPromotionUnit();
        return promotionProduct.getQuantity() / promotionUnit;
    }

    private void addFreeProduct(Product promotionProduct) {
        int freeCount = promotionProduct.getPromotionType().getFreeCount();
        String productName = promotionProduct.getName();
        promotionProduct.deduct(freeCount);
        freeGiftItem.add(productName, freeCount);
        cart.add(productName, freeCount);
    }
}
