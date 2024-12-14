package ecommercia.model.orders;

@FunctionalInterface
public interface DiscountCalculator {
    double calculateDiscount(double totalAmount);
}
