package store;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Purchase {
    private static final String PRODUCT_NAME_NOT_EXISTED = "[ERROR] 존재하지 않는 상품입니다. 다시 입력해 주세요.";
    private static final String PRODUCT_QUANTITY_EXCEEDED = "[ERROR] 재고 수량을 초과하여 구매할 수 없습니다. 다시 입력해 주세요.";

    private final InputView inputView;
    private final Stock stock;
    private final Map<String, Integer> purchaseProducts;
    private final Map<String, Integer> payProducts;
    private final Map<String, Integer> freeProducts;

    public Purchase(InputView inputView, Stock stock, Map<String, Integer> purchaseProducts) {
        this.stock = stock;
        validate(purchaseProducts);
        this.purchaseProducts = purchaseProducts;
        this.inputView = inputView;
        this.payProducts = new HashMap<>();
        this.freeProducts = new HashMap<>();
        execute();
    }

    public Map<String, Integer> getPayProducts() {
        return Collections.unmodifiableMap(payProducts);
    }

    public Map<String, Integer> getFreeProducts() {
        return Collections.unmodifiableMap(freeProducts);
    }

    private void execute() {
        for(String productName : purchaseProducts.keySet()) {
            List<Product> products = stock.get();
            List<Product> purchaseCandidatesProduct = products.stream()
                    .filter(product -> product.getName().equals(productName))
                    .toList();
            if (purchaseCandidatesProduct.size() == 2) {
                Product promotionProduct = purchaseCandidatesProduct.stream()
                        .filter(product -> !product.getPromotionType().equals(PromotionType.NONE))
                        .findAny()
                        .get();
                int purchaseCount = purchaseProducts.get(productName);
                int promotionUnit = 0;
                int remainder = 0;
                int payCount = 0;
                int freeCount = 0;
                PromotionType promotionType = promotionProduct.getPromotionType();
                if (promotionType == PromotionType.TWO_PLUS_ONE) {
                    promotionUnit = purchaseCount / 3;
                    remainder = purchaseCount % 3;
                    payCount = promotionUnit * 2 + remainder;
                    freeCount = promotionUnit;
                }
                if (promotionType == PromotionType.ONE_PLUS_ONE) {
                    promotionUnit = purchaseCount / 2;
                    remainder = purchaseCount % 2;
                    payCount = promotionUnit + remainder;
                    freeCount = promotionUnit;
                }
                payProducts.put(productName, payCount);
                freeProducts.put(productName, freeCount);
                promotionProduct.deduct(purchaseProducts.get(productName));

                if (remainder == promotionType.getPurchaseCount()
                        && promotionProduct.getQuantity() >= freeCount) {
                    if(isPositiveToAdd(productName, freeCount)) {
                        addFreeProduct(promotionProduct);
                    }
                }
            }
        }
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
