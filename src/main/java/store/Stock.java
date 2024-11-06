package store;

import camp.nextstep.edu.missionutils.DateTimes;
import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Stock {
    private static final String PRODUCTS_FILE_PATH = "src/main/resources/products.md";
    private static final String PROMOTIONS_FILE_PATH = "src/main/resources/promotions.md";

    private final List<Product> products;
    private final Map<String, PromotionType> validPromotions;

    public Stock() {
        this.products = new ArrayList<>();
        this.validPromotions = new HashMap<>();
        loadPromotion();
        loadProduct();
    }

    public List<Product> get() {
        return Collections.unmodifiableList(products);
    }

    private void loadPromotion() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PROMOTIONS_FILE_PATH))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int buy = Integer.parseInt(parts[1]);
                int get = Integer.parseInt(parts[2]);
                LocalDate startDate = LocalDate.parse(parts[3], formatter);
                LocalDate endDate = LocalDate.parse(parts[4], formatter);
                LocalDate currentDate = LocalDate.from(DateTimes.now());
                if ((currentDate.isEqual(startDate) || currentDate.isAfter(startDate)) &&
                        (currentDate.isEqual(endDate) || currentDate.isBefore(endDate))) {
                    PromotionType promotionType = PromotionType.from(buy, get);
                    validPromotions.put(name, promotionType);
                    continue;
                }
                validPromotions.put(name, PromotionType.NOT_NOW);
            }
        }
        catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }

    private void loadProduct() {
        try (BufferedReader reader = Files.newBufferedReader(Paths.get(PRODUCTS_FILE_PATH))) {
            reader.readLine();
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String name = parts[0];
                int price = Integer.parseInt(parts[1]);
                int quantity = Integer.parseInt(parts[2]);
                String promotionName = parts[3];
                PromotionType promotionType = validPromotions.getOrDefault(promotionName, PromotionType.NONE);
                if(promotionType == PromotionType.NOT_NOW) {
                    continue;
                }
                Product product = new Product(name, price, quantity, promotionType, promotionName);
                products.add(product);
            }
        }
        catch (IOException e) {
            throw new IllegalArgumentException("[ERROR] 잘못된 입력입니다. 다시 입력해 주세요.");
        }
    }
}
