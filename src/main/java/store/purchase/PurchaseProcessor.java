package store.purchase;

import store.purchase.item.FreeGiftItem;
import store.purchase.item.GeneralPurchaseItem;
import store.purchase.item.PromotionPurchaseItem;
import store.view.InputView;
import store.inventory.Product;
import store.dto.PromotionDetails;
import store.inventory.Stock;

public class PurchaseProcessor {
    private final InputView inputView;
    private final Stock stock;
    private final Cart cart;
    private final GeneralPurchaseItem generalPurchaseItem;
    private final PromotionPurchaseItem promotionPurchaseItem;
    private final FreeGiftItem freeGiftItem;

    public PurchaseProcessor(InputView inputView, Stock stock, Cart cart) {
        this.stock = stock;
        this.cart = cart;
        this.inputView = inputView;
        this.generalPurchaseItem = new GeneralPurchaseItem(stock);
        this.promotionPurchaseItem = new PromotionPurchaseItem(stock);
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
        generalPurchaseItem.add(productName, desiredQuantity);
    }

    private void processShortage(Product promotionProduct, Product generalProduct, PromotionDetails promotionDetails) {
        String productName = promotionProduct.getName();
        int desiredQuantity = cart.getQuantity(productName);
        int shortageQuantity = calculateShortageQuantity(promotionProduct, desiredQuantity);

        promotionDetails.modify(shortageQuantity, calculateAvailableFreeGiftQuantity(promotionProduct));
        if (inputView.readPositiveToGeneral(productName, shortageQuantity)) {
            generalPurchaseItem.replacePromotion(promotionProduct, generalProduct, shortageQuantity);
        }
        cart.add(productName, desiredQuantity - shortageQuantity);
    }

    private void record(Product promotionProduct, PromotionDetails promotionDetails) {
        String productName = promotionProduct.getName();
        promotionPurchaseItem.add(productName, promotionDetails.getPayQuantity());
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