package presentationlayer;

import datalayer.Repository;
import entitylayer.User;
import java.util.Scanner;

public class LoginMenu extends BaseMenu {

    User authenticatedUser;

    public LoginMenu(Scanner input, Repository repository) {
        super(null, input, repository);
    }

    @Override
    protected void displayMenu() {
            System.out.println("\n=== SYSTEM LOGIN ===" );
            System.out.println("[1] LOGIN");
            System.out.println("[0] EXIT");
    }

    @Override
    protected void executeChoice(int choice) {
        switch (choice) {
            case 1 -> handleLogin();
            case 0 -> {
                System.out.println("Exiting Application");
                stop();
            }
        }
    }

    @Override
    protected int getMinChoice() {
        return 0;
    }

    @Override
    protected int getMaxChoice() {
        return 1;
    }

    private void handleLogin() {

        System.out.println("\n--- Please Enter Credentials ---");
        String userId = readLine("User ID (e.g., REQ-001): ", false);
        String userPassword = readLine("Password: ", false);

        User foundUser = repository.findUserById(userId);
        if (foundUser == null || !foundUser.checkPassword(userPassword)) {
            System.out.println("Login Failed: Invalid User ID or Password.");
            return;
        }

        System.out.println("Login Successful! Welcome, " + foundUser.getFullName() + " (" + foundUser.getRole() + ")");
        authenticatedUser = foundUser;
        waitForEnter();
        stop();

    }

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

}
