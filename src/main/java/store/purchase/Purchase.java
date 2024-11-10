package store.purchase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import store.dto.PurchasePriceInfo;
import store.dto.PurchaseProductInfo;
import store.view.InputView;
import store.inventory.Product;
import store.dto.PromotionResult;
import store.inventory.PromotionType;
import store.inventory.Stock;

public class Purchase {
    private final InputView inputView;
    private final Stock stock; // 재고
    private final Map<String, Integer> freeProducts; // 프로모션 적용에 의한 공짜 상품 이름, 개수

    private final Cart cart;
    private final NonPromotionPurchase nonPromotionPurchase;
    private final PromotionPurchase promotionPurchase;

    public Purchase(InputView inputView, Stock stock, Cart cart) {
        this.stock = stock;
        this.cart = cart;
        this.inputView = inputView;
        this.nonPromotionPurchase = new NonPromotionPurchase(stock);
        this.promotionPurchase = new PromotionPurchase(stock);
        this.freeProducts = new HashMap<>();
        execute();
    }

    public NonPromotionPurchase getNonPromotionPurchase() {
        return nonPromotionPurchase;
    }

    public PromotionPurchase getPromotionPurchase() {
        return promotionPurchase;
    }

    public Map<String, Integer> getFreeProducts() {
        return Collections.unmodifiableMap(freeProducts);
    }

    public Receipt getInfo(Membership membership) {
        int purchasePrice = cart.getTotalPrice();

        int freePrice = 0;
        List<PurchaseProductInfo> freePurchaseProductInfo = new ArrayList<>();
        for (String productName : freeProducts.keySet()) {
            Product product = stock.getPromotionProduct(productName);
            int count = freeProducts.get(productName);
            if(count <= 0) continue;
            int price = product.getPrice();
            freePurchaseProductInfo.add(new PurchaseProductInfo(productName, count, price));
            freePrice += count * price;
        }

        int membershipPrice = membership.getPrice();
        int paymentPrice = purchasePrice - membershipPrice - freePrice;

        PurchasePriceInfo purchasePriceInfo = new PurchasePriceInfo(purchasePrice, freePrice, membershipPrice, paymentPrice);

        return new Receipt(cart.getInfo(), freePurchaseProductInfo, purchasePriceInfo);
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
        freeProducts.put(productName, promotionResult.getFreeCount());
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
        freeProducts.put(productName, freeProducts.get(productName) + freeCount);
        cart.add(productName, cart.getQuantity(productName) + freeCount);
    }
}
