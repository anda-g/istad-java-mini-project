package utils;

import java.util.Scanner;

public class InputValidator {
    private static final Scanner scanner = new Scanner(System.in);

    private static final String RESET = "\033[0m";
    private static final String RED = "\033[0;31m";

    public static int getInt(String message) {
        int num;
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                num = Integer.parseInt(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println(RED + "Please enter a valid integer." + RESET);
            }
        }
        return num;
    }

    public static double getDouble(String message) {
        double num;
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                num = Double.parseDouble(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println(RED + "Please enter a valid double." + RESET);
            }
        }
        return num;
    }

    public static long getLong(String message) {
        long num;
        while (true) {
            System.out.print(message);
            String input = scanner.nextLine();
            try {
                num = Long.parseLong(input);
                break;
            } catch (NumberFormatException e) {
                System.out.println(RED + "Please enter a valid long." + RESET);
            }
        }
        return num;
    }

    public static String getString(String message) {
        String string;
        do {
            System.out.print(message);
            string = scanner.nextLine();
            if (string.isEmpty()) {
                System.out.println(RED + "Please enter the required field." + RESET);
            }
        } while (string.isEmpty());
        return string.trim();
    }

    public static String getEmail(String message) {
        String email;
        while (true) {
            email = getString(message);
            if (email.contains("@") && email.contains(".")) {
                break;
            } else {
                System.out.println(RED + "Please enter a valid email." + RESET);
            }
        }
        return email.trim();
    }
}
