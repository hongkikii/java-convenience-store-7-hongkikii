package store.purchase;

import store.dto.PurchasePriceInfo;
import store.view.InputView;
import store.inventory.Product;
import store.dto.PromotionResult;
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

    public NonPromotionPurchase getNonPromotionPurchase() {
        return nonPromotionPurchase;
    }

    public PromotionPurchase getPromotionPurchase() {
        return promotionPurchase;
    }

    public FreeGiftItem getFreeGiftItem() {
        return freeGiftItem;
    }

    // TODO : 분리
    public Receipt getInfo(Membership membership) {
        int purchasePrice = cart.getTotalPrice();
        int freePrice = freeGiftItem.getTotalPrice();
        int membershipPrice = membership.getPrice();
        int paymentPrice = purchasePrice - membershipPrice - freePrice;

        PurchasePriceInfo purchasePriceInfo = new PurchasePriceInfo(purchasePrice, freePrice, membershipPrice, paymentPrice);
        return new Receipt(cart.getInfo(), freeGiftItem.getInfo(), purchasePriceInfo);
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

        PromotionResult promotionResult = promotionProduct.getPromotionType().getResult(desiredQuantity);
        if(stock.isPromotionStockNotEnough(productName, desiredQuantity)) {
            processShortage(promotionProduct, generalProduct, promotionResult);
        }
        record(promotionProduct, promotionResult);
        processAdditionalFreeProduct(promotionProduct, promotionResult);
    }

    private void applyGeneralPurchase(String productName) {
        int desiredQuantity = cart.getQuantity(productName);
        Product generalProduct = stock.getGeneralProduct(productName);
        generalProduct.deduct(desiredQuantity);
        nonPromotionPurchase.add(productName, desiredQuantity);
    }

    private void processShortage(Product promotionProduct, Product generalProduct, PromotionResult promotionResult) {
        String productName = promotionProduct.getName();
        int desiredQuantity = cart.getQuantity(productName);
        int shortageQuantity = calculateShortageQuantity(promotionProduct, desiredQuantity);

        promotionResult.modify(shortageQuantity, calculateAvailableFreeGiftQuantity(promotionProduct));
        if (inputView.readPositiveToGeneral(productName, shortageQuantity)) {
            nonPromotionPurchase.replacePromotion(promotionProduct, generalProduct, shortageQuantity);
        }
        cart.add(productName, desiredQuantity - shortageQuantity);
    }

    private void record(Product promotionProduct, PromotionResult promotionResult) {
        String productName = promotionProduct.getName();
        promotionPurchase.add(productName, promotionResult.getPayQuantity());
        freeGiftItem.add(productName, promotionResult.getFreeQuantity());
        promotionProduct.deduct(cart.getQuantity(productName));
    }

    private void processAdditionalFreeProduct(Product promotionProduct, PromotionResult promotionResult) {
        if (promotionResult.getRemainder() == promotionProduct.getPromotionType().getPurchaseCount()
                && promotionProduct.getQuantity() >= promotionResult.getFreeQuantity()) {
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
