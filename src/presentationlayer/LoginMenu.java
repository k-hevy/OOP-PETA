package presentationlayer;

import datalayer.Repository;
import entitylayer.Admin;
import entitylayer.Requestor;
import entitylayer.SupportAgent;
import entitylayer.User;
import java.util.Scanner;

public class LoginMenu extends BaseMenu {

    public LoginMenu(Scanner input, Repository repository) {
        super(null, input, repository);
    }

    @Override
    protected void displayMenu() {
            System.out.println("\n=== SYSTEM LOGIN ===" );
            System.out.println("[1] LOGIN");
            System.out.println("[2] EXIT");
    }

    @Override
    protected void executeChoice(int choice) {
        switch (choice) {
            case 1 -> handleLogin();
            case 2 -> {
                System.out.println("Exiting Application");
                stop();
            }
        }
    }

    @Override
    protected int getMinChoice() {
        return 1;
    }

    @Override
    protected int getMaxChoice() {
        return 2;
    }

    private void handleLogin() {

        // creates a Menu based on the LoggedInUser --> calls menu.run() --> calls displayMenu implementatation 
        // --> takes a choice from readInt --> calls execute choice (switch statements)

        System.out.println("\n--- Please Enter Credentials ---");
        String userId = readLine("User ID (e.g., REQ-001): ", false);
        String userPassword = readLine("Password: ", false);

        User loggedInUser = repository.findUserById(userId);

        if (loggedInUser == null || !loggedInUser.checkPassword(userPassword)) {
            System.out.println("Login Failed: Invalid User ID or Password.");
            return;
        }

        System.out.println("Login Successful! Welcome, " + loggedInUser.getFullName() + " (" + loggedInUser.getRole() + ")");

        BaseMenu roleMenu = switch (loggedInUser.getRole()) {
            case "ADMIN"        -> new AdminMenu((Admin)               loggedInUser, input, repository);
            case "SUPPORTAGENT" -> new SupportAgentMenu((SupportAgent)loggedInUser, input, repository);
            case "REQUESTOR"    -> new RequestorMenu((Requestor)      loggedInUser, input, repository);
            default             -> null;
        };

        if (roleMenu != null ) {
            roleMenu.run();
        } else {
            System.out.println("Error: Unrecognized role configuration.");
        }

        System.out.println("\nYou have been logged out.");

    }

}
