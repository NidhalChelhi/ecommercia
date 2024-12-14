package ecommercia.model.orders;

public enum PaymentMethod {
    CREDIT_CARD("Credit Card"),
    PAYPAL("PayPal"),
    BANK_TRANSFER("Bank Transfer"),
    CASH_ON_DELIVERY("Cash on Delivery");

    private final String displayName;

    PaymentMethod(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Map display name to enum constant
    public static PaymentMethod fromDisplayName(String displayName) {
        for (PaymentMethod method : PaymentMethod.values()) {
            if (method.getDisplayName().equalsIgnoreCase(displayName)) {
                return method;
            }
        }
        throw new IllegalArgumentException("Invalid PaymentMethod: " + displayName);
    }

    // Map database value to enum constant
    public static PaymentMethod fromDatabaseValue(String value) {
        return PaymentMethod.valueOf(value.toUpperCase());
    }
}
