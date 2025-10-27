package coffee.singleton;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection conn;

    private DatabaseManager() {
        connectSQLite();
        createTables();
        seedDefaultMenu();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    // ===== 1️⃣ Kết nối SQLite =====
    private void connectSQLite() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection("jdbc:sqlite:Database/coffeeDB.db");
            System.out.println("✅ Kết nối SQLite thành công!");
        } catch (Exception e) {
            System.out.println("❌ Kết nối SQLite thất bại: " + e.getMessage());
        }
    }

    // ===== 2️⃣ Tạo bảng nếu chưa có =====
    private void createTables() {
        try (Statement st = conn.createStatement()) {
            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT UNIQUE,
                    password TEXT,
                    role TEXT
                );
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Menu (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT UNIQUE,
                    price REAL
                );
            """);

            st.executeUpdate("""
                CREATE TABLE IF NOT EXISTS Orders (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    description TEXT,
                    quantity INTEGER,
                    subtotal REAL,
                    discount REAL,
                    total REAL,
                    strategy TEXT,
                    created_at TEXT DEFAULT CURRENT_TIMESTAMP
                );
            """);
        } catch (SQLException e) {
            System.out.println("⚠️ Lỗi khi tạo bảng: " + e.getMessage());
        }
    }

    // ===== 3️⃣ Seed dữ liệu mẫu cho Menu =====
    private void seedDefaultMenu() {
    try (Statement st = conn.createStatement()) {
        // Seed menu nếu trống
        ResultSet rs = st.executeQuery("SELECT COUNT(*) FROM Menu");
        if (rs.next() && rs.getInt(1) == 0) {
            System.out.println("📋 Đang thêm dữ liệu mẫu vào Menu...");
            String sql = """
                INSERT INTO Menu (name, price) VALUES
                ('Espresso', 25000),
                ('Cappuccino', 35000),
                ('Latte', 40000),
                ('Tea', 30000),
                ('Cookie', 15000),
                ('Milk Tea', 32000);
            """;
            st.executeUpdate(sql);
        }

        // Seed người dùng mặc định (không ghi đè nếu đã có)
        System.out.println("🔐 Đang thêm user mặc định (nếu chưa có)...");
        st.executeUpdate("INSERT OR IGNORE INTO Users (username, password, role) VALUES ('admin', 'admin123', 'admin');");
        st.executeUpdate("INSERT OR IGNORE INTO Users (username, password, role) VALUES ('staff', 'staff123', 'staff');");

    } catch (SQLException e) {
        System.out.println("⚠️ Lỗi khi thêm dữ liệu mẫu: " + e.getMessage());
    }
    }

    // ===== 5️⃣ Đọc lịch sử đơn hàng =====
    public List<String> readOrders() {
        List<String> list = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery("SELECT description FROM Orders ORDER BY id")) {
            while (rs.next()) list.add(rs.getString("description"));
        } catch (SQLException e) {
            System.out.println("⚠️ Lỗi khi đọc đơn hàng: " + e.getMessage());
        }
        return list;
    }

    // ===== 6️⃣ Cho phép các class khác truy cập Connection =====
    public Connection getConnection() {
        return conn;
    }
}
