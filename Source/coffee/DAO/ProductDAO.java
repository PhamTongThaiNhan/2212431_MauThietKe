package coffee.DAO;

import coffee.model.SimpleProduct;
import coffee.database.SQLiteConnector;
import java.sql.*;
import java.util.*;

public class ProductDAO {

    public void initTable() {
        try (Connection conn = SQLiteConnector.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Menu (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT NOT NULL,
                    price REAL NOT NULL
                )
            """);
        } catch (Exception e) {
            System.out.println("❌ Không thể tạo bảng Menu: " + e.getMessage());
        }
    }

    public List<SimpleProduct> loadMenu() {
        List<SimpleProduct> list = new ArrayList<>();
        try (Connection conn = SQLiteConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM Menu")) {

            while (rs.next()) {
                list.add(new SimpleProduct(rs.getString("name"), rs.getDouble("price")));
            }

        } catch (Exception e) {
            System.out.println("❌ Lỗi load menu: " + e.getMessage());
        }
        return list;
    }

    public void addProduct(SimpleProduct p) {
        try (Connection conn = SQLiteConnector.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "INSERT INTO Menu(name, price) VALUES (?, ?)")) {
            ps.setString(1, p.getName());
            ps.setDouble(2, p.cost());
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("❌ Lỗi thêm món: " + e.getMessage());
        }
    }

    public void updatePrice(String name, double newPrice) {
        try (Connection conn = SQLiteConnector.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "UPDATE Menu SET price=? WHERE name=?")) {
            ps.setDouble(1, newPrice);
            ps.setString(2, name);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("❌ Lỗi cập nhật giá: " + e.getMessage());
        }
    }

    public void deleteProduct(String name) {
        try (Connection conn = SQLiteConnector.connect();
             PreparedStatement ps = conn.prepareStatement(
                     "DELETE FROM Menu WHERE name=?")) {
            ps.setString(1, name);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("❌ Lỗi xóa món: " + e.getMessage());
        }
    }
}
