package coffee.service;

import coffee.model.*;
import coffee.strategy.*;
import coffee.singleton.*;
import java.util.*;
import coffee.DAO.ProductDAO;

public class OrderService {

    private ProductDAO dao = new ProductDAO();

    public void createOrder() {
        Scanner sc = new Scanner(System.in);
        List<Product> menuList = dao.loadMenu();
        if (menuList.isEmpty()) {
            System.out.println("⚠️  Menu trống. Hãy thêm món trong Quản lý menu trước!");
            return;
        }

        System.out.println("=== DANH SÁCH MÓN HIỆN TẠI ===");
        int i = 1;
        for (Product p : menuList)
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
        System.out.println("Thêm topping? (1.Milk +5000  2.Caramel +7000  3.Ice +2000  0.Không");
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

        System.out.println("Chiến lược giá: (1.Regular  2.Member  3.HappyHour  4.Combo");
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
        int orderId = OrderManager.getInstance().nextOrderId();

        System.out.println("\n=== HÓA ĐƠN #" + orderId + " ===");
        System.out.println("Mặt hàng: " + p.getDescription());
        System.out.println("Giá gốc: " + p.cost() + " VND");
        if (toppingPrice > 0)
            System.out.println("Phụ phí topping: " + toppingPrice + " VND");
        System.out.println("Số lượng: " + qty);
        System.out.println("Tổng trước giảm: " + subtotal + " VND");
        System.out.println("Giảm giá (" + strategyName + "): -" + discount + " VND");
        System.out.println("Thành tiền: " + total + " VND");
        System.out.println("==========================");

        String line = String.format(
            "Order #%d | %s | Qty: %d | Subtotal: %.2f | Discount: %.2f | Total: %.2f | Strategy: %s",
            orderId, p.getDescription(), qty, subtotal, discount, total, strategyName
        );
        DatabaseManager.getInstance().appendOrder(line);
    }

    // === Lịch sử đơn hàng ===
    public void viewHistory() {
        System.out.println("=== LỊCH SỬ ĐƠN HÀNG ===");
        for (String line : DatabaseManager.getInstance().readOrders()) {
            System.out.println(line);
        }
        System.out.println("========================");
    }

    // === Thống kê doanh thu ===
    public void viewRevenueStatistics() {
        System.out.println("=== THỐNG KÊ DOANH THU ===");
        List<String> lines = DatabaseManager.getInstance().readOrders();
        if (lines.isEmpty()) {
            System.out.println("Chưa có đơn hàng nào.");
            System.out.println("=========================");
            return;
        }

        double sum = 0;
        int count = 0;
        Map<String, Double> byStrategy = new LinkedHashMap<>();

        for (String line : lines) {
            if (line == null || line.trim().isEmpty()) continue;
            count++;

            double total = 0;
            try {
                int i = line.indexOf("Total:");
                int j = (i >= 0) ? line.indexOf("|", i + 6) : -1;
                String totalStr = (i >= 0)
                        ? line.substring(i + 6, j > 0 ? j : line.length()).trim()
                        : "0";
                total = Double.parseDouble(totalStr);
            } catch (Exception ignored) {}
            sum += total;

            String strategy = "Unknown";
            int k = line.indexOf("Strategy:");
            if (k >= 0) strategy = line.substring(k + 9).trim();
            byStrategy.put(strategy, byStrategy.getOrDefault(strategy, 0.0) + total);
        }

        System.out.printf("Số đơn: %d%nTổng doanh thu: %.2f VND%n", count, sum);
        System.out.println("-- Doanh thu theo chiến lược --");
        for (var e : byStrategy.entrySet()) {
            System.out.printf("%s: %.2f VND%n", e.getKey(), e.getValue());
        }
        System.out.println("==============================");
    }

    // === Xuất hóa đơn gần nhất ===
    public void exportLatestInvoice() {
        List<String> lines = DatabaseManager.getInstance().readOrders();
        if (lines.isEmpty()) {
            System.out.println("Chưa có đơn để xuất.");
            return;
        }
        String last = lines.get(lines.size() - 1);

        String id = "unknown";
        int h = last.indexOf('#');
        int p = last.indexOf('|');
        if (h >= 0 && p > h) id = last.substring(h + 1, p).trim();

        java.nio.file.Path dir = java.nio.file.Paths.get("Doc");
        java.nio.file.Path out = dir.resolve("receipt_" + id + ".txt");
        try {
            java.nio.file.Files.createDirectories(dir);
            java.nio.file.Files.writeString(
                out,
                "=== COFFEE SHOP RECEIPT ===\n" +
                last + "\n" +
                "Generated at: " + java.time.LocalDateTime.now() + "\n",
                java.nio.charset.StandardCharsets.UTF_8
            );
            System.out.println("Đã xuất hóa đơn: " + out.toString());
        } catch (Exception e) {
            System.out.println("Xuất hóa đơn thất bại: " + e.getMessage());
        }
    }

    // Helper đọc số an toàn
    private int safeInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            sc.next();
            System.out.print("Nhập số hợp lệ: ");
        }
        return sc.nextInt();
    }
}
