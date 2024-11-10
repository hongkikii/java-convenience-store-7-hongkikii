package store.dto;

public class ProductInfo {
    private String productName;
    private int count;
    private int price;

    public ProductInfo(String productName, int count, int price) {
        this.productName = productName;
        this.count = count;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public int getCount() {
        return count;
    }

    public int getPrice() {
        return price;
    }
}
