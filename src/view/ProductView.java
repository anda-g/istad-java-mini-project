package view;

import controller.ProductController;
import controller.UserController;
import model.dto.ProductCreateDto;
import model.dto.ProductResponseDto;
import model.dto.UserResponseDto;
import model.entites.Category;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class ProductView {
    private final Scanner sc = new Scanner(System.in);
    private final ProductController productController = new ProductController();
    private final UserController userController = new UserController();
//    public ProductView(){
//        System.out.println("Welcome to the Product View");
//        do{
//            switch (menu()){
//                case 1 -> {
//                    productHeadDisplay();
//                    for (ProductResponseDto product : productController.getAllProducts()) {
//                        productDisplay(product);
//                    }
//                }
//                case 2 -> {
//                    System.out.print("Enter product name: ");
//                    String name = sc.nextLine();
//                    System.out.print("Enter product price: ");
//                    double price = sc.nextDouble(); sc.nextLine();
//                    System.out.print("Enter product quantity: ");
//                    int quantity = sc.nextInt(); sc.nextLine();
//                    if(productController.insertNewProduct(
//                            new ProductCreateDto(name, price, quantity, Category.FOOD)
//                    )){
//                        System.out.println("Product added successfully");
//                    }else {
//                        System.out.println("Something went wrong");
//                    }
//
//                }
//                case 3 -> {
//                    System.out.print("Enter product uuid: ");
//                    String uuid = sc.nextLine();
//                    ProductResponseDto product = productController.getProductByUuid(uuid);
//                    if(product!=null){
//                        productHeadDisplay();
//                        productDisplay(product);
//                    }else{
//                        System.err.println("Can't find product with uuid " + uuid);
//                    }
//                }
//                case 4 -> {
//                    System.out.print("Enter product uuid: ");
//                    String uuid = sc.nextLine();
//                    if(productController.deleteByUuid(uuid)){
//                        System.out.println("Product deleted successfully");
//                    }else{
//                        System.err.println("Can't find product with uuid " + uuid);
//                    }
//                }
//                case 5 -> {
//                    return;
//                }
//            }
//            System.out.print("Any key to continue..."); sc.nextLine();
//        }while (true);
//
//
//    }

    public Integer menu(){
        System.out.println("""
                1. VIEW ALL PRODUCTS
                2. SEARCH PRODUCTS
                3. ADD TO CART
                4. ORDER
                5. EXIT""");
        System.out.print("Enter an option: ");
        int option = sc.nextInt();
        sc.nextLine();
        return option;

    }

    public void productHeadDisplay(){
        System.out.printf("%-40s %-10s %-10s %-10s %-10s\n","UUID", "NAME", "PRICE", "QUANTITY", "CATEGORY");
    }

    public void productDisplay(ProductResponseDto product){
        System.out.printf("%-40s %-10s %-10.2f %-10d %-10s\n", product.uuid(), product.name(), product.price(), product.quantity(), product.category().toString());
    }

    public void adminMode(UserResponseDto user) {
        do{
            switch (adminMenu()){
                case 1 -> {
                    productHeadDisplay();
                    for (ProductResponseDto product : productController.getAllProducts()) {
                        productDisplay(product);
                    }
                }
                case 2 -> {
                    System.out.print("Enter product name: ");
                    String name = sc.nextLine();
                    System.out.print("Enter product price: ");
                    double price = sc.nextDouble(); sc.nextLine();
                    System.out.print("Enter product quantity: ");
                    int quantity = sc.nextInt(); sc.nextLine();
                    if(productController.insertNewProduct(
                            new ProductCreateDto(name, price, quantity, Category.FOOD)
                    )){
                        System.out.println("Product added successfully");
                    }else {
                        System.out.println("Something went wrong");
                    }

                }
                case 3 -> {
                    System.out.print("Enter product uuid: ");
                    String uuid = sc.nextLine();
                    ProductResponseDto product = productController.getProductByUuid(uuid);
                    if(product!=null){
                        productHeadDisplay();
                        productDisplay(product);
                    }else{
                        System.err.println("Can't find product with uuid " + uuid);
                    }
                }
                case 4 -> {
                    System.out.print("Enter product uuid: ");
                    String uuid = sc.nextLine();
                    if(productController.deleteByUuid(uuid)){
                        System.out.println("Product deleted successfully");
                    }else{
                        System.err.println("Can't find product with uuid " + uuid);
                    }
                }

                case 5 -> {
                    userHeadDisplay();
                    for (UserResponseDto userResponseDto : userController.getAllUsers()) {
                        userDisplay(userResponseDto);
                    }
                }
                case 6 -> {
                    System.out.print("Enter user uuid: ");
                    String uuid = sc.nextLine();
                    UserResponseDto userResponseDto = userController.searchUserByUuid(uuid);
                    if(userResponseDto!=null){
                        userHeadDisplay();
                        userDisplay(userResponseDto);
                    }else{
                        System.err.println("Can't find user with uuid " + uuid);
                    }
                }
                case 7 -> {
                    System.out.print("Enter user uuid: ");
                    String uuid = sc.nextLine();
                    if(userController.deleteUserByUuid(uuid)){
                        System.out.println("User deleted successfully");
                    }else{
                        System.err.println("Can't find user with uuid " + uuid);
                    }
                }
                case 8 -> {
                    System.out.println("' Account Profile '");
                    userHeadDisplay();
                    userDisplay(user);
                }
                case 9 -> {
                    Path path = Paths.get("user.properties");
                    try{
                        if(Files.deleteIfExists(path)){
                            System.out.println("Account logged out");
                            return;
                        }else{
                            System.err.println("Something went wrong");
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
                case 0 -> {
                    System.exit(0);
                }
            }
            System.out.print("Any key to continue..."); sc.nextLine();
        }while (true);

    }

    public Integer adminMenu(){
        System.out.println("""
                1. VIEW ALL PRODUCTS
                2. INSERT NEW PRODUCT
                3. SEARCH PRODUCT
                4. DELETE PRODUCT
                
                5. VIEW ALL USERS
                6. SEARCH USER
                7. DELETE USER
                8. VIEW PROFILE
                9. LOG OUT
                0. EXIT""");
        System.out.print("Enter an option: ");
        int option = sc.nextInt();
        sc.nextLine();
        return option;

    }

    public void userDisplay(UserResponseDto user){
        System.out.printf("%-40s %-10s %-12s %-7s\n", user.uuid(), user.username(), user.email(), user.role());
    }

    public void userHeadDisplay(){
        System.out.printf("%-40s %-10s %-12s %-7s\n", "UUID", "USERNAME", "EMAIL", "ROLE");
    }
}
