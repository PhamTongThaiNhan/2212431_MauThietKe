package coffee.service;

import coffee.singleton.DatabaseManager;
import coffee.singleton.UserSession;
import java.sql.*;
import java.util.Scanner;

public class AuthService {

    public boolean login(Scanner sc) {
        Connection conn = DatabaseManager.getInstance().getConnection();
        if (conn == null) {
            System.out.println("Không thể kết nối cơ sở dữ liệu!");
            return false;
        }

        while (true) {
            System.out.println("\n=== HỆ THỐNG ĐĂNG NHẬP COFFEE SHOP ===");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Đăng ký tài khoản mới");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1" -> {
                    System.out.print("Tên đăng nhập: ");
                    String username = sc.nextLine().trim();
                    System.out.print("Mật khẩu: ");
                    String password = sc.nextLine().trim();

                    try (PreparedStatement ps = conn.prepareStatement(
                            "SELECT role FROM Users WHERE username=? AND password=?")) {
                        ps.setString(1, username);
                        ps.setString(2, password);
                        ResultSet rs = ps.executeQuery();

                        if (rs.next()) {
                            String role = rs.getString("role");
                            System.out.println("Đăng nhập thành công với vai trò: " + role);
                            UserSession.getInstance().setUser(username, role);
                            return true;
                        } else {
                            System.out.println("Sai tên đăng nhập hoặc mật khẩu!");
                        }
                    } catch (SQLException e) {
                        System.out.println("Lỗi khi kiểm tra đăng nhập: " + e.getMessage());
                    }
                }

                case "2" -> register(sc, conn);
                case "0" -> {
                    return false;
                }
                default -> System.out.println("Lựa chọn không hợp lệ!");
            }
        }
    }

    private void register(Scanner sc, Connection conn) {
        System.out.print("Chọn tên đăng nhập: ");
        String username = sc.nextLine().trim();
        System.out.print("Chọn mật khẩu: ");
        String password = sc.nextLine().trim();
        System.out.print("Chọn vai trò (admin / staff): ");
        String role = sc.nextLine().trim().toLowerCase();

        try (PreparedStatement ps = conn.prepareStatement(
                "INSERT INTO Users (username, password, role) VALUES (?, ?, ?)")) {
            ps.setString(1, username);
            ps.setString(2, password);
            ps.setString(3, role);
            ps.executeUpdate();
            System.out.println("Đăng ký thành công!");
        } catch (SQLException e) {
            if (e.getMessage().contains("UNIQUE"))
                System.out.println("Tên đăng nhập đã tồn tại!");
            else
                System.out.println("Lỗi khi đăng ký: " + e.getMessage());
        }
    }
}
