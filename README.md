# Store Management System

A simple console-based Store Management System implemented in Java, using a file system for data storage. This project allows efficient management of store products and daily sales through an intuitive terminal interface.

## Features

- **Manage Products**  
  Add new products or update existing ones. Delete products from the inventory.
  
- **Track Daily Income**  
  View total income from sales for the current day.

- **Sell Products**  
  Record customer purchases and update product quantities. Handle exceptions for insufficient stock.

- **Show Products**  
  Display the list of all available products with their details.

- **Show Sales History**  
  View a history of all sales transactions.

- **Low Stock Warnings**  
  Alerts for products running below their stock threshold.

## Technical Details

- **Language**: Java
- **Data Storage**: Text files (`products.txt`, `sales.txt`)
- **Interface**: Console-based UI with minimal options.

## Usage

1. Run the program using a Java IDE or the terminal.
2. Navigate through the menu options to manage the store effectively.
3. Ensure `products.txt` and `sales.txt` exist in the working directory.

## File Formats

- **products.txt**  
  Each line represents a product in the following format:  
  `ProductID|Name|Price|Quantity|LowStockThreshold`

- **sales.txt**  
  Each line represents a sale in the following format:  
  `Date|ProductID|Name|Quantity|TotalPrice`

## Future Enhancements

- Add user authentication for admin and sales personnel.
- Implement more sophisticated error handling.
- Extend the system with GUI for better usability.
