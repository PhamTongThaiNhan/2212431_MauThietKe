package coffee.strategy;

public class MemberPricing implements PricingStrategy {
    public double getPrice(double basePrice, int quantity) {
        return basePrice * quantity * 0.9;
    }
}
