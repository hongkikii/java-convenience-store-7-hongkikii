package store;

public class OutputFormatter {
    public static String formatWithNegativeSign(int price) {
        return String.format("-%,d", Math.abs(price));
    }
}
