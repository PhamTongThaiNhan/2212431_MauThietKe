package coffee.database;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLiteConnector {
    private static final String URL = "jdbc:sqlite:Database/coffeeDB.db";

    public static Connection connect() {
        try {
            Connection conn = DriverManager.getConnection(URL);
            System.out.println("✅ Kết nối SQLite thành công!");
            return conn;
        } catch (Exception e) {
            System.out.println("❌ Kết nối SQLite thất bại: " + e.getMessage());
            return null;
        }
    }
}
