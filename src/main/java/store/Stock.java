package store;

import java.util.Collections;
import java.util.List;

public class Stock {
    private final List<Product> products;

    public Stock() {
        ProductLoader productLoader = new ProductLoader();
        this.products = productLoader.execute();
    }

    public List<Product> get() {
        return Collections.unmodifiableList(products);
    }
}
