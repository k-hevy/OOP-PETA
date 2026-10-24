package presentationlayer;

import datalayer.Repository;
import entitylayer.User;
import java.util.Scanner;

public class LoginMenu extends BaseMenu {

    // It handles the login process and allows the user to enter their user ID and password, verifies their credential, 
    // and identifies the authenticated user before they access the appropriate menu. 
    // Throws early for user experience

    private User authenticatedUser;

    // Constructs a LoginMenu instance
    public LoginMenu(Scanner input, Repository repository) {
        super(null, input, repository);
    }

    // Displays the login menu options
    @Override
    protected void displayMenu() {
        printHeader("SYSTEM LOGIN");
        System.out.println("[1] LOGIN");
        System.out.println("[0] EXIT");
    }

    // Executes the selected login menu choice
    @Override
    protected void executeChoice(int choice) {
        switch (choice) {
            case 1 -> handleLogin();
            case 0 -> {
                System.out.println("Exiting Application");
                stop();
            }
        }
        waitForEnter();
    }

    // Returns minimum menu choice
    @Override
    protected int getMinChoice() {
        return 0;
    }

    // Returns maximum menu choice
    @Override
    protected int getMaxChoice() {
        return 1;
    }

    // Handles user credential input and authentication
    private void handleLogin() {

        printHeader("Please Enter Credentials");
        String userId = readLine("User ID (e.g., ACC-001): ", false);
        String userPassword = readLine("Password: ", false);

        System.out.println();

        User foundUser = repository.findUserById(userId);
        if (foundUser == null || !foundUser.checkPassword(userPassword)) {
            printError("Login Failed: Invalid User ID or Password.");
            return;
        }

        printSuccess("Login Successful! Welcome, " + foundUser.getFullName() + " (" + foundUser.getRole() + ")");
        authenticatedUser = foundUser;
        stop();

    }

    // Returns the successfully authenticated user
    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

}
