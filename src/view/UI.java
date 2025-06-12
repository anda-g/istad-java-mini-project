package view;

import controller.UserController;
import model.dto.UserCreateDto;
import model.dto.UserLoginDto;
import model.dto.UserResponseDto;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.Scanner;

public class UI {
    private static UserResponseDto user;
    private static final UserController userController = new UserController();
    private static final Scanner sc = new Scanner(System.in);

    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";
    private static final String GREEN = "\033[0;32m";
    private static final String YELLOW = "\033[0;33m";
    private static final String BLUE = "\033[0;34m";
    private static final String PURPLE = "\033[0;35m";
    private static final String CYAN = "\033[0;36m";
    private static final String BOLD = "\033[1m";
    private static final String UNDERLINE = "\033[4m";

    public static void home() {
        System.out.println(CYAN + BOLD + "\n=== WELCOME TO PRODUCT MANAGEMENT SYSTEM ===" + RESET);

        do {
            do {
                authUser();
            } while (user == null);

            if (user.role().equals("admin")) {
                new ProductView().adminMode(user);
                user = null;
            } else {
                new ProductView().userMode(user);
            }
        } while (true);
    }

    public static void authUser() {
        Path path = Paths.get("user.properties");
        if (Files.exists(path)) {
            try (FileInputStream file = new FileInputStream("user.properties")) {
                Properties prop = new Properties();
                prop.load(file);
                String uuid = prop.getProperty("uuid");
                user = userController.findUserByUuid(uuid);
                if (user == null) {
                    System.out.println(YELLOW + "\n⚠ Session expired or invalid. Please login again." + RESET);
                    loginOrRegister();
                } else {
                    System.out.println(GREEN + "\n✔ Welcome back, " + user.username() + "!" + RESET);
                }
            } catch (IOException e) {
                System.err.println(RED + "✖ Error: " + e.getMessage() + RESET);
            }
        } else {
            loginOrRegister();
        }
    }

    public static void loginOrRegister() {
        System.out.println("\n" + PURPLE + BOLD + "AUTHENTICATION MENU" + RESET);
        System.out.println(BLUE + "1. Login to existing account");
        System.out.println("2. Register new account");
        System.out.println("3. Exit system" + RESET);
        System.out.print(BOLD + "\nEnter your choice (1-3): " + RESET);

        int choice = sc.nextInt();
        sc.nextLine();
        String uuid;

        switch (choice) {
            case 1 -> {
                System.out.println("\n" + CYAN + BOLD + "=== USER LOGIN ===" + RESET);
                System.out.print("Enter email: ");
                String email = sc.nextLine();
                System.out.print("Enter password: ");
                String password = sc.nextLine();

                UserLoginDto userLoginDto = new UserLoginDto(email, password);
                user = userController.login(userLoginDto);

                if (user != null) {
                    System.out.println(GREEN + "\n✔ Login successful! Welcome, " + user.username() + "!" + RESET);
                    uuid = user.uuid();
                } else {
                    System.err.println(RED + "\n✖ Login failed. Invalid credentials." + RESET);
                    return;
                }
            }
            case 2 -> {
                System.out.println("\n" + CYAN + BOLD + "=== USER REGISTRATION ===" + RESET);
                System.out.println("Please provide the following information:");

                System.out.print("Username: ");
                String username = sc.nextLine();
                System.out.print("Email: ");
                String email = sc.nextLine();
                System.out.print("Password: ");
                String password = sc.nextLine();
                System.out.print("Role (user/admin): ");
                String role = sc.nextLine();

                UserCreateDto userCreateDto = new UserCreateDto(username, email, password, role);
                user = userController.register(userCreateDto);

                if (user != null) {
                    System.out.println(GREEN + "\n✔ Registration successful! Welcome, " + user.username() + "!" + RESET);
                    uuid = user.uuid();
                } else {
                    System.err.println(RED + "\n✖ Registration failed. Please try again." + RESET);
                    return;
                }
            }
            case 3 -> {
                System.out.println(YELLOW + "\nThank you for using our system. Goodbye!" + RESET);
                System.exit(0);
            }
            default -> {
                System.err.println(RED + "\n✖ Invalid choice. Please try again." + RESET);
                return;
            }
        }

        try (FileOutputStream file = new FileOutputStream("user.properties")) {
            Properties prop = new Properties();
            prop.setProperty("uuid", user.uuid());
            prop.store(file, null);
        } catch (IOException e) {
            System.err.println(RED + "✖ Error saving session: " + e.getMessage() + RESET);
        }
    }
}