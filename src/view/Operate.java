package view;

import model.dto.UserResponseDto;
import utils.InputValidator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class Operate {
    private final UserResponseDto user;
    private final Scanner sc = new Scanner(System.in);
    private final ProductView productView;
    private final UserView userView;

    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";
    private static final String BLUE = "\033[0;34m";
    private static final String PURPLE = "\033[0;35m";
    private static final String CYAN = "\033[0;36m";
    private static final String BOLD = "\033[1m";
    private static final String UNDERLINE = "\033[4m";


    public Operate(UserResponseDto user) {
        this.user = user;
        productView = new ProductView(user);
        userView = new UserView(user);
        if(user.role().equals("admin")) {
            adminMode();
        }else{
            userMode();
        }
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
        return InputValidator.getInt(BOLD + "Enter your choice (0-9): " + RESET);
    }
    public void userMode() {
        System.out.println(CYAN + BOLD + "\n=== WELCOME TO PRODUCT MANAGEMENT SYSTEM ===" + RESET);
        System.out.println(YELLOW + "Logged in as: " + user.username() + " (" + user.role() + ")" + RESET);

        do {
            System.out.println("\n" + PURPLE + BOLD + "MAIN MENU" + RESET);
            switch (userMenu()) {
                case 1 -> {
                    productView.viewAllProducts();
                }
                case 2 -> {
                    productView.searchProductByUuid();
                }
                case 3 -> {
                    productView.addProductToCart();
                }
                case 4 -> {
                    productView.viewCartOrder();
                }
                case 5 -> {
                    productView.viewOrderHistoryForUser();
                }
                case 6 -> {
                    productView.filterProductByCategory();
                }
                case 7 -> {
                    productView.searchProductByName();
                }
                case 8 -> {
                    userView.viewUserProfile();
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

    public Integer adminMenu() {
        System.out.println(BLUE + BOLD + "\nPlease select an option:" + RESET);
        System.out.println(PURPLE + BOLD + "PRODUCT MANAGEMENT" + RESET);
        System.out.println("1. View All Products");
        System.out.println("2. Add New Product");
        System.out.println("3. Search Product By Uuid");
        System.out.println("4. Delete Product");
        System.out.println("5. Search Product By Name");
        System.out.println("6. Filter Products By Category");
        System.out.println("7. Update Product By Uuid");

        System.out.println(PURPLE + BOLD + "\nUSER MANAGEMENT" + RESET);
        System.out.println("8. View All Users");
        System.out.println("9. Search User");
        System.out.println("10. Delete User");
        System.out.println("11. View Profile");
        System.out.println("12. Log Out");

        System.out.println(PURPLE + BOLD + "\nADVANCED OPTIONS" + RESET);
        System.out.println("13. Bulk Insert Products");
        System.out.println("14. Bulk Read Products");
        System.out.println("15. Clear All Products");
        System.out.println("16. View All Orders");
        System.out.println("0. Exit System");

        return InputValidator.getInt(BOLD + "\nEnter your choice (0-16): " + RESET);
    }

    public void adminMode() {
        System.out.println(CYAN + BOLD + "\n=== ADMIN DASHBOARD ===" + RESET);
        System.out.println(YELLOW + "Logged in as: " + user.username() + " (ADMIN)" + RESET);

        do {
            System.out.println("\n" + PURPLE + BOLD + "ADMIN MENU" + RESET);
            switch (adminMenu()) {
                case 1 -> {
                    productView.viewAllProducts();
                }
                case 2 -> {
                    productView.insertNewProduct();
                }
                case 3 -> {
                    productView.searchProductByUuid();
                }
                case 4 -> {
                    productView.deleteProductByUuid();
                }
                case 5 -> {
                    productView.searchProductByName();
                }
                case 6 -> {
                    productView.filterProductByCategory();
                }
                case 7 -> {
                    productView.updateProductByUuid();
                }
                case 8 -> {
                    userView.viewAllUsers();
                }
                case 9 -> {
                    userView.searchUserByUuid();
                }
                case 10 -> {
                    userView.deleteUserByUuid();
                }
                case 11 -> {
                    userView.viewUserProfile();
                }
                case 12 -> {
                    logout();
                    return;
                }
                case 13 -> {
                    productView.bulkInsertProduct();
                }
                case 14 -> {
                    productView.bulkReadProduct();
                }
                case 15 -> {
                    productView.clearAllProducts();
                }
                case 16 -> {
                    productView.viewAllOrderHistory();
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
