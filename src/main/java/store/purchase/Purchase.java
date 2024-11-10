package store.purchase;

import store.dto.PurchasePriceInfo;
import store.view.InputView;
import store.inventory.Product;
import store.dto.PromotionResult;
import store.inventory.PromotionType;
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
            applyGeneralPayment(productName);
        }
    }

    private void applyPromotion(String productName) {
        Product promotionProduct = stock.getPromotionProduct(productName);
        Product generalProduct = stock.getGeneralProduct(productName);
        int desiredQuantity = cart.getQuantity(productName);

        PromotionResult promotionResult = calculatePromotionResult(promotionProduct, desiredQuantity);
        if(isPromotionStockNotEnough(promotionProduct)) {
            processShortage(promotionProduct, generalProduct, promotionResult);
        }
        record(promotionProduct, promotionResult);
        processAdditionalFreeProduct(promotionProduct, promotionResult);
    }

    private void applyGeneralPayment(String productName) {
        int desiredQuantity = cart.getQuantity(productName);
        Product generalProduct = stock.getGeneralProduct(productName);
        generalProduct.deduct(desiredQuantity);
        nonPromotionPurchase.add(productName, desiredQuantity);
    }

    private void processAdditionalFreeProduct(Product promotionProduct, PromotionResult promotionResult) {
        if (promotionResult.getRemainder() == promotionProduct.getPromotionType().getPurchaseCount()
                && promotionProduct.getQuantity() >= promotionResult.getFreeCount()) {
            int freeCount = promotionProduct.getPromotionType().getFreeCount();
            if(inputView.isPositiveToAdd(promotionProduct.getName(), freeCount)) {
                addFreeProduct(promotionProduct);
            }
        }
    }

    private void record(Product promotionProduct, PromotionResult promotionResult) {
        String productName = promotionProduct.getName();
        promotionPurchase.add(productName, promotionResult.getPayCount());
        freeGiftItem.add(productName, promotionResult.getFreeCount());
        promotionProduct.deduct(cart.getQuantity(productName));
    }

    private boolean isPromotionStockNotEnough(Product promotionProduct) {
        return promotionProduct.getQuantity() < cart.getQuantity(promotionProduct.getName());
    }

    private void processShortage(Product promotionProduct, Product generalProduct, PromotionResult promotionResult) {
        PromotionType promotionType = promotionProduct.getPromotionType();
        String productName = promotionProduct.getName();
        int purchaseCount = cart.getQuantity(productName);
        int promotionQuantity = promotionProduct.getQuantity();

        int promotionUnit = promotionType.getPurchaseCount() + promotionType.getFreeCount();
        int availablePromotionUnit = promotionQuantity / promotionUnit;
        int requiredGeneralCount = purchaseCount - (availablePromotionUnit * promotionUnit);

        promotionResult.exceptShortage(requiredGeneralCount, availablePromotionUnit);

        if (inputView.isPositiveToGeneral(productName, requiredGeneralCount)) {
            int promotionRemain = promotionQuantity - requiredGeneralCount;
            if (promotionRemain > 0) {
                promotionProduct.deduct(promotionRemain);
                generalProduct.deduct(requiredGeneralCount - promotionRemain);
            } else {
                generalProduct.deduct(requiredGeneralCount);
            }
            nonPromotionPurchase.add(productName, requiredGeneralCount);
            return;
        }
        cart.add(productName, purchaseCount - requiredGeneralCount);
    }

    private PromotionResult calculatePromotionResult(Product promotionProduct, int purchaseCount) {
        PromotionType promotionType = promotionProduct.getPromotionType();
        return promotionType.getResult(purchaseCount);
    }

    private void addFreeProduct(Product promotionProduct) {
        int freeCount = promotionProduct.getPromotionType().getFreeCount();
        String productName = promotionProduct.getName();
        promotionProduct.deduct(freeCount);
        freeGiftItem.add(productName, freeGiftItem.getQuantity(productName) + freeCount);
        cart.add(productName, cart.getQuantity(productName) + freeCount);
    }
}
