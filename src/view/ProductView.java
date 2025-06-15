package view;

import controller.OrderController;
import controller.ProductController;
import model.dto.*;
import model.entites.Category;
import utils.InputValidator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ProductView {
    private final UserResponseDto user;
    private final ProductController productController = new ProductController();
    private final List<ProductResponseDto> productCart = new ArrayList<>();
    private final OrderController orderController = new OrderController();
    private final TableUI<ProductResponseDto> productTable = new TableUI<>();
    private final TableUI<OrderResponseDto> orderTable = new TableUI<>();

    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";
    private static final String BLUE = "\033[0;34m";
    private static final String BOLD = "\033[1m";

    public ProductView(UserResponseDto user) {
        this.user = user;
    }

    public void viewAllProducts() {
        System.out.println("\n" + BLUE + BOLD + "ALL PRODUCTS" + RESET);
        List<ProductResponseDto> allProducts = productController.getAllProducts();
        if (allProducts!= null && !allProducts.isEmpty()) {
            productTable.getTableDisplay(allProducts);
        }else{
            System.err.println(RED + "✖ The product is empty" + RESET);
        }
    }

    public void searchProductByUuid(){
        System.out.println("\n" + BLUE + BOLD + "SEARCH PRODUCT" + RESET);
        String uuid = InputValidator.getString("Enter product uuid: ");
        ProductResponseDto product = productController.getProductByUuid(uuid);
        if (product != null) {
            productTable.getTableDisplay(List.of(product));
        } else {
            System.err.println(RED + "✖ Error: Can't find product with uuid " + uuid + RESET);
        }
    }

    public void filterProductByCategory(){
        System.out.println("\n" + BLUE + BOLD + "FILTER BY CATEGORY" + RESET);
        System.out.println(GREEN + "1. FOOD" + RESET);
        System.out.println(GREEN + "2. DRINK" + RESET);
        System.out.println(GREEN + "3. FRUIT" + RESET);
        int cat = InputValidator.getInt("Enter category (1-3): ");
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

    public void addProductToCart(){
        System.out.println("\n" + BLUE + BOLD + "ADD TO CART" + RESET);
        String uuid;
        do {
            uuid = InputValidator.getString("Enter product uuid (or 'exit' to finish): ");
            if (uuid.equalsIgnoreCase("exit")) {
                break;
            }
            ProductResponseDto product = productController.getProductByUuid(uuid);
            if (product == null) {
                System.err.println(RED + "✖ Error: Product not found with uuid " + uuid + RESET);
            } else {
                int qty;
                do{
                    qty = InputValidator.getInt("Enter product quantity: ");
                    if(qty > product.quantity()){
                        System.err.println(RED + "✖ Requested quantity exceeds available stock. Maximum allowed is " + product.quantity() + "." + RESET);
                    }
                }while (qty > product.quantity());

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

    public void viewCartOrder(){
        if (productCart.isEmpty()) {
            System.err.println(RED + "✖ Your cart is empty" + RESET);
        } else {
            System.out.println("\n" + BLUE + BOLD + "YOUR ORDER SUMMARY" + RESET);
            productOrderDisplay(productCart);
            String c = InputValidator.getString(BOLD + BOLD + "You want to order (y/n): ").toUpperCase();
            if (c.charAt(0) == 'y') {
                List<String> productUuids = new ArrayList<>();
                for (ProductResponseDto responseDto : productCart) {
                    productUuids.add(responseDto.uuid());
                    ProductResponseDto getProduct = productController.getProductByUuid(responseDto.uuid());
                    ProductUpdateDto productUpdateDto = new ProductUpdateDto( getProduct.name(), getProduct.quantity() - responseDto.quantity(), getProduct.price());
                    productController.updateProductByUuid(responseDto.uuid(), productUpdateDto);
                }
                OrderCreateDto orderCreateDto = new OrderCreateDto(user.uuid(), productUuids, LocalDate.now());
                orderController.saveOrder(orderCreateDto);
                System.out.println(GREEN + "Order created successfully!" + RESET);
                System.out.println(GREEN + BOLD + "Wait for your meals!!" + RESET);
                productCart.clear();
            }
        }
    }

    public void viewOrderHistoryForUser(){
        System.out.println("\n" + BLUE + BOLD + "YOUR ORDER HISTORY" + RESET);
        List<OrderResponseDto> orders = orderController.getOrdersByCustomer(user.uuid());
        if (orders.isEmpty()) {
            System.err.println(RED + "✖ There is no order history" + RESET);
        }else{
            orderTable.getTableDisplay(orders);
        }
    }

    public void viewAllOrderHistory(){
        System.out.println("\n" + BLUE + BOLD + "ALL ORDERS HISTORY" + RESET);
        List<OrderResponseDto> orders = orderController.getAllOrders();
        if (orders.isEmpty()) {
            System.err.println(RED + "✖ There is no order history" + RESET);
        }else{
            orderTable.getTableDisplay(orders);
        }
    }

    public void searchProductByName(){
        System.out.println("\n" + BLUE + BOLD + "SEARCH PRODUCT BY NAME" + RESET);
        String name = InputValidator.getString("Enter product name: ");
        List<ProductResponseDto> productResponseDtoList = productController.searchByName(name);
        if (productResponseDtoList != null && !productResponseDtoList.isEmpty()) {
            productTable.getTableDisplay(productResponseDtoList);
        } else {
            System.err.println(RED + "✖ Error: Can't find product with name " + name + RESET);
        }
    }

    private void productOrderDisplay(List<ProductResponseDto> products) {
        double total = 0;
        productTable.getTableDisplay(products);
        for (ProductResponseDto product : products) {
            double subTotal = product.price() * product.quantity();
            total += subTotal;
        }
        System.out.println(GREEN + BOLD + BOLD + "TOTAL PRODUCTS : " + RESET +  products.size());
        System.out.println(GREEN + BOLD + BOLD + "TOTAL PRICE    : $ " + RESET +  total);
    }

    public void insertNewProduct(){
        System.out.println("\n" + BLUE + BOLD + "ADD NEW PRODUCT" + RESET);
        String name = InputValidator.getString("Enter product name: ");
        double price = InputValidator.getDouble("Enter product price: ");
        int quantity = InputValidator.getInt("Enter product quantity: ");
        System.out.println(BOLD + BOLD + "CHOOSE PRODUCT CATEGORY" + RESET);
        System.out.println(GREEN + "1. FOOD" + RESET);
        System.out.println(GREEN + "2. DRINK" + RESET);
        System.out.println(GREEN + "3. FRUIT" + RESET);
        int cat = InputValidator.getInt("Enter category (1-3): ");
        Category category = Category.FOOD;
        switch (cat) {
            case 2 -> category = Category.DRINK;
            case 3 -> category = Category.FRUIT;
        }
        if (productController.insertNewProduct(
                new ProductCreateDto(name, price, quantity, category)
        )) {
            System.out.println(GREEN + "✔ Product added successfully!" + RESET);
        } else {
            System.err.println(RED + "✖ Error: Something went wrong" + RESET);
        }
    }

    public void deleteProductByUuid(){
        System.out.println("\n" + BLUE + BOLD + "DELETE PRODUCT" + RESET);
        String uuid = InputValidator.getString("Enter product uuid: ");
        if (productController.deleteByUuid(uuid)) {
            System.out.println(GREEN + "✔ Product deleted successfully!" + RESET);
        } else {
            System.err.println(RED + "✖ Error: Can't find product with uuid " + uuid + RESET);
        }
    }

    public void bulkInsertProduct(){
        System.out.println("\n" + BLUE + BOLD + "BULK INSERT" + RESET);
        Long numbers = InputValidator.getLong("Enter number of products to generate: ");
        Long start = System.currentTimeMillis();
        productController.insertMultiProduct(numbers);
        Long end = System.currentTimeMillis();
        System.out.println(GREEN + "✔ Insert completed in " + (end - start) + " ms" + RESET);
    }

    public void bulkReadProduct(){
        System.out.println("\n" + BLUE + BOLD + "BULK READ" + RESET);
        Long numbers = InputValidator.getLong("Enter number of products to read: ");
        Long start = System.currentTimeMillis();
        List<ProductResponseDto> productResponseDtoList = productController.readMultiProduct(numbers);
        if (productResponseDtoList != null) {
            productTable.getTableDisplay(productResponseDtoList);
        }else{
            System.out.println(RED + "Error: Something went wrong" + RESET);
        }
        Long end = System.currentTimeMillis();
        System.out.println(GREEN + "✔ Insert completed in " + (end - start) + " ms" + RESET);
    }

    public void updateProductByUuid(){
        System.out.println("\n" + BLUE + BOLD + "UPDATE PRODUCT BY UUID" + RESET);
        String uuid = InputValidator.getString("Enter product uuid: ");
        ProductResponseDto productResponseDto = productController.getProductByUuid(uuid);
        if (productResponseDto == null) {
            System.err.println(RED + "✖ Error: Can't find product with uuid " + uuid + RESET);
            return;
        }
        String name = InputValidator.getString("Enter new product name: ");
        double price = InputValidator.getDouble("Enter new product price: ");
        int quantity = InputValidator.getInt("Enter new product quantity: ");
        ProductUpdateDto updateDto = new ProductUpdateDto(name, quantity, price);
        if(productController.updateProductByUuid(uuid, updateDto)){
            System.out.println(GREEN + "✔ Product updated successfully!" + RESET);
        }
    }


    public void clearAllProducts(){
        System.out.println("\n" + BLUE + BOLD + "CLEAR ALL PRODUCTS" + RESET);
        String confirm = InputValidator.getString("Are you sure you want to clear all products? (yes/no): ");
        if (confirm.equalsIgnoreCase("yes")) {
            productController.clearAllProducts();
            System.out.println(GREEN + "✔ All products have been cleared" + RESET);
        } else {
            System.out.println(YELLOW + "Operation cancelled" + RESET);
        }
    }

    public static void printInvoice(String brandName, String customerName, List<ProductResponseDto> products) {
        final int WIDTH = 60;
        final int NAME_WIDTH = 28;
        final int PRICE_WIDTH = 10;
        final int QTY_WIDTH = 5;
        final int TOTAL_WIDTH = 10;

        final String TOP_BORDER = "╔" + "═".repeat(WIDTH - 2) + "╗";
        final String BOTTOM_BORDER = "╚" + "═".repeat(WIDTH - 2) + "╝";
        final String SECTION_BORDER = "╠" + "═".repeat(WIDTH - 2) + "╣";
        final String LINE_BORDER = "╟" + "─".repeat(WIDTH - 2) + "╢";
        final String EMPTY_LINE = "║" + " ".repeat(WIDTH - 2) + "║";

        String currentDate = new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date());

        double grandTotal = products.stream()
                .mapToDouble(p -> p.price() * p.quantity())
                .sum();

        System.out.println(TOP_BORDER);

        int brandPadding = (WIDTH - 2 - brandName.length()) / 2;
        System.out.printf("║%" + (brandPadding + brandName.length()) + "s%" + (WIDTH - 2 - brandPadding - brandName.length()) + "s║%n",
                brandName, "");

        System.out.println(SECTION_BORDER);

        System.out.printf("║ %-12s: %-43s║%n", "OrderID", UUID.randomUUID());
        System.out.printf("║ %-12s: %-43s║%n", "Date", currentDate);
        System.out.printf("║ %-12s: %-43s║%n", "Customer", customerName);
        System.out.println(SECTION_BORDER);

        System.out.printf("║ %-" + NAME_WIDTH + "s %-" + PRICE_WIDTH + "s %-" + QTY_WIDTH + "s %" + TOTAL_WIDTH + "s ║%n",
                "PRODUCT", "PRICE", "QTY", "TOTAL");
        System.out.println(LINE_BORDER);

        for (ProductResponseDto product : products) {
            String displayName = product.name().length() > NAME_WIDTH
                    ? product.name().substring(0, NAME_WIDTH - 3) + "..."
                    : product.name();

            double totalPrice = product.price() * product.quantity();

            System.out.printf("║ %-" + NAME_WIDTH + "s $%-" + (PRICE_WIDTH -1) + ".2f %-" + QTY_WIDTH + "d $%" + (TOTAL_WIDTH -1) + ".2f ║%n",
                    displayName, product.price(), product.quantity(), totalPrice);
        }

        System.out.println(SECTION_BORDER);
        System.out.printf("║%" + (WIDTH - TOTAL_WIDTH - 8) + "sTOTAL: $%" + (TOTAL_WIDTH - 3) + ".2f ║%n", "", grandTotal);
        System.out.println(BOTTOM_BORDER);
    }

}