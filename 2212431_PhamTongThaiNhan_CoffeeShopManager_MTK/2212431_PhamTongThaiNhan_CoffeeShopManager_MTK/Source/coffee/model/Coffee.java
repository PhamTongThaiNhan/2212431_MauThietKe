package coffee.model;

public class Coffee extends Product {
    public Coffee(String name, double basePrice) {
        super(name, basePrice);
    }

    @Override
    public double cost() { return basePrice; }

    @Override
    public String getDescription() { return name; }
}
