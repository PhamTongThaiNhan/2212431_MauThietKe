package coffee.service;

import coffee.model.*;
import coffee.strategy.*;
import coffee.singleton.*;
import java.util.*;                     // Map, LinkedHashMap, Scanner

public class OrderService {

    public void createOrder() {
        Scanner sc = new Scanner(System.in);
        Product p;

        System.out.println("Chọn sản phẩm: 1.Coffee  2.Tea  3.Snack");
        int type = safeInt(sc);
        if (type == 1) p = new Coffee("Coffee", 25000);
        else if (type == 2) p = new Tea("Tea", 30000);
        else p = new Snack("Cookie", 15000);

        System.out.println("Thêm topping? (1.Milk 2.Caramel 3.Ice 0.Không)");
        int top = safeInt(sc);
        if (top == 1) p = new MilkDecorator(p);
        else if (top == 2) p = new CaramelDecorator(p);
        else if (top == 3) p = new IceDecorator(p);

        System.out.println("Số lượng: ");
        int qty = Math.max(1, safeInt(sc));

        System.out.println("Chiến lược giá: 1.Regular 2.Member 3.HappyHour 4.Bulk");
        int ch = safeInt(sc);
        PricingStrategy strategy;
        String strategyName;
        switch (ch) {
            case 2: strategy = new MemberPricing(); strategyName = "MemberPricing"; break;
            case 3: strategy = new HappyHourPricing(); strategyName = "HappyHourPricing"; break;
            case 4: strategy = new BulkOrderPricing(); strategyName = "BulkOrderPricing"; break;
            default: strategy = new RegularPricing(); strategyName = "RegularPricing";
        }

        double total = strategy.getPrice(p.cost(), qty);
        int orderId = OrderManager.getInstance().nextOrderId();

        System.out.println("=== HÓA ĐƠN #" + orderId + " ===");
        System.out.println("Mặt hàng: " + p.getDescription());
        System.out.println("Số lượng: " + qty);
        System.out.println("Tổng tiền: " + total + " VND");
        System.out.println("==========================");

        // Lưu lịch sử
        String line = String.format(
            "Order #%d | %s | Qty: %d | Total: %.2f | Strategy: %s",
            orderId, p.getDescription(), qty, total, strategyName
        );
        DatabaseManager.getInstance().appendOrder(line);
    }

    public void viewHistory() {
        System.out.println("=== LỊCH SỬ ĐƠN HÀNG ===");
        for (String line : DatabaseManager.getInstance().readOrders()) {
            System.out.println(line);
        }
        System.out.println("========================");
    }

    // === THỐNG KÊ DOANH THU (khớp Main.java) ===
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

            // Lấy Total: (dựa chuỗi 'Total: xxx |')
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

            // Gom theo Strategy:
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

    // === XUẤT HÓA ĐƠN GẦN NHẤT (khớp Main.java) ===
    public void exportLatestInvoice() {
        List<String> lines = DatabaseManager.getInstance().readOrders();
        if (lines.isEmpty()) {
            System.out.println("Chưa có đơn để xuất.");
            return;
        }
        String last = lines.get(lines.size() - 1);

        // Lấy id đơn từ "Order #<id> | ..."
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
