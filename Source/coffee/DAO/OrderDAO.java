package coffee.DAO;

import coffee.database.SQLiteConnector;
import java.sql.*;

public class OrderDAO {

    public void initTable() {
        try (Connection conn = SQLiteConnector.connect();
             Statement stmt = conn.createStatement()) {
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS Orders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    product_name TEXT,
                    quantity INTEGER,
                    subtotal REAL,
                    discount REAL,
                    total REAL,
                    strategy TEXT,
                    created_at DATETIME DEFAULT CURRENT_TIMESTAMP
                )
            """);
        } catch (Exception e) {
            System.out.println("❌ Không thể tạo bảng Orders: " + e.getMessage());
        }
    }

    public void insertOrder(String productName, int qty, double subtotal,
                            double discount, double total, String strategy) {
        try (Connection conn = SQLiteConnector.connect();
             PreparedStatement ps = conn.prepareStatement("""
                INSERT INTO Orders(product_name, quantity, subtotal, discount, total, strategy)
                VALUES (?, ?, ?, ?, ?, ?)
            """)) {
            ps.setString(1, productName);
            ps.setInt(2, qty);
            ps.setDouble(3, subtotal);
            ps.setDouble(4, discount);
            ps.setDouble(5, total);
            ps.setString(6, strategy);
            ps.executeUpdate();
        } catch (Exception e) {
            System.out.println("❌ Lỗi lưu đơn hàng: " + e.getMessage());
        }
    }

    public void showRevenueSummary() {
        try (Connection conn = SQLiteConnector.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("""
                SELECT strategy, COUNT(*) as count, SUM(total) as total
                FROM Orders GROUP BY strategy
            """)) {
            System.out.println("=== THỐNG KÊ DOANH THU ===");
            while (rs.next()) {
                System.out.printf("%s: %d đơn - %.0f VND%n",
                        rs.getString("strategy"),
                        rs.getInt("count"),
                        rs.getDouble("total"));
            }
        } catch (Exception e) {
            System.out.println("❌ Lỗi thống kê: " + e.getMessage());
        }
    }
}
