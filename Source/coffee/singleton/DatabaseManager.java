package coffee.singleton;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class DatabaseManager {
    private static DatabaseManager instance;
    private final Path ordersFile;

    private DatabaseManager() {
        this.ordersFile = Paths.get("Database", "orders.txt");
        try {
            Files.createDirectories(ordersFile.getParent());
            if (!Files.exists(ordersFile)) {
                Files.createFile(ordersFile);
            }
        } catch (IOException e) {
            System.err.println("Cannot init orders file: " + e.getMessage());
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) instance = new DatabaseManager();
        return instance;
    }

    public void appendOrder(String line) {
        try (BufferedWriter bw = Files.newBufferedWriter(ordersFile, 
                java.nio.charset.StandardCharsets.UTF_8, 
                StandardOpenOption.APPEND)) {
            bw.write(line);
            bw.newLine();
        } catch (IOException e) {
            System.err.println("Write order failed: " + e.getMessage());
        }
    }

    public List<String> readOrders() {
        try {
            return Files.readAllLines(ordersFile, java.nio.charset.StandardCharsets.UTF_8);
        } catch (IOException e) {
            System.err.println("Read orders failed: " + e.getMessage());
            return Collections.emptyList();
        }
    }
}
