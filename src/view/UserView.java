package view;

import controller.UserController;
import model.dto.UserResponseDto;
import utils.InputValidator;

import java.util.List;
import java.util.Scanner;

public class UserView {
    private final UserController userController = new UserController();
    private final UserResponseDto user;
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

    public UserView(UserResponseDto user) {
        this.user = user;
    }

    public void viewAllUsers(){
        System.out.println("\n" + BLUE + BOLD + "ALL USERS" + RESET);
        userTable.getTableDisplay(userController.getAllUsers());
    }

    public void searchUserByUuid(){
        System.out.println("\n" + BLUE + BOLD + "SEARCH USER" + RESET);
        String uuid = InputValidator.getString("Enter user uuid: ");
        UserResponseDto userResponseDto = userController.searchUserByUuid(uuid);
        if (userResponseDto != null) {
            userTable.getTableDisplay(List.of(userResponseDto));
        } else {
            System.err.println(RED + "✖ Error: Can't find user with uuid " + uuid + RESET);
        }
    }

    public void deleteUserByUuid(){
        System.out.println("\n" + BLUE + BOLD + "DELETE USER" + RESET);
        String uuid = InputValidator.getString("Enter user uuid: ");
        if (userController.deleteUserByUuid(uuid)) {
            System.out.println(GREEN + "✔ User deleted successfully!" + RESET);
        } else {
            System.err.println(RED + "✖ Error: Can't find user with uuid " + uuid + RESET);
        }
    }

    public void viewUserProfile(){
        System.out.println("\n" + BLUE + BOLD + "USER PROFILE" + RESET);
        userTable.getTableDisplay(List.of(user));
    }
}
