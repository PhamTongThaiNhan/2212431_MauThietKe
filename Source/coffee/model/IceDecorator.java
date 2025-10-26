package coffee.model;

public class IceDecorator extends ToppingDecorator {
    public IceDecorator(Product product) { super(product); }
    @Override
    public String getDescription() { return product.getDescription() + ", Ice"; }
    @Override
    public double cost() { return product.cost() + 2000; }
}
