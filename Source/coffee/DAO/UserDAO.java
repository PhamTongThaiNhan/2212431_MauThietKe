package coffee.DAO;

import java.sql.*;

public class UserDAO {
    private static final String DB_URL = "jdbc:sqlite:coffeeDB.db";

    public UserDAO() {
        createTableIfNotExists();
    }

    private void createTableIfNotExists() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Users (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                username TEXT UNIQUE NOT NULL,
                password TEXT NOT NULL,
                role TEXT NOT NULL
            )
        """;
        try (Connection conn = DriverManager.getConnection(DB_URL);
             Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        } catch (SQLException e) {
            System.out.println("⚠️ Không thể tạo bảng Users: " + e.getMessage());
        }
    }

    public boolean register(String username, String password, String role) {
        String sql = "INSERT INTO Users(username, password, role) VALUES(?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, role);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("⚠️ Lỗi khi đăng ký: " + e.getMessage());
            return false;
        }
    }

    public String login(String username, String password) {
        String sql = "SELECT role FROM Users WHERE username=? AND password=?";
        try (Connection conn = DriverManager.getConnection(DB_URL);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getString("role");
            }
        } catch (SQLException e) {
            System.out.println("⚠️ Lỗi khi đăng nhập: " + e.getMessage());
        }
        return null;
    }
}
