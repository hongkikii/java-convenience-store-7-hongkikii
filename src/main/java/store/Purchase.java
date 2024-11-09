package store;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import store.Receipt.PriceInfo;
import store.Receipt.ProductInfo;

public class Purchase {
    private static final String PRODUCT_NAME_NOT_EXISTED = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String PRODUCT_QUANTITY_EXCEEDED = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private final InputView inputView;
    private final Stock stock;
    private final Map<String, Integer> purchaseProducts;
    private final Map<String, Integer> generalProducts;
    private final Map<String, Integer> promotionProducts;
    private final Map<String, Integer> freeProducts;

    public Purchase(InputView inputView, Stock stock, Map<String, Integer> purchaseProducts) {
        this.stock = stock;
        validate(purchaseProducts);
        this.purchaseProducts = purchaseProducts;
        this.inputView = inputView;
        this.generalProducts = new HashMap<>();
        this.promotionProducts = new HashMap<>();
        this.freeProducts = new HashMap<>();
        execute();
    }

    public int getGeneralPrice() {
        int generalPrice = 0;
        for(String productName: generalProducts.keySet()) {
            Product generalProduct = stock.getGeneralProduct(productName);
            generalPrice += generalProduct.getPrice() * generalProducts.get(productName);
        }
        return generalPrice;
    }

    public Map<String, Integer> getGeneralProducts() {
        return Collections.unmodifiableMap(generalProducts);
    }

    public Map<String, Integer> getPromotionProducts() {
        return Collections.unmodifiableMap(promotionProducts);
    }

    public Map<String, Integer> getFreeProducts() {
        return Collections.unmodifiableMap(freeProducts);
    }

    public Receipt getInfo(Membership membership) {
        int generalPrice = generalProducts.keySet().stream()
                .mapToInt(productionName ->
                        stock.getGeneralProduct(productionName).getPrice() * generalProducts.get(productionName))
                .sum();

        int promotionPrice = promotionProducts.keySet().stream()
                .mapToInt(productionName ->
                        stock.getPromotionProduct(productionName).getPrice() * promotionProducts.get(productionName))
                .sum();

        int freePrice = freeProducts.keySet().stream()
                .mapToInt(productName ->
                        stock.getPromotionProduct(productName).getPrice() * freeProducts.get(productName))
                .sum();

        int totalPrice = generalPrice + promotionPrice + freePrice;
        int membershipPrice = membership.getPrice();
        int paymentPrice = totalPrice - membershipPrice - freePrice;

        PriceInfo priceInfo = new PriceInfo(totalPrice, freePrice, membershipPrice, paymentPrice);

        List<ProductInfo> totalProductInfo = new ArrayList<>();
        for (String productName : purchaseProducts.keySet()) {
            Product product = stock.getGeneralProduct(productName);
            int count = purchaseProducts.get(productName);
            totalProductInfo.add(new ProductInfo(productName, count, product.getPrice() * count));
        }

        List<ProductInfo> freeProductInfo = new ArrayList<>();
        for (String productName : freeProducts.keySet()) {
            Product product = stock.getPromotionProduct(productName);
            freeProductInfo.add(new ProductInfo(productName, freeProducts.get(productName), product.getPrice()));
        }

        return new Receipt(totalProductInfo, freeProductInfo, priceInfo);
    }

    private void execute() {
        for(String productName : purchaseProducts.keySet()) {
            if(stock.hasPromotion(productName)) {
                applyPromotion(productName);
                continue;
            }
            applyGeneralPayment(productName);
        }
    }

    private void applyGeneralPayment(String productName) {
        int purchaseCount = purchaseProducts.get(productName);
        Product generalProduct = stock.getGeneralProduct(productName);
        generalProduct.deduct(purchaseCount);
        generalProducts.put(productName, purchaseCount);
    }

    private void applyPromotion(String productName) {
        Product promotionProduct = stock.getPromotionProduct(productName);
        Product generalProduct = stock.getGeneralProduct(productName);
        int purchaseCount = purchaseProducts.get(productName);

        PromotionResult promotionResult = calculatePromotionResult(promotionProduct, purchaseCount);
        if(isPromotionStockNotEnough(promotionProduct)) {
            processShortage(promotionProduct, generalProduct, promotionResult);
        }
        record(promotionProduct, promotionResult);
        processAdditionalFreeProduct(promotionProduct, promotionResult);
    }

    private void processAdditionalFreeProduct(Product promotionProduct, PromotionResult promotionResult) {
        if (promotionResult.getRemainder() == promotionProduct.getPromotionType().getPurchaseCount()
                && promotionProduct.getQuantity() >= promotionResult.getFreeCount()) {
            int freeCount = promotionProduct.getPromotionType().getFreeCount();
            if(isPositiveToAdd(promotionProduct.getName(), freeCount)) {
                addFreeProduct(promotionProduct);
            }
        }
    }

    private void record(Product promotionProduct, PromotionResult promotionResult) {
        String productName = promotionProduct.getName();
        promotionProducts.put(productName, promotionResult.getPayCount());
        freeProducts.put(productName, promotionResult.getFreeCount());
        promotionProduct.deduct(purchaseProducts.get(productName));
    }

    private boolean isPromotionStockNotEnough(Product promotionProduct) {
        return promotionProduct.getQuantity() < purchaseProducts.get(promotionProduct.getName());
    }

    private void processShortage(Product promotionProduct, Product generalProduct, PromotionResult promotionResult) {
        PromotionType promotionType = promotionProduct.getPromotionType();
        int unit = promotionType.getPurchaseCount() + promotionType.getFreeCount();
        int availablePromotionUnit = promotionProduct.getQuantity() / unit;
        int requiredGeneralCount = purchaseProducts.get(promotionProduct.getName()) - (availablePromotionUnit * unit);

        String productName = promotionProduct.getName();
        if (isPositiveToGeneral(productName, requiredGeneralCount)) {
            int promotionRemain = promotionProduct.getQuantity() - requiredGeneralCount;
            if (promotionRemain > 0) {
                promotionProduct.deduct(promotionRemain);
                generalProduct.deduct(requiredGeneralCount - promotionRemain);
            }
            else {
                generalProduct.deduct(requiredGeneralCount);
            }
            generalProducts.put(productName, requiredGeneralCount);
        }
        promotionResult.exceptShortage(requiredGeneralCount, availablePromotionUnit);
        purchaseProducts.put(productName, purchaseProducts.get(productName) - requiredGeneralCount);
    }

    private PromotionResult calculatePromotionResult(Product promotionProduct, int purchaseCount) {
        PromotionType promotionType = promotionProduct.getPromotionType();
        return promotionType.getResult(purchaseCount);
    }

    private boolean isPositiveToGeneral(String productName, int generalCount) {
        String answer;
        do {
            answer = inputView.readGeneralAnswer(productName, generalCount);
            try {
                return AnswerValidator.validate(answer);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (answer == null);
        return false;
    }

    private boolean isPositiveToAdd(String productionName, int freeCount) {
        String answer;
        do {
            answer = inputView.readPromotionAnswer(productionName, freeCount);
            try {
                return AnswerValidator.validate(answer);
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        } while (answer == null);
        return false;
    }

    private void addFreeProduct(Product promotionProduct) {
        int freeCount = promotionProduct.getPromotionType().getFreeCount();
        String productName = promotionProduct.getName();
        promotionProduct.deduct(freeCount);
        freeProducts.put(productName, freeProducts.get(productName) + freeCount);
        purchaseProducts.put(productName, purchaseProducts.get(productName) + freeCount);
    }

    private void validate(Map<String, Integer> purchaseProducts) {
        validateProductName(purchaseProducts.keySet());
        validateProductQuantity(purchaseProducts);
    }

    private void validateProductName(Set<String> purchaseProductNames) {
        List<Product> products = stock.get();
        for (String purchaseProductName : purchaseProductNames) {
            boolean productExists = products.stream()
                    .anyMatch(product -> product.getName().equals(purchaseProductName));
            if (!productExists) {
                throw new IllegalArgumentException(PRODUCT_NAME_NOT_EXISTED);
            }
        }
    }

    private void validateProductQuantity(Map<String, Integer> purchaseProducts) {
        List<Product> products = stock.get();
        purchaseProducts.forEach((productName, requiredQuantity) -> {
            int availableQuantity = getAvailableQuantity(products, productName);
            if (availableQuantity < requiredQuantity) {
                throw new IllegalArgumentException(PRODUCT_QUANTITY_EXCEEDED);
            }
        });
    }

    private int getAvailableQuantity(List<Product> products, String productName) {
        return products.stream()
                .filter(product -> product.getName().equals(productName))
                .mapToInt(Product::getQuantity)
                .sum();
    }
}
