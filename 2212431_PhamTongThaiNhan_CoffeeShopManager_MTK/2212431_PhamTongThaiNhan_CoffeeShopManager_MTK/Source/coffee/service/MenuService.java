package coffee.service;

import java.util.Arrays;
import java.util.List;

public class MenuService {
    public List<String> getMenu() {
        return Arrays.asList(
            "1) Espresso - 25000 VND",
            "2) Cappuccino - 35000 VND",
            "3) Tea - 30000 VND",
            "4) Cookie - 15000 VND"
        );
    }

    public void showMenu() {
        System.out.println("=== MENU ===");
        for (String line : getMenu()) {
            System.out.println(line);
        }
    }
}
