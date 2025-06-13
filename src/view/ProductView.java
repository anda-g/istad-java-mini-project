package view;

import controller.OrderController;
import controller.ProductController;
import controller.UserController;
import model.dto.*;
import model.entites.Category;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ProductView {
    private final Scanner sc = new Scanner(System.in);
    private final ProductController productController = new ProductController();
    private final UserController userController = new UserController();
    private final List<ProductResponseDto> productCart = new ArrayList<>();
    private final OrderController orderController = new OrderController();
    private final TableUI<ProductResponseDto> productTable = new TableUI<>();
    private final TableUI<OrderResponseDto> orderTable = new TableUI<>();
    private final TableUI<UserResponseDto> userTable = new TableUI<>();

    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";
    private static final String BLUE = "\033[0;34m";
    private static final String PURPLE = "\033[0;35m";
    private static final String CYAN = "\033[0;36m";
    private static final String BOLD = "\033[1m";
    private static final String UNDERLINE = "\033[4m";

    public void userMode(UserResponseDto user) {
        System.out.println(CYAN + BOLD + "\n=== WELCOME TO PRODUCT MANAGEMENT SYSTEM ===" + RESET);
        System.out.println(YELLOW + "Logged in as: " + user.username() + " (" + user.role() + ")" + RESET);

        do {
            System.out.println("\n" + PURPLE + BOLD + "MAIN MENU" + RESET);
            switch (userMenu()) {
                case 1 -> {
                    System.out.println("\n" + BLUE + BOLD + "ALL PRODUCTS" + RESET);
                    productTable.getTableDisplay(productController.getAllProducts());
                }
                case 2 -> {
                    System.out.println("\n" + BLUE + BOLD + "SEARCH PRODUCT" + RESET);
                    System.out.print("Enter product uuid: ");
                    String uuid = sc.nextLine();
                    ProductResponseDto product = productController.getProductByUuid(uuid);
                    if (product != null) {
                        productTable.getTableDisplay(List.of(product));
                    } else {
                        System.err.println(RED + "✖ Error: Can't find product with uuid " + uuid + RESET);
                    }
                }
                case 3 -> {
                    System.out.println("\n" + BLUE + BOLD + "ADD TO CART" + RESET);
                    String uuid;
                    do {
                        System.out.print("Enter product uuid (or 'exit' to finish): ");
                        uuid = sc.nextLine();
                        if (uuid.equalsIgnoreCase("exit")) {
                            break;
                        }
                        ProductResponseDto product = productController.getProductByUuid(uuid);
                        if (product == null) {
                            System.err.println(RED + "✖ Error: Product not found with uuid " + uuid + RESET);
                        } else {
                            System.out.print("Enter product quantity: ");
                            int qty = sc.nextInt();
                            sc.nextLine();
                            productCart.add(new ProductResponseDto(
                                    product.uuid(),
                                    product.name(),
                                    product.price(),
                                    qty,
                                    product.category()
                            ));
                            System.out.println(GREEN + "✔ Product added to cart successfully!" + RESET);
                        }
                    } while (!uuid.equalsIgnoreCase("exit"));
                }
                case 4 -> {
                    if (productCart.isEmpty()) {
                        System.err.println(RED + "✖ Your cart is empty" + RESET);
                    } else {
                        System.out.println("\n" + BLUE + BOLD + "YOUR ORDER SUMMARY" + RESET);
                        productOrderDisplay(productCart);
                        System.out.print(BOLD + BOLD + "You want to order (y/n): ");
                        String c = sc.next().toLowerCase();
                        if (c.charAt(0) == 'y') {
                            List<String> productUuids = new ArrayList<>();
                            for (ProductResponseDto responseDto : productCart) {
                                productUuids.add(responseDto.uuid());
                            }
                            OrderCreateDto orderCreateDto = new OrderCreateDto(user.uuid(), productUuids, LocalDate.now());
                            orderController.saveOrder(orderCreateDto);
                            System.out.println(GREEN + "Order created successfully!" + RESET);
                            System.out.println(GREEN + BOLD + "Wait for your meals!!" + RESET);
                            productCart.clear();
                        }
                    }
                }
                case 5 -> {
                    System.out.println("\n" + BLUE + BOLD + "YOUR ORDER HISTORY" + RESET);
                    List<OrderResponseDto> orders = orderController.getOrdersByCustomer(user.uuid());
                    if (orders.isEmpty()) {
                        System.err.println(RED + "✖ There is no order history" + RESET);
                    }else{
                        orderTable.getTableDisplay(orders);
                    }
                }
                case 6 -> {
                    System.out.println("\n" + BLUE + BOLD + "FILTER BY CATEGORY" + RESET);
                    System.out.println(GREEN + "1. FOOD" + RESET);
                    System.out.println(GREEN + "2. DRINK" + RESET);
                    System.out.println(GREEN + "3. FRUIT" + RESET);
                    System.out.print("Enter category (1-3): ");
                    int cat = sc.nextInt(); sc.nextLine();
                    String category = "";
                    switch (cat) {
                        case 1 -> category = Category.FOOD.getCategory();
                        case 2 -> category = Category.DRINK.getCategory();
                        case 3 -> category = Category.FRUIT.getCategory();
                    }
                    if(!category.isEmpty()){
                        List<ProductResponseDto> productResponseDtoList = productController.filterByCategory(category);
                        productTable.getTableDisplay(productResponseDtoList);
                    }

                }
                case 7 -> {
                    System.out.println("\n" + BLUE + BOLD + "SEARCH PRODUCT BY NAME" + RESET);
                    System.out.print("Enter product name: ");
                    String name = sc.nextLine();
                    List<ProductResponseDto> productResponseDtoList = productController.searchByName(name);
                    productTable.getTableDisplay(productResponseDtoList);

                }
                case 8 -> {
                    System.out.println("\n" + BLUE + BOLD + "USER PROFILE" + RESET);
                    userTable.getTableDisplay(List.of(user));
                }
                case 9 -> {
                    logout();
                    return;
                }
                case 0 -> {
                    System.out.println(YELLOW + "\nThank you for using our system. Goodbye!" + RESET);
                    System.exit(0);
                }
            }
            System.out.print("\n" + CYAN + "Press Enter to continue..." + RESET); sc.nextLine();
        } while (true);
    }

    public void productOrderDisplay(List<ProductResponseDto> products) {
        double total = 0;
        productTable.getTableDisplay(products);
        for (ProductResponseDto product : products) {
            double subTotal = product.price() * product.quantity();
            total += subTotal;
        }
        System.out.println("------------------------------------------------------------");
        System.out.printf("%50s: %d\n",  (GREEN + "Total products" + RESET), products.size());
        System.out.printf("%50s: %.2f$\n",  (GREEN + "Total price" + RESET), total);
    }

    public Integer userMenu() {
        System.out.println(BLUE + BOLD + "\nPlease select an option:" + RESET);
        System.out.println("1. View All Products");
        System.out.println("2. Search Product By Uuid");
        System.out.println("3. Add to Cart");
        System.out.println("4. View Cart/Order");
        System.out.println("5. View Order History");
        System.out.println("6. Filter By Category");
        System.out.println("7. Search Product By Name");
        System.out.println("8. View User Profile");
        System.out.println("9. Logout");
        System.out.println("0. Exit");
        System.out.print(BOLD + "Enter your choice (0-9): " + RESET);
        int option = sc.nextInt();
        sc.nextLine();
        return option;
    }


    public void adminMode(UserResponseDto user) {
        System.out.println(CYAN + BOLD + "\n=== ADMIN DASHBOARD ===" + RESET);
        System.out.println(YELLOW + "Logged in as: " + user.username() + " (ADMIN)" + RESET);

        do {
            System.out.println("\n" + PURPLE + BOLD + "ADMIN MENU" + RESET);
            switch (adminMenu()) {
                case 1 -> {
                    System.out.println("\n" + BLUE + BOLD + "ALL PRODUCTS" + RESET);
                    productTable.getTableDisplay(productController.getAllProducts());
                }
                case 2 -> {
                    System.out.println("\n" + BLUE + BOLD + "ADD NEW PRODUCT" + RESET);
                    System.out.print("Enter product name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter product price: ");
                    double price = sc.nextDouble();
                    sc.nextLine();
                    System.out.print("Enter product quantity: ");
                    int quantity = sc.nextInt();
                    sc.nextLine();
                    if (productController.insertNewProduct(
                            new ProductCreateDto(name, price, quantity, Category.FOOD)
                    )) {
                        System.out.println(GREEN + "✔ Product added successfully!" + RESET);
                    } else {
                        System.err.println(RED + "✖ Error: Something went wrong" + RESET);
                    }
                }
                case 3 -> {
                    System.out.println("\n" + BLUE + BOLD + "SEARCH PRODUCT" + RESET);
                    System.out.print("Enter product uuid: ");
                    String uuid = sc.nextLine();
                    ProductResponseDto product = productController.getProductByUuid(uuid);
                    if (product != null) {
                        productTable.getTableDisplay(List.of(product));
                    } else {
                        System.err.println(RED + "✖ Error: Can't find product with uuid " + uuid + RESET);
                    }
                }
                case 4 -> {
                    System.out.println("\n" + BLUE + BOLD + "DELETE PRODUCT" + RESET);
                    System.out.print("Enter product uuid: ");
                    String uuid = sc.nextLine();
                    if (productController.deleteByUuid(uuid)) {
                        System.out.println(GREEN + "✔ Product deleted successfully!" + RESET);
                    } else {
                        System.err.println(RED + "✖ Error: Can't find product with uuid " + uuid + RESET);
                    }
                }
                case 5 -> {
                    System.out.println("\n" + BLUE + BOLD + "ALL USERS" + RESET);
                    userTable.getTableDisplay(userController.getAllUsers());
                }
                case 6 -> {
                    System.out.println("\n" + BLUE + BOLD + "SEARCH USER" + RESET);
                    System.out.print("Enter user uuid: ");
                    String uuid = sc.nextLine();
                    UserResponseDto userResponseDto = userController.searchUserByUuid(uuid);
                    if (userResponseDto != null) {
                        userTable.getTableDisplay(List.of(userResponseDto));
                    } else {
                        System.err.println(RED + "✖ Error: Can't find user with uuid " + uuid + RESET);
                    }
                }
                case 7 -> {
                    System.out.println("\n" + BLUE + BOLD + "DELETE USER" + RESET);
                    System.out.print("Enter user uuid: ");
                    String uuid = sc.nextLine();
                    if (userController.deleteUserByUuid(uuid)) {
                        System.out.println(GREEN + "✔ User deleted successfully!" + RESET);
                    } else {
                        System.err.println(RED + "✖ Error: Can't find user with uuid " + uuid + RESET);
                    }
                }
                case 8 -> {
                    System.out.println("\n" + BLUE + BOLD + "YOUR PROFILE" + RESET);
                    userTable.getTableDisplay(List.of(user));
                }
                case 9 -> {
                    logout();
                    return;
                }
                case 10 -> {
                    System.out.println("\n" + BLUE + BOLD + "BULK INSERT" + RESET);
                    System.out.print("Enter number of products to generate: ");
                    Long numbers = sc.nextLong();
                    sc.nextLine();
                    Long start = System.currentTimeMillis();
                    productController.insertMultiProduct(numbers);
                    Long end = System.currentTimeMillis();
                    System.out.println(GREEN + "✔ Insert completed in " + (end - start) + " ms" + RESET);
                }
                case 11 -> {
                    System.out.println("\n" + BLUE + BOLD + "CLEAR ALL PRODUCTS" + RESET);
                    System.out.print("Are you sure you want to clear all products? (yes/no): ");
                    String confirm = sc.nextLine();
                    if (confirm.equalsIgnoreCase("yes")) {
                        productController.clearAllProducts();
                        System.out.println(GREEN + "✔ All products have been cleared" + RESET);
                    } else {
                        System.out.println(YELLOW + "Operation cancelled" + RESET);
                    }
                }
                case 12 -> {
                    System.out.println("\n" + BLUE + BOLD + "ALL ORDERS HISTORY" + RESET);
                    List<OrderResponseDto> orders = orderController.getAllOrders();
                    if (orders.isEmpty()) {
                        System.err.println(RED + "✖ There is no order history" + RESET);
                    }else{
                        orderTable.getTableDisplay(orders);
                    }
                }
                case 0 -> {
                    System.out.println(YELLOW + "\nThank you for using our system. Goodbye!" + RESET);
                    System.exit(0);
                }
            }
            System.out.print("\n" + CYAN + "Press Enter to continue..." + RESET);
            sc.nextLine();
        } while (true);
    }

    public Integer adminMenu() {
        System.out.println(BLUE + BOLD + "\nPlease select an option:" + RESET);
        System.out.println(PURPLE + BOLD + "PRODUCT MANAGEMENT" + RESET);
        System.out.println("1. View All Products");
        System.out.println("2. Add New Product");
        System.out.println("3. Search Product");
        System.out.println("4. Delete Product");

        System.out.println(PURPLE + BOLD + "\nUSER MANAGEMENT" + RESET);
        System.out.println("5. View All Users");
        System.out.println("6. Search User");
        System.out.println("7. Delete User");
        System.out.println("8. View Profile");
        System.out.println("9. Log Out");

        System.out.println(PURPLE + BOLD + "\nADVANCED OPTIONS" + RESET);
        System.out.println("10. Bulk Insert Products");
        System.out.println("11. Clear All Products");
        System.out.println("12. View All Orders");
        System.out.println("0. Exit System");

        System.out.print(BOLD + "\nEnter your choice (0-11): " + RESET);
        int option = sc.nextInt();
        sc.nextLine();
        return option;
    }

    public void logout(){
        Path path = Paths.get("user.properties");
        try {
            if (Files.deleteIfExists(path)) {
                System.out.println(YELLOW + "\n✔ Successfully logged out. Goodbye!" + RESET);
            } else {
                System.err.println(RED + "✖ Error: Something went wrong during logout" + RESET);
            }
        } catch (IOException e) {
            System.err.println(RED + "✖ Error: " + e.getMessage() + RESET);
        }
    }
}