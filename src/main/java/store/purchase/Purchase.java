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
    private final Map<String, Integer> generalPurchaseProducts; // 프로모션 미적용 구매 상품 이름, 개수
    private final Map<String, Integer> promotionPurchaseProducts; // 프로모션 적용 구매 상품 이름, 개수
    private final Map<String, Integer> freeProducts; // 프로모션 적용에 의한 공짜 상품 이름, 개수

    private final DesiredProduct desiredProduct;

    public Purchase(InputView inputView, Stock stock, DesiredProduct desiredProduct) {
        this.stock = stock;
        this.desiredProduct = desiredProduct;
        this.inputView = inputView;
        this.generalPurchaseProducts = new HashMap<>();
        this.promotionPurchaseProducts = new HashMap<>();
        this.freeProducts = new HashMap<>();
        execute();
    }

    public int getGeneralPrice() {
        int generalPrice = 0;
        for(String productName: generalPurchaseProducts.keySet()) {
            Product generalProduct = stock.getGeneralProduct(productName);
            generalPrice += generalProduct.getPrice() * generalPurchaseProducts.get(productName);
        }
        return generalPrice;
    }

    public Map<String, Integer> getGeneralPurchaseProducts() {
        return Collections.unmodifiableMap(generalPurchaseProducts);
    }

    public Map<String, Integer> getPromotionPurchaseProducts() {
        return Collections.unmodifiableMap(promotionPurchaseProducts);
    }

    public Map<String, Integer> getFreeProducts() {
        return Collections.unmodifiableMap(freeProducts);
    }

    public Receipt getInfo(Membership membership) {
        int purchasePrice = desiredProduct.getTotalPrice();

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

        return new Receipt(desiredProduct.getInfo(), freePurchaseProductInfo, purchasePriceInfo);
    }

    private void execute() {
        for(String productName : desiredProduct.getNames()) {
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
        int desiredQuantity = desiredProduct.getQuantity(productName);

        PromotionResult promotionResult = calculatePromotionResult(promotionProduct, desiredQuantity);
        if(isPromotionStockNotEnough(promotionProduct)) {
            processShortage(promotionProduct, generalProduct, promotionResult);
        }
        record(promotionProduct, promotionResult);
        processAdditionalFreeProduct(promotionProduct, promotionResult);
    }

    private void applyGeneralPayment(String productName) {
        int desiredQuantity = desiredProduct.getQuantity(productName);
        Product generalProduct = stock.getGeneralProduct(productName);
        generalProduct.deduct(desiredQuantity);
        generalPurchaseProducts.put(productName, desiredQuantity);
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
        promotionPurchaseProducts.put(productName, promotionResult.getPayCount());
        freeProducts.put(productName, promotionResult.getFreeCount());
        promotionProduct.deduct(desiredProduct.getQuantity(productName));
    }

    private boolean isPromotionStockNotEnough(Product promotionProduct) {
        return promotionProduct.getQuantity() < desiredProduct.getQuantity(promotionProduct.getName());
    }

    private void processShortage(Product promotionProduct, Product generalProduct, PromotionResult promotionResult) {
        PromotionType promotionType = promotionProduct.getPromotionType();
        String productName = promotionProduct.getName();
        int purchaseCount =desiredProduct.getQuantity(productName);
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
            generalPurchaseProducts.put(productName, requiredGeneralCount);
            return;
        }
        desiredProduct.record(productName, purchaseCount - requiredGeneralCount);
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
        desiredProduct.record(productName, desiredProduct.getQuantity(productName) + freeCount);
    }
}
