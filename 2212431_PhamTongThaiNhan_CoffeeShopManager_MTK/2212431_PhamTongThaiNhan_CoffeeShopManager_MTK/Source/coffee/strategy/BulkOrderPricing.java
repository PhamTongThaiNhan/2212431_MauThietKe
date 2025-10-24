package coffee.strategy;

public class BulkOrderPricing implements PricingStrategy {
    public double getPrice(double basePrice, int quantity) {
        if (quantity >= 5) return basePrice * quantity * 0.85;
        return basePrice * quantity;
    }
}
