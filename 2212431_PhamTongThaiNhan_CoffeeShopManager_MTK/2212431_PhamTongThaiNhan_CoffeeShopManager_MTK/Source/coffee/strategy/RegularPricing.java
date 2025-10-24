package coffee.strategy;

public class RegularPricing implements PricingStrategy {
    public double getPrice(double basePrice, int quantity) {
        return basePrice * quantity;
    }
}
