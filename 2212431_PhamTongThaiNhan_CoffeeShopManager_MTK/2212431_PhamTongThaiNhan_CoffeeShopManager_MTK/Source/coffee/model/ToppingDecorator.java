package coffee.model;

public abstract class ToppingDecorator extends Product {
    protected Product product;
    public ToppingDecorator(Product product) {
        super(product.getName(), product.getBasePrice());
        this.product = product;
    }
}
