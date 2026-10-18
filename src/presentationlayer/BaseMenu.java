package presentationlayer;
import datalayer.Repository;
import entitylayer.User;
import java.util.Scanner;

public abstract class BaseMenu {

    protected User user;
    protected Scanner input;
    protected boolean running;
    protected Repository repository;

    public static String LINE = "======================================";

    protected BaseMenu(User user, Scanner input, Repository repository) {
        this.input = input;
        this.user = user;
        this.repository = repository;
        this.running = true;
    }

    public void run() {
        while (running) {
            System.out.println("");
            displayMenu(); // display your menu
            System.out.println("");
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
            System.out.print(prompt + " [Y/N]: ");
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
        System.out.print("\nPress Enter to proceed: ");
        input.nextLine();
    } 

    protected String readPriority() {
        while(true) {
            System.out.println("");
            System.out.println("[1] LOW");
            System.out.println("[2] MEDIUM");
            System.out.println("[3] HIGH");
            System.out.println("[4] URGENT");
            System.out.println("");
            int choice = readInt("choice: ", 1, 4);
            switch (choice) {
                case 1 -> { return "LOW"; }
                case 2 -> { return "MEDIUM"; }
                case 3 -> { return "HIGH"; }
                case 4 -> { return "URGENT"; }
            }
        }
    }

    protected String readCategory() {
        while(true) {
            System.out.println("");
            System.out.println("[1] FACILITIES MAINTENANCE");
            System.out.println("[2] SOFTWARE SYSTEMS");
            System.out.println("[3] NETWORK");
            System.out.println("[4] HARDWARE");
            System.out.println("[5] ACCOUNT SECURITY"); 
            System.out.println();
            int choice = readInt("choice: ", 1, 5);
            switch (choice) {
                case 1 -> { return "FACILITIES_MAINTENANCE"; }
                case 2 -> { return "SOFTWARE_SYSTEMS"; }
                case 3 -> { return "NETWORK"; }
                case 4 -> { return "HARDWARE"; }
                case 5 -> { return "ACCOUNT_SECURITY"; }
            }
        }
    }

    protected String readSubject(String category) {
        switch (category) {
            case "FACILITIES_MAINTENANCE" -> {
                System.out.println();
                System.out.println("[1] Plumbing & Leaks");
                System.out.println("[2] HVAC & Temperature");
                System.out.println("[3] Lighting & Electrical");
                System.out.println("[4] Carpentry & Keys");
                System.out.println("[5] Others (specify)");
                System.out.println();
                int choice = readInt("choice: ", 1, 5);
                switch (choice) {
                    case 1 -> { return "Plumbing & Leaks"; }
                    case 2 -> { return "HVAC & Temperature"; }
                    case 3 -> { return "Lighting & Electrical"; }
                    case 4 -> { return "Carpentry & Keys"; }
                    case 5 -> { return readLine("Enter Subject: ", false); }
                }
            }
            case "SOFTWARE_SYSTEMS" -> {
                System.out.println();
                System.out.println("[1] LMS / Portal Issue");
                System.out.println("[2] Software Installation");
                System.out.println("[3] Email / Collaboration");
                System.out.println("[4] Grading & Enrollment Tools ");
                System.out.println("[5] Others (specify)");
                System.out.println();
                int choice = readInt("choice: ", 1, 5);
                switch (choice) {
                    case 1 -> { return "LMS / Portal Issue"; }
                    case 2 -> { return "Software Installation"; }
                    case 3 -> { return "Email / Collaboration"; }
                    case 4 -> { return "Grading & Enrollment Tools "; }
                    case 5 -> { return readLine("Enter Subject: ", false); }
                }
            }
            case "NETWORK" -> {
                System.out.println();
                System.out.println("[1] Campus Wi-Fi Disconnect");
                System.out.println("[2] Ethernet / Wired Port ");
                System.out.println("[3] VPN Connection");
                System.out.println("[4] Slow Internet Speed");
                System.out.println("[5] Others (specify)");
                System.out.println();
                int choice = readInt("choice: ", 1, 5);
                switch (choice) {
                    case 1 -> { return "Campus Wi-Fi Disconnect"; }
                    case 2 -> { return "Ethernet / Wired Port "; }
                    case 3 -> { return "VPN Connection "; }
                    case 4 -> { return "Slow Internet Speed"; }
                    case 5 -> { return readLine("Enter Subject: ", false); }
                }
            }
            case "HARDWARE" -> {
                System.out.println();
                System.out.println("[1] Computer / Lab PC Failure");
                System.out.println("[2] Printer & Copier Jam");
                System.out.println("[3] Classroom AV & Projector");
                System.out.println("[4] Peripherals & Cables");
                System.out.println("[5] Others (specify)");
                System.out.println();
                int choice = readInt("choice: ", 1, 5);
                switch (choice) {
                    case 1 -> { return "Computer / Lab PC Failure"; }
                    case 2 -> { return "Printer & Copier Jam"; }
                    case 3 -> { return "Classroom AV & Projector"; }
                    case 4 -> { return "Peripherals & Cables"; }
                    case 5 -> { return readLine("Enter Subject: ", false); }
                }
            }
            case "ACCOUNT_SECURITY" -> {
                System.out.println();
                System.out.println("[1] Password Reset");
                System.out.println("[2] Multi-Factor Auth (MFA)");
                System.out.println("[3] Phishing & Scams");
                System.out.println("[4] Access Permissions");
                System.out.println("[5] Others (specify)");
                System.out.println();
                int choice = readInt("choice: ", 1, 5);
                switch (choice) {
                    case 1 -> { return "Password Reset"; }
                    case 2 -> { return "Multi-Factor Auth (MFA)"; }
                    case 3 -> { return "Phishing & Scams"; }
                    case 4 -> { return "Access Permissions"; }
                    case 5 -> { return readLine("Enter Subject: ", false); }
                }
            }
        }
        return readLine("Enter Subject: ", false);
    }

    protected String readState() { 
        while(true) {
            System.out.println("");
            System.out.println("[1] OPEN");
            System.out.println("[2] IN-PROGRESS");
            System.out.println("[3] RESOLVED");
            System.out.println("[4] CLOSED");
            System.out.println("[5] CANCELLED");
            System.out.println("");
            int choice = readInt("choice: ", 1, 5);
            switch (choice) {
                case 1 -> { return "OPEN"; }
                case 2 -> { return "IN_PROGRESS"; }
                case 3 -> { return "RESOLVED"; }
                case 4 -> { return "RESOLVED"; }
                case 5 -> { return "CANCELLED"; }
            }
        }
    }

    protected String readRole() {
        while(true) {
            System.out.println("");
            System.out.println("[1] REQUESTOR");
            System.out.println("[2] SUPPORTAGENT");
            System.out.println("[3] ADMIN");
            System.out.println("");
            int choice = readInt("choice: ", 1, 3);
            switch (choice) {
                case 1 -> { return "REQUESTOR"; }
                case 2 -> { return "SUPPORTAGENT"; }
                case 3 -> { return "ADMIN"; }
            }
        }
    }

    protected abstract void displayMenu();
    protected abstract void executeChoice(int choice);
    protected abstract int getMinChoice();
    protected abstract int getMaxChoice();
    
}