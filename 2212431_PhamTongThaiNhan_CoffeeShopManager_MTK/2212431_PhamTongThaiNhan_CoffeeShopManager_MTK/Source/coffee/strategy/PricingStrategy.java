package coffee.strategy;

public interface PricingStrategy {
    double getPrice(double basePrice, int quantity);
}
