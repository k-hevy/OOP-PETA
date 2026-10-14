package presentationlayer;
import datalayer.Repository;
import entitylayer.User;
import java.util.Scanner;

public abstract class BaseMenu {

    protected User user;
    protected Scanner input;
    protected boolean running;
    protected Repository repository;

    protected BaseMenu(User user, Scanner input, Repository repository) {
        this.input = input;
        this.user = user;
        this.repository = repository;
        this.running = true;
    }

    public void run() {
        while (running) {
            displayMenu(); // display your menu
            int choice = readInt("Enter your choice: ", getMinChoice(), getMaxChoice()); // prompts the user
            executeChoice(choice); // executes that choice
        }
    } // Contains Main loop of Menus only

    public void stop() {
        this.running = false;
    }

    protected int readInt(String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String rawInput = input.nextLine();
            if (rawInput == null || rawInput.trim().isEmpty()) {
                System.out.println("Input cannot be blank. Please try again");
                continue;
            } 
            try {
                int value = Integer.parseInt(rawInput.trim());
                if (value >= min && value <= max) { return value; }
                System.out.println("Invalid choice. Please enter a number between " + min + " and " + max + ".");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
    }

    protected String readLine(String prompt, boolean allowBlank) {
        while (true) {
            System.out.print(prompt);
            String line = input.nextLine();

            if (line != null) {
                line = line.trim();
                if (allowBlank || !line.isEmpty()) {
                    return line;
                }
            }
            System.out.println("Field cannot be blank. Please enter a valid value.");
        }
    } 

    protected String truncate(String text, int maxLength) {
        if (text == null) { return ""; }
        if (text.length() <= maxLength) {return text;}
        if (maxLength <= 3) { return text.substring(0, maxLength); }
        return text.substring(0, maxLength -3) + "...";
    }

    protected boolean confirm(String prompt) {
        while (true) {
            System.out.println(prompt + "? [Y/N]: ");
            String rawChoice = input.nextLine();
            if (rawChoice == null || rawChoice.trim().isEmpty()) {
                System.out.println("Input cannot be blank. Please try again.");
                continue;
            }
            String choice = rawChoice.trim().toLowerCase();
            if (choice.equals("y") || choice.equals("yes")) {
                return true;
            } else if (choice.equals("n") || choice.equals("no")) {
                return false;
            } 
            System.out.println("Invalid Input. Please enter Y for Yes, and N for No.");
        }
    } // asks a question then returns the choice

    protected void waitForEnter() {
        System.out.println("\nPress Enter to go back ");
    } // 

    protected abstract void displayMenu();
    protected abstract void executeChoice(int choice);
    protected abstract int getMinChoice();
    protected abstract int getMaxChoice();
    
}