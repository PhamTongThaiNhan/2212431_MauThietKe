package coffee.service;

import coffee.model.*;
import coffee.strategy.*;
import coffee.DAO.*;
import java.util.*;

public class OrderService {

    private final ProductDAO productDAO = new ProductDAO();
    private final OrderDAO orderDAO = new OrderDAO();

    public OrderService() {
        orderDAO.initTable();
    }

    public void createOrder() {
        Scanner sc = new Scanner(System.in);
        List<SimpleProduct> menuList = productDAO.loadMenu();
        if (menuList.isEmpty()) {
            System.out.println("⚠️ Menu trống. Hãy thêm món trong Quản lý menu trước!");
            return;
        }

        System.out.println("=== DANH SÁCH MÓN HIỆN TẠI ===");
        int i = 1;
        for (SimpleProduct p : menuList)
            System.out.printf("%d. %s (%.0f VND)%n", i++, p.getName(), p.cost());

        System.out.println("(Nhập 'b' để quay lại)");
        System.out.print("Chọn số món: ");
        String input = sc.nextLine();
        if (input.equalsIgnoreCase("b")) return;

        int type;
        try {
            type = Integer.parseInt(input);
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Lựa chọn không hợp lệ!");
            return;
        }

        if (type < 1 || type > menuList.size()) {
            System.out.println("⚠️ Không có món này trong menu!");
            return;
        }

        Product p = menuList.get(type - 1);

        // topping
        System.out.println("Thêm topping? (1.Milk +5000  2.Caramel +7000  3.Ice +2000  0.Không)");
        String topInput = sc.nextLine();
        if (topInput.equalsIgnoreCase("b")) return;
        double toppingPrice = 0;
        switch (topInput) {
            case "1" -> { p = new MilkDecorator(p); toppingPrice = 5000; }
            case "2" -> { p = new CaramelDecorator(p); toppingPrice = 7000; }
            case "3" -> { p = new IceDecorator(p); toppingPrice = 2000; }
        }

        System.out.print("Số lượng món: ");
        String qtyStr = sc.nextLine();
        if (qtyStr.equalsIgnoreCase("b")) return;
        int qty = 1;
        try {
            qty = Math.max(1, Integer.parseInt(qtyStr));
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Số lượng không hợp lệ!");
            return;
        }

        System.out.println("Chiến lược giá: (1.Regular  2.Member  3.HappyHour  4.Combo)");
        String strategyStr = sc.nextLine();
        if (strategyStr.equalsIgnoreCase("b")) return;

        PricingStrategy strategy;
        String strategyName;
        double discountRate = 0;

        switch (strategyStr) {
            case "2" -> { strategy = new MemberPricing(); strategyName = "MemberPricing"; discountRate = 0.10; }
            case "3" -> { strategy = new HappyHourPricing(); strategyName = "HappyHourPricing"; discountRate = 0.20; }
            case "4" -> { strategy = new BulkOrderPricing(); strategyName = "ComboPricing"; discountRate = (qty >= 3 ? 0.15 : 0); }
            default -> { strategy = new RegularPricing(); strategyName = "RegularPricing"; discountRate = 0; }
        }

        double subtotal = (p.cost() + toppingPrice) * qty;
        double discount = subtotal * discountRate;
        double total = subtotal - discount;

        System.out.println("\n=== HÓA ĐƠN MỚI ===");
        System.out.println("Mặt hàng: " + p.getDescription());
        System.out.println("Giá gốc: " + p.cost() + " VND");
        if (toppingPrice > 0)
            System.out.println("Phụ phí topping: " + toppingPrice + " VND");
        System.out.println("Số lượng: " + qty);
        System.out.println("Tổng trước giảm: " + subtotal + " VND");
        System.out.println("Giảm giá (" + strategyName + "): -" + discount + " VND");
        System.out.println("Thành tiền: " + total + " VND");
        System.out.println("==========================");

        // ✅ Lưu vào SQLite
        orderDAO.insertOrder(p.getDescription(), qty, subtotal, discount, total, strategyName);
        System.out.println("💾 Đơn hàng đã được lưu vào cơ sở dữ liệu!");
    }

    // === Thống kê doanh thu từ DB ===
    public void viewRevenueStatistics() {
        orderDAO.showRevenueSummary();
    }
}
