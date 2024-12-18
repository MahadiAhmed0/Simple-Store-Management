import java.io.*;
import java.util.*;

public class StoreManager {
    private final String productsFile = "products.txt";
    private final String salesFile = "sales.txt";

    public void manageProducts() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Manage Products ===");
        System.out.println("1. Add Product");
        System.out.println("2. Update Product");
        System.out.println("3. Delete Product");
        System.out.print("Enter your choice: ");
        int choice = scanner.nextInt();
        scanner.nextLine();

        switch (choice) {
            case 1:
                addProduct();
                break;
            case 2:
                updateProduct(scanner);
                break;
            case 3:
                deleteProduct(scanner);
                break;
            default:
                System.out.println("Invalid choice.");
        }
    }

    public void viewDailyIncome() {
        double totalIncome = 0;
        String today = java.time.LocalDate.now().toString();

        try (BufferedReader reader = new BufferedReader(new FileReader(salesFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split("\\|");
                if (data[0].equals(today)) {
                    totalIncome += Double.parseDouble(data[4]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading sales file.");
        }

        System.out.printf("Today's income: %.2f\n", totalIncome);
    }

    public void sellProduct() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("=== Sell Product ===");
        System.out.print("Enter Product ID: ");
        int productId = scanner.nextInt();
        System.out.print("Enter Quantity: ");
        int quantity = scanner.nextInt();

        try {
            List<String> products = new ArrayList<>();
            boolean productFound = false;
            String productName = "";
            double price = 0;

            try (BufferedReader reader = new BufferedReader(new FileReader(productsFile))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    String[] data = line.split("\\|");
                    if (Integer.parseInt(data[0]) == productId) {
                        productFound = true;
                        productName = data[1];
                        price = Double.parseDouble(data[2]);
                        int availableQty = Integer.parseInt(data[3]);

                        if (quantity > availableQty) {
                            throw new InsufficientStockException("Insufficient stock available.");
                        }

                        data[3] = String.valueOf(availableQty - quantity);
                        line = String.join("|", data);
                    }
                    products.add(line);
                }
            }

            if (!productFound) {
                System.out.println("Product not found.");
                return;
            }

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(productsFile))) {
                for (String product : products) {
                    writer.write(product);
                    writer.newLine();
                }
            }

            double totalPrice = price * quantity;
            logSale(productName, productId, quantity, totalPrice);

            System.out.printf("Product sold successfully. Total Price: %.2f\n", totalPrice);

        } catch (InsufficientStockException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println("Error processing the sale.");
        }
    }

    private void logSale(String productName, int productId, int quantity, double totalPrice) {
        String today = java.time.LocalDate.now().toString();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(salesFile, true))) {
            writer.write(today + "|" + productId + "|" + productName + "|" + quantity + "|" + totalPrice);
            writer.newLine();
        } catch (IOException e) {
            System.out.println("Error logging the sale.");
        }
    }

    private void addProduct() {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter Product ID: ");
        String productId = scanner.nextLine();

        System.out.print("Enter Product Name: ");
        String productName = scanner.nextLine();

        System.out.print("Enter Product Price: ");
        double price = scanner.nextDouble();

        System.out.print("Enter Product Quantity: ");
        int quantityToAdd = scanner.nextInt();

        System.out.print("Enter Low Stock Threshold: ");
        int lowStockThreshold = scanner.nextInt();
        scanner.nextLine();

        List<String> productList = new ArrayList<>();
        boolean productExists = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(productsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] productData = line.split("\\|");
                if (productData[0].equals(productId)) {
                    productExists = true;
                    int currentQuantity = Integer.parseInt(productData[3]);
                    int newQuantity = currentQuantity + quantityToAdd;

                    String updatedProduct = productId + "|" + productData[1] + "|" + productData[2] + "|" + newQuantity + "|" + productData[4];
                    productList.add(updatedProduct);

                    System.out.println("Product already exists. Quantity updated!");
                    System.out.println("Product: " + productData[1]);
                    System.out.println("Quantity: " + newQuantity);
                } else {
                    productList.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading products file: " + e.getMessage());
            return;
        }

        if (!productExists) {
            String newProduct = productId + "|" + productName + "|" + price + "|" + quantityToAdd + "|" + lowStockThreshold;
            productList.add(newProduct);
            System.out.println("New product added successfully!");
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productsFile))) {
            for (String product : productList) {
                writer.write(product);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to products file: " + e.getMessage());
        }
    }


    private void updateProduct(Scanner scanner) {
        System.out.print("Enter Product ID to update: ");
        String productId = scanner.nextLine();

        List<String> productList = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(productsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] productData = line.split("\\|");
                if (productData[0].equals(productId)) {
                    found = true;
                    System.out.print("Enter new name (current: " + productData[1] + "): ");
                    String name = scanner.nextLine();
                    System.out.print("Enter new price (current: " + productData[2] + "): ");
                    double price = scanner.nextDouble();
                    System.out.print("Enter new quantity (current: " + productData[3] + "): ");
                    int quantity = scanner.nextInt();
                    System.out.print("Enter new low-stock threshold (current: " + productData[4] + "): ");
                    int threshold = scanner.nextInt();
                    scanner.nextLine();

                    String updatedProduct = productId + "|" + name + "|" + price + "|" + quantity + "|" + threshold;
                    productList.add(updatedProduct);
                } else {
                    productList.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading products file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Product with ID " + productId + " not found.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productsFile))) {
            for (String product : productList) {
                writer.write(product);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to products file: " + e.getMessage());
        }

        System.out.println("Product updated successfully!");
    }

    private void deleteProduct(Scanner scanner) {
        System.out.print("Enter Product ID to delete: ");
        String productId = scanner.nextLine();

        List<String> productList = new ArrayList<>();
        boolean found = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(productsFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] productData = line.split("\\|");
                if (productData[0].equals(productId)) {
                    found = true;
                    System.out.println("Deleting product: " + line);
                } else {
                    productList.add(line);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading products file: " + e.getMessage());
            return;
        }

        if (!found) {
            System.out.println("Product with ID " + productId + " not found.");
            return;
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(productsFile))) {
            for (String product : productList) {
                writer.write(product);
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Error writing to products file: " + e.getMessage());
        }

        System.out.println("Product deleted successfully!");
    }
    void showProducts() {
        System.out.println("=== Product List ===");
        try (BufferedReader reader = new BufferedReader(new FileReader(productsFile))) {
            String line;
            System.out.printf("%-5s %-15s %-10s %-10s %-10s%n", "ID", "Name", "Price", "Quantity", "LowStock");
            System.out.println("----------------------------------------------------");
            while ((line = reader.readLine()) != null) {
                String[] productData = line.split("\\|");
                System.out.printf("%-5s %-15s %-10s %-10s %-10s%n",
                        productData[0], productData[1], productData[2], productData[3], productData[4]);
            }
        } catch (IOException e) {
            System.out.println("Error reading products file: " + e.getMessage());
        }
    }
    void showSales() {
        System.out.println("=== Sales History ===");
        try (BufferedReader reader = new BufferedReader(new FileReader(salesFile))) {
            String line;
            System.out.printf("%-12s %-5s %-15s %-10s %-10s%n", "Date", "ID", "Name", "Quantity", "Total Price");
            System.out.println("----------------------------------------------------------");
            while ((line = reader.readLine()) != null) {
                String[] saleData = line.split("\\|");
                System.out.printf("%-12s %-5s %-15s %-10s %-10s%n",
                        saleData[0], saleData[1], saleData[2], saleData[3], saleData[4]);
            }
        } catch (IOException e) {
            System.out.println("Error reading sales file: " + e.getMessage());
        }
    }
    void checkLowStock() {
        System.out.println("=== Low Stock Warning ===");
        boolean hasLowStock = false;

        try (BufferedReader reader = new BufferedReader(new FileReader(productsFile))) {
            String line;
            System.out.printf("%-5s %-15s %-10s %-10s %-10s%n", "ID", "Name", "Price", "Quantity", "Threshold");
            System.out.println("----------------------------------------------------------");
            while ((line = reader.readLine()) != null) {
                String[] productData = line.split("\\|");
                int quantity = Integer.parseInt(productData[3]);
                int threshold = Integer.parseInt(productData[4]);

                if (quantity <= threshold) {
                    hasLowStock = true;
                    System.out.printf("%-5s %-15s %-10s %-10s %-10s%n",
                            productData[0], productData[1], productData[2], productData[3], productData[4]);
                }
            }
        } catch (IOException e) {
            System.out.println("Error reading products file: " + e.getMessage());
            return;
        }

        if (!hasLowStock) {
            System.out.println("All products are above their low-stock threshold.");
        }
    }



}
