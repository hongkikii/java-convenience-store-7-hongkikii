package store.receipt;

public record ReceiptPrice(int totalPrice,
                           int promotionPrice,
                           int membershipPrice,
                           int paymentPrice) {
}
