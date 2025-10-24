package coffee.service;

import coffee.singleton.UserSession;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

public class AuthService {
    private final Path usersFile = Paths.get("Database", "users.txt");

    public AuthService() { ensureUsersFile(); }

    public boolean login(Scanner sc) {
        System.out.println("=== ĐĂNG NHẬP ===");
        System.out.print("Tài khoản: ");
        String u = sc.next();
        System.out.print("Mật khẩu: ");
        String p = sc.next();

        String role = check(u, p);
        if (role == null) {
            System.out.println("Sai tài khoản hoặc mật khẩu!");
            return false;
        }
        UserSession.getInstance().loginAs(u, role);
        System.out.println("Đăng nhập thành công. Vai trò: " + role);
        return true;
    }

    private void ensureUsersFile() {
        try {
            Files.createDirectories(usersFile.getParent());
            if (!Files.exists(usersFile)) {
                // seed mặc định
                List<String> seed = Arrays.asList(
                    "admin:admin123:MANAGER",
                    "staff:staff123:EMPLOYEE"
                );
                Files.write(usersFile, seed, StandardCharsets.UTF_8, StandardOpenOption.CREATE_NEW);
            }
        } catch (IOException e) {
            System.err.println("Không thể khởi tạo users.txt: " + e.getMessage());
        }
    }

    private String check(String user, String pass) {
        try {
            for (String line : Files.readAllLines(usersFile, StandardCharsets.UTF_8)) {
                if (line == null || line.isBlank() || line.trim().startsWith("#")) continue;
                String[] parts = line.split(":");
                if (parts.length >= 3) {
                    String u = parts[0].trim();
                    String p = parts[1].trim();
                    String r = parts[2].trim().toUpperCase();
                    if (u.equals(user) && p.equals(pass)) return r;
                }
            }
        } catch (IOException ignored) {}
        return null;
    }
}
