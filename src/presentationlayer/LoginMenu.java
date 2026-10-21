package presentationlayer;

import datalayer.Repository;
import entitylayer.Ticket;
import entitylayer.User;
import java.util.ArrayList;
import java.util.Scanner;

public class LoginMenu extends BaseMenu {

    User authenticatedUser;

    public LoginMenu(Scanner input, Repository repository) {
        super(null, input, repository);
    }

    @Override
    protected void displayMenu() {
        printHeader("SYSTEM LOGIN");
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
        waitForEnter();
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

        printHeader("Please Enter Credentials");
        String userId = readLine("User ID (e.g., REQ-001): ", false);
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

    public User getAuthenticatedUser() {
        return authenticatedUser;
    }

    @Override
    protected void displayTickets(ArrayList<Ticket> tickets) {
    }

}
