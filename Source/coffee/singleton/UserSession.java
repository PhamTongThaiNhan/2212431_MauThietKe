package coffee.singleton;

public class UserSession {
    private static UserSession instance;
    private String username;
    private String role;

    // private constructor để đảm bảo Singleton
    private UserSession() {}

    // Lấy instance duy nhất
    public static UserSession getInstance() {
        if (instance == null) {
            instance = new UserSession();
        }
        return instance;
    }

    // Thiết lập thông tin người dùng
    public void setUser(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Lấy tên người dùng hiện tại
    public String getUsername() {
        return username;
    }

    // Lấy vai trò hiện tại
    public String getRole() {
        return role;
    }

    // Kiểm tra có phải quản lý không
    public boolean isManager() {
        return role != null && role.equalsIgnoreCase("admin");
    }

    // Xóa thông tin đăng nhập (nếu cần logout)
    public void clear() {
        username = null;
        role = null;
    }
}
