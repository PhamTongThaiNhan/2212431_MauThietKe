package coffee.model;

public class MilkDecorator extends ToppingDecorator {
    public MilkDecorator(Product product) { super(product); }
    @Override
    public String getDescription() { return product.getDescription() + ", Milk"; }
    @Override
    public double cost() { return product.cost() + 5000; }
}
