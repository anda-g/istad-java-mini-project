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

    public static void home(){
        do{
            authUser();
        }while (user == null);
        if(user.role().equals("admin")){
            new ProductView().adminMode(user);
        }
    }

    public static void authUser(){
        Path path = Paths.get("user.properties");
        if(Files.exists(path)){
            try(FileInputStream file = new FileInputStream("user.properties")) {
                Properties prop = new Properties();
                prop.load(file);
                String uuid = prop.getProperty("uuid");
                user = userController.findUserByUuid(uuid);
                if(user == null){
                    loginOrRegister();
                }
            } catch (IOException e) {
                System.out.println(e.getMessage());
            }
        }else{
            loginOrRegister();
        }

    }
    public static void loginOrRegister(){

        System.out.println("""
                Login or Register
                1. Login
                2. Register
                3. Exit""");
        System.out.print("[+] Enter your choice: ");
        int choice = sc.nextInt(); sc.nextLine();
        String uuid;
        switch(choice){
            case 1 -> {
                System.out.println("=< Welcome back >=");
                System.out.print("[+] Enter email: ");
                String email = sc.nextLine();
                System.out.print("[+] Enter password: ");
                String password = sc.nextLine();
                UserLoginDto userLoginDto = new UserLoginDto(email, password);
                user = userController.login(userLoginDto);
                if(user != null){
                    System.out.println("Login Successful");
                    uuid = user.uuid();
                }else{
                    System.err.println("[!] Login Failed");
                    return;
                }

            }
            case 2 -> {
                System.out.println("'=< Type in the following information >=");
                System.out.print("[+] Enter your username: ");
                String username = sc.nextLine();
                System.out.print("[+] Enter email: ");
                String email = sc.nextLine();
                System.out.print("[+] Enter password: ");
                String password = sc.nextLine();
                System.out.print("[+] Enter role: ");
                String role = sc.nextLine();
                UserCreateDto userCreateDto = new UserCreateDto(username, email, password, role);
                user = userController.register(userCreateDto);
                if(user != null){
                    System.out.println("Register Successful");
                    uuid = user.uuid();
                }else{
                    System.err.println("[!] Register Failed");
                    return;
                }
            }
            case 3 -> {
                System.exit(0);
            }
        }

        try(FileOutputStream file = new FileOutputStream("user.properties")) {
            Properties prop = new Properties();
            prop.setProperty("uuid", user.uuid());
            prop.store(file, null);
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
