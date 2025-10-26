package coffee.model;

public abstract class Product {
    protected String name;
    protected double basePrice;

    public Product(String name, double basePrice) {
        this.name = name;
        this.basePrice = basePrice;
    }

    public String getName() { return name; }
    public double getBasePrice() { return basePrice; }
    public void setBasePrice(double basePrice) {
        this.basePrice = basePrice;
    }

    public abstract double cost();
    public abstract String getDescription();
}
