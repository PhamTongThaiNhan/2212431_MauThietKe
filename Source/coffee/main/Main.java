package coffee.main;

import coffee.service.*;
import coffee.singleton.UserSession;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        MenuService menuService = new MenuService();
        OrderService orderService = new OrderService();
        AuthService auth = new AuthService();
        Scanner sc = new Scanner(System.in);

        // Đăng nhập
        if (!auth.login(sc)) {
            System.out.println("Thoát chương trình.");
            return;
        }

        boolean isManager = UserSession.getInstance().isManager();
        int choice;

        do {
            System.out.println("\n=== COFFEE SHOP MANAGER ===");
            System.out.println("1. Xem menu");
            System.out.println("2. Tạo đơn hàng mới");
            System.out.println("3. Xem lịch sử đơn hàng");
            if (isManager) {
                System.out.println("4. Thống kê doanh thu (từ orders.txt)  [Quản lý]");
                System.out.println("5. Xuất hóa đơn gần nhất ra file       [Quản lý]");
                System.out.println("6. Quản lý menu (thêm / sửa / xóa món) [Quản lý]");
            }
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");

            while (!sc.hasNextInt()) {
                sc.next();
                System.out.print("Chọn lại: ");
            }
            choice = sc.nextInt();

            switch (choice) {
                case 1 -> menuService.showMenu();
                case 2 -> orderService.createOrder();
                case 3 -> orderService.viewHistory();
                case 4 -> {
                    if (isManager) orderService.viewRevenueStatistics();
                    else System.out.println("❌ Bạn không có quyền truy cập.");
                }
                case 5 -> {
                    if (isManager) orderService.exportLatestInvoice();
                    else System.out.println("❌ Bạn không có quyền truy cập.");
                }
                case 6 -> {
                    if (isManager) menuService.manageMenu(sc);
                    else System.out.println("❌ Chức năng chỉ dành cho quản lý.");
                }
                case 0 -> System.out.println("👋 Tạm biệt, hẹn gặp lại!");
                default -> System.out.println("⚠️ Lựa chọn không hợp lệ!");
            }

        } while (choice != 0);
    }
}
