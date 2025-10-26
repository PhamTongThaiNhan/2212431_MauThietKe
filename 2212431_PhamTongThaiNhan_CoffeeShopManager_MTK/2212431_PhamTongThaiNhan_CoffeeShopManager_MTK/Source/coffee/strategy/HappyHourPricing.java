package coffee.strategy;

public class HappyHourPricing implements PricingStrategy {
    public double getPrice(double basePrice, int quantity) {
        return basePrice * quantity * 0.8;
    }
}
