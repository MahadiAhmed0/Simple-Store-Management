import java.util.Scanner;

public class StoreManagement {
    public static void main(String[] args) {
        StoreManager manager = new StoreManager();
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("=== Store Management System ===");
            System.out.println("1. Manage Products");
            System.out.println("2. Check Daily Income");
            System.out.println("3. Sell Products");
            System.out.println("4. Show Products");
            System.out.println("5. Show Sales");
            System.out.println("6. Low Stocks");
            System.out.println("7. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    manager.manageProducts();
                    break;
                case 2:
                    manager.viewDailyIncome();
                    break;
                case 3:
                    manager.sellProduct();
                    break;
                case 4:
                    manager.showProducts();
                    break;
                case 5:
                    manager.showSales();
                    break;
                case 6:
                    manager.checkLowStock();
                    break;
                case 7:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }
}
