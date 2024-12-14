package ecommercia.model.orders;

public enum OrderStatus {
    PENDING("Pending"),
    SHIPPED("Shipped"),
    DELIVERED("Delivered"),
    CANCELED("Canceled");

    private final String displayName;

    OrderStatus(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    // Map display name to enum constant
    public static OrderStatus fromDisplayName(String displayName) {
        for (OrderStatus status : OrderStatus.values()) {
            if (status.getDisplayName().equalsIgnoreCase(displayName)) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid OrderStatus: " + displayName);
    }

    // Map database value to enum constant
    public static OrderStatus fromDatabaseValue(String value) {
        return OrderStatus.valueOf(value.toUpperCase());
    }
}
