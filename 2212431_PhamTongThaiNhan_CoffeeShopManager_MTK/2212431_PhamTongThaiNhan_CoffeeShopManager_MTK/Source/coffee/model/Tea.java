package coffee.model;

public class Tea extends Product {
    public Tea(String name, double basePrice) {
        super(name, basePrice);
    }

    @Override
    public double cost() { return basePrice; }

    @Override
    public String getDescription() { return name; }
}
