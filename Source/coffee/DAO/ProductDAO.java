package coffee.DAO;

import java.io.*;
import java.util.*;
import coffee.model.Product;
import coffee.model.SimpleProduct; 

public class ProductDAO {
    private static final String FILE_PATH = "menu.txt";

    // Đọc danh sách món từ file
    public List<Product> loadMenu() {
        List<Product> menu = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 2) {
                    String name = parts[0].trim();
                    double price = Double.parseDouble(parts[1].trim());
                    // ✅ dùng SimpleProduct thay vì Product
                    menu.add(new SimpleProduct(name, price));
                }
            }
        } catch (IOException e) {
            System.out.println("Không thể đọc menu.txt: " + e.getMessage());
        }
        return menu;
    }

    // Ghi danh sách món ra file
    public void saveMenu(List<Product> menu) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Product p : menu) {
                pw.println(p.getName() + "," + p.cost());
            }
        } catch (IOException e) {
            System.out.println("Lưu menu thất bại: " + e.getMessage());
        }
    }
}
