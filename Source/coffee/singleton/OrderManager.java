package coffee.singleton;

public class OrderManager {
    private static OrderManager instance;
    private int orderCount = 0;

    private OrderManager() {}

    public static OrderManager getInstance() {
        if (instance == null) instance = new OrderManager();
        return instance;
    }

    public int nextOrderId() {
        return ++orderCount;
    }
}
