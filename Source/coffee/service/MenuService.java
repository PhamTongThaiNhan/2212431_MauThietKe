package coffee.service;

import coffee.model.*;
import coffee.DAO.ProductDAO;
import java.util.*;
import coffee.model.SimpleProduct;

public class MenuService {
    private List<Product> menu;
    private ProductDAO dao = new ProductDAO();

    public MenuService() {
        menu = dao.loadMenu();
    }

    // Hiển thị menu hiện tại
    public void showMenu() {
        System.out.println("\n=== MENU HIỆN TẠI ===");
        if (menu.isEmpty()) {
            System.out.println("⚠️  Menu trống. Hãy thêm món mới!");
            return;
        }
        int i = 1;
        for (Product p : menu) {
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
            menu.add(new SimpleProduct(name, price));
            dao.saveMenu(menu);
            System.out.println("✅ Đã thêm món: " + name + " (" + price + " VND)");
        } catch (NumberFormatException e) {
            System.out.println("⚠️  Giá không hợp lệ!");
        }
    }

    // Xóa món theo tên
    public void removeProduct(Scanner sc) {
        sc.nextLine();
        System.out.println("(Nhập 'b' để quay lại)");
        System.out.print("Nhập tên món cần xóa: ");
        String name = sc.nextLine();
        if (name.equalsIgnoreCase("b")) return;

        boolean removed = menu.removeIf(p -> p.getName().equalsIgnoreCase(name));
        if (removed) {
            dao.saveMenu(menu);
            System.out.println("🗑 Đã xóa món: " + name);
        } else {
            System.out.println("⚠️ Không tìm thấy món " + name);
        }
    }

    // Sửa giá món
    public void updatePrice(Scanner sc) {
        sc.nextLine();
        System.out.println("(Nhập 'b' để quay lại)");
        System.out.print("Nhập tên món cần sửa giá: ");
        String name = sc.nextLine();
        if (name.equalsIgnoreCase("b")) return;

        boolean found = false;
        for (Product p : menu) {
            if (p.getName().equalsIgnoreCase(name)) {
                System.out.print("Nhập giá mới: ");
                String newPriceStr = sc.nextLine();
                if (newPriceStr.equalsIgnoreCase("b")) return;

                try {
                    double newPrice = Double.parseDouble(newPriceStr);
                    if (p instanceof SimpleProduct sp) {
                        sp.setBasePrice(newPrice);
                    }
                    found = true;
                    break;
                } catch (NumberFormatException e) {
                    System.out.println("⚠️  Giá không hợp lệ!");
                    return;
                }
            }
        }

        if (found) {
            dao.saveMenu(menu);
            System.out.println("💰 Đã cập nhật giá thành công!");
        } else {
            System.out.println("⚠️ Không tìm thấy món " + name);
        }
    }

    // Menu quản lý sản phẩm
    public void manageMenu(Scanner sc) {
        int ch = -1;
        do {
            System.out.println("\n=== QUẢN LÝ MENU ===");
            System.out.println("1. Xem menu");
            System.out.println("2. Thêm món mới");
            System.out.println("3. Xóa món");
            System.out.println("4. Sửa giá");
            System.out.println("0. Quay lại");
            System.out.print("Chọn: ");
            String input = sc.next();
            if (input.equalsIgnoreCase("b")) ch = 0;
            else {
                try {
                    ch = Integer.parseInt(input);
                } catch (NumberFormatException e) {
                    System.out.println("⚠️ Lựa chọn không hợp lệ!");
                    continue;
                }
            }

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
