package store.purchase;

import store.view.InputView;
import store.inventory.Product;
import store.dto.PromotionDetails;
import store.inventory.Stock;

public class Purchase {
    private final InputView inputView;
    private final Stock stock;
    private final Cart cart;
    private final NonPromotionPurchase nonPromotionPurchase;
    private final PromotionPurchase promotionPurchase;
    private final FreeGiftItem freeGiftItem;

    public Purchase(InputView inputView, Stock stock, Cart cart) {
        this.stock = stock;
        this.cart = cart;
        this.inputView = inputView;
        this.nonPromotionPurchase = new NonPromotionPurchase(stock);
        this.promotionPurchase = new PromotionPurchase(stock);
        this.freeGiftItem = new FreeGiftItem(stock);
        execute();
    }

    public Cart getCart() {
        return cart;
    }

    public NonPromotionPurchase getNonPromotionPurchase() {
        return nonPromotionPurchase;
    }

    public PromotionPurchase getPromotionPurchase() {
        return promotionPurchase;
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

        PromotionDetails promotionDetails = promotionProduct.getPromotionDetails(desiredQuantity);
        if(stock.isPromotionStockNotEnough(productName, desiredQuantity)) {
            processShortage(promotionProduct, generalProduct, promotionDetails);
        }
        record(promotionProduct, promotionDetails);
        processAdditionalFreeGift(promotionProduct, promotionDetails);
    }

    private void applyGeneralPurchase(String productName) {
        int desiredQuantity = cart.getQuantity(productName);
        Product generalProduct = stock.getGeneralProduct(productName);
        generalProduct.deduct(desiredQuantity);
        nonPromotionPurchase.add(productName, desiredQuantity);
    }

    private void processShortage(Product promotionProduct, Product generalProduct, PromotionDetails promotionDetails) {
        String productName = promotionProduct.getName();
        int desiredQuantity = cart.getQuantity(productName);
        int shortageQuantity = calculateShortageQuantity(promotionProduct, desiredQuantity);

        promotionDetails.modify(shortageQuantity, calculateAvailableFreeGiftQuantity(promotionProduct));
        if (inputView.readPositiveToGeneral(productName, shortageQuantity)) {
            nonPromotionPurchase.replacePromotion(promotionProduct, generalProduct, shortageQuantity);
        }
        cart.add(productName, desiredQuantity - shortageQuantity);
    }

    private void record(Product promotionProduct, PromotionDetails promotionDetails) {
        String productName = promotionProduct.getName();
        promotionPurchase.add(productName, promotionDetails.getPayQuantity());
        freeGiftItem.add(productName, promotionDetails.getFreeQuantity());
        promotionProduct.deduct(cart.getQuantity(productName));
    }

    private void processAdditionalFreeGift(Product promotionProduct, PromotionDetails promotionDetails) {
        if (promotionDetails.getRemainder() == promotionProduct.getPromotionType().getPurchaseCount()
                && promotionProduct.getQuantity() >= promotionDetails.getFreeQuantity()) {
            int freeCount = promotionProduct.getPromotionType().getFreeCount();
            if(inputView.readPositiveToAdd(promotionProduct.getName(), freeCount)) {
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
