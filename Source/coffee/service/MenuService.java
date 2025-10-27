package coffee.service;

import coffee.DAO.ProductDAO;
import coffee.model.SimpleProduct;
import java.util.*;

public class MenuService {
    private final ProductDAO dao = new ProductDAO();

    public MenuService() {
        dao.initTable(); // tạo bảng Menu nếu chưa có
    }

    // Hiển thị menu
    public void showMenu() {
        List<SimpleProduct> menu = dao.loadMenu();
        System.out.println("\n=== MENU HIỆN TẠI ===");
        if (menu.isEmpty()) {
            System.out.println("⚠️ Menu trống. Hãy thêm món mới!");
            return;
        }
        int i = 1;
        for (SimpleProduct p : menu) {
            System.out.printf("%d) %s - %.0f VND%n", i++, p.getName(), p.cost());
        }
    }

    // Thêm món mới
    public void addProduct(Scanner sc) {
        sc.nextLine(); // clear buffer
        System.out.println("(Nhập 'b' để quay lại)");
        System.out.print("Nhập tên món mới: ");
        String name = sc.nextLine();
        if (name.equalsIgnoreCase("b")) return;

        System.out.print("Nhập giá: ");
        String priceStr = sc.nextLine();
        if (priceStr.equalsIgnoreCase("b")) return;

        try {
            double price = Double.parseDouble(priceStr);
            dao.addProduct(new SimpleProduct(name, price));
            System.out.println("✅ Đã thêm món: " + name + " (" + price + " VND)");
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Giá không hợp lệ!");
        }
    }

    // Xóa món
    public void removeProduct(Scanner sc) {
        sc.nextLine();
        System.out.println("(Nhập 'b' để quay lại)");
        System.out.print("Nhập tên món cần xóa: ");
        String name = sc.nextLine();
        if (name.equalsIgnoreCase("b")) return;

        dao.deleteProduct(name);
        System.out.println("🗑 Đã xóa món (nếu tồn tại): " + name);
    }

    // Cập nhật giá
    public void updatePrice(Scanner sc) {
        sc.nextLine();
        System.out.println("(Nhập 'b' để quay lại)");
        System.out.print("Nhập tên món cần sửa giá: ");
        String name = sc.nextLine();
        if (name.equalsIgnoreCase("b")) return;

        System.out.print("Nhập giá mới: ");
        String newPriceStr = sc.nextLine();
        if (newPriceStr.equalsIgnoreCase("b")) return;

        try {
            double newPrice = Double.parseDouble(newPriceStr);
            dao.updatePrice(name, newPrice);
            System.out.println("💰 Đã cập nhật giá cho " + name + " -> " + newPrice + " VND");
        } catch (NumberFormatException e) {
            System.out.println("⚠️ Giá không hợp lệ!");
        }
    }

    // Menu quản lý sản phẩm
    public void manageMenu(Scanner sc) {
        int ch;
        do {
            System.out.println("\n=== QUẢN LÝ MENU (SQLite) ===");
            System.out.println("1. Xem menu");
            System.out.println("2. Thêm món mới");
            System.out.println("3. Xóa món");
            System.out.println("4. Sửa giá");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            while (!sc.hasNextInt()) {
                sc.next(); System.out.print("Nhập số hợp lệ: ");
            }
            ch = sc.nextInt();

            switch (ch) {
                case 1 -> showMenu();
                case 2 -> addProduct(sc);
                case 3 -> removeProduct(sc);
                case 4 -> updatePrice(sc);
                case 0 -> System.out.println("🔙 Quay lại menu chính...");
                default -> System.out.println("⚠️ Lựa chọn không hợp lệ!");
            }
        } while (ch != 0);
    }
}
