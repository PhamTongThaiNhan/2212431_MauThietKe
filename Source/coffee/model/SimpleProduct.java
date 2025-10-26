package coffee.model;

public class SimpleProduct extends Product {

    public SimpleProduct(String name, double price) {
        super(name, price); 
    }

    @Override
    public String getDescription() {
        return name;
    }

    @Override
    public double cost() {
        return basePrice;
    }
}
