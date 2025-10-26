package coffee.model;

public class Snack extends Product {
    public Snack(String name, double basePrice) {
        super(name, basePrice);
    }

    @Override
    public double cost() { return basePrice; }

    @Override
    public String getDescription() { return name; }
}
