package coffee.singleton;

public class UserSession {
    private static UserSession instance;
    private String username;
    private String role; // "MANAGER" | "EMPLOYEE"

    private UserSession() {}

    public static UserSession getInstance() {
        if (instance == null) instance = new UserSession();
        return instance;
    }

    public void loginAs(String user, String role) {
        this.username = user;
        this.role = role;
    }

    public boolean isLoggedIn() { return username != null; }
    public String getUsername() { return username; }
    public String getRole() { return role; }
    public boolean isManager() { return "MANAGER".equalsIgnoreCase(role); }

    public void logout() { username = null; role = null; }
}
