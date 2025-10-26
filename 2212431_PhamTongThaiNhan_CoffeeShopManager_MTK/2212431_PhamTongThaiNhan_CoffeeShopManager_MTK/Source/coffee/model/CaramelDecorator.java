package coffee.model;

public class CaramelDecorator extends ToppingDecorator {
    public CaramelDecorator(Product product) { super(product); }
    @Override
    public String getDescription() { return product.getDescription() + ", Caramel"; }
    @Override
    public double cost() { return product.cost() + 7000; }
}
