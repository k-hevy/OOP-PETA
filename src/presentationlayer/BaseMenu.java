package presentationlayer;
import datalayer.Repository;
import entitylayer.Comment;
import entitylayer.Ticket;
import entitylayer.TicketLogEntry;
import entitylayer.User;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;

public abstract class BaseMenu {

    // Serves as an abstract parent class for the different menu in Campus Help Desk Ticketing System. 
    // It contains common methods for displaying ticket information, accepting and validating user input, formatting output, and controlling the menu loop. 

    protected User user;
    protected Scanner input;
    protected boolean running;
    protected Repository repository;
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final int WIDTH = 60;
    
    // Constructs a BaseMenu instance[cite: 1]
    protected BaseMenu(User user, Scanner input, Repository repository) {
        this.input = input;
        this.user = user;
        this.repository = repository;
        this.running = true;
    }

    // Main loop running the console menu
    public void run() {
        while (running) {
            System.out.println();
            displayMenu(); // display your menu
            System.out.println("");
            int choice = readInt("Enter your choice: ", getMinChoice(), getMaxChoice()); // prompts the user
            executeChoice(choice); // executes that choice
        }
    } 

    // Stops the menu loop
    public void stop() {
        this.running = false;
    }

    // Displays detailed information for a specific ticket
    protected void displayTicket(Ticket t) {
        String agentName = (t.getAssignedAgent() != null) ? t.getAssignedAgent().getFullName() : "Unassigned";
        String requestorName = (t.getSubmittedBy() != null) ? t.getSubmittedBy().getFullName() + " (" + t.getSubmittedBy().getRole() + ")" : "Unknown";

        printHeader(" TICKET " + t.getTicketId());
        System.out.printf("%-13s : %s%n", "Category", t.getCategory());
        System.out.printf("%-13s : %s%n", "Subject", t.getSubject());
        System.out.printf("%-13s : %s%n", "Priority", t.getPriority());
        System.out.printf("%-13s : %s%n", "State", t.getState());
        System.out.printf("%-13s : %s%n", "Requestor", requestorName);
        System.out.printf("%-13s : %s%n", "Agent", agentName);
        System.out.printf("%-13s : %s%n", "Submitted", formatDate(t.getDateSubmitted()));
        System.out.printf("%-13s : %s%n", "Description", t.getDescription());
        System.out.println();
    
        System.out.printf("%-13s : %s%n", "Comments", t.getComments().size());
        for (Comment c : t.getComments()) {
            System.out.printf("[%s] %-30s | %s%n", formatDate(c.getTimestamp()), c.getAuthorName(), c.getMessage());
        }
        System.out.println();

        System.out.printf("%-13s : %s%n", "History", t.getTicketLogEntries().size());
        for (TicketLogEntry log : t.getTicketLogEntries()) {
            System.out.printf("[%s] %-30s | %s: %s%n", formatDate(log.getTimestamp()), (log.getOldValue() + " -> " + log.getNewValue()), log.getChangedByUserId(), log.getReason() );
        }
        System.out.println();
        printDivider();
    }

    // Displays a tabular list of tickets
    protected void displayTickets(ArrayList<Ticket> tickets) {
       if (tickets.isEmpty()) {
            printError("No tickets found.");
            return;
        }

        System.out.printf("%-10s | %-15s | %-15s | %-15s | %-20s | %-10s | %-12s | %s%n", "ID", "Requestor", "Agent", "Category", "Subject", "Priority", "State", "Description");
        System.out.println("-".repeat(148));

        for (Ticket t : tickets) {
            String requestorName = (t.getSubmittedBy() != null) ? t.getSubmittedBy().getFullName() : "Unknown";
            String agentName = (t.getAssignedAgent() != null) ? t.getAssignedAgent().getFullName() : "Unassigned";
            System.out.printf("%-10s | %-15s | %-15s | %-15s | %-20s | %-10s | %-12s | %s%n",
                truncate(t.getTicketId(), 10),
                truncate(requestorName, 15),
                truncate(agentName, 15),
                truncate(t.getCategory(), 15),
                truncate(t.getSubject(), 20),
                truncate(t.getPriority(), 10),
                truncate(t.getState(), 12),
                truncate(t.getDescription(), 30)
            );
        }
        System.out.println();
    }

    // Prompts for and validates an integer input within a range
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

    // Prompts for and reads a line of text input
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

    // Truncates text to fit display limits
    protected String truncate(String text, int maxLength) {
        if (text == null) { return ""; }
        if (text.length() <= maxLength) {return text;}
        if (maxLength <= 3) { return text.substring(0, maxLength); }
        return text.substring(0, maxLength -3) + "...";
    }

    // Prompts the user for a confirmation
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

    // Pauses execution until the user presses Enter
    protected void waitForEnter() {
        System.out.print("\nPress Enter to proceed: ");
        input.nextLine();
    } 

    // Prompts the user to select a ticket priority
    protected String readPriority() {
        while(true) {
            System.out.println("");
            System.out.println("[1] LOW");
            System.out.println("[2] MEDIUM");
            System.out.println("[3] HIGH");
            System.out.println("[4] URGENT");
            System.out.println("");
            int choice = readInt("Enter Priority: ", 1, 4);
            switch (choice) {
                case 1 -> { return "LOW"; }
                case 2 -> { return "MEDIUM"; }
                case 3 -> { return "HIGH"; }
                case 4 -> { return "URGENT"; }
            }
        }
    }

    // Prompts the user to select a ticket category
    protected String readCategory() {
        while(true) {
            System.out.println("");
            System.out.println("[1] FACILITIES MAINTENANCE");
            System.out.println("[2] SOFTWARE SYSTEMS");
            System.out.println("[3] NETWORK");
            System.out.println("[4] HARDWARE");
            System.out.println("[5] ACCOUNT SECURITY"); 
            System.out.println();
            int choice = readInt("Enter Category: ", 1, 5);
            switch (choice) {
                case 1 -> { return "FACILITIES_MAINTENANCE"; }
                case 2 -> { return "SOFTWARE_SYSTEMS"; }
                case 3 -> { return "NETWORK"; }
                case 4 -> { return "HARDWARE"; }
                case 5 -> { return "ACCOUNT_SECURITY"; }
            }
        }
    }

    // Prompts the user to select or enter a ticket subject based on category
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
                int choice = readInt("Enter Subject: ", 1, 5);
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
                int choice = readInt("Enter Subject: ", 1, 5);
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
                int choice = readInt("Enter Subject: ", 1, 5);
                switch (choice) {
                    case 1 -> { return "Campus Wi-Fi Disconnect"; }
                    case 2 -> { return "Ethernet / Wired Port "; }
                    case 3 -> { return "VPN Connection"; }
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
                int choice = readInt("Enter Subject: ", 1, 5);
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
                int choice = readInt("Enter Subject: ", 1, 5);
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

    // Prompts the user to select a ticket state
    protected String readState() { 
        while(true) {
            System.out.println("");
            System.out.println("[1] OPEN");
            System.out.println("[2] IN-PROGRESS");
            System.out.println("[3] RESOLVED");
            System.out.println("[4] CLOSED");
            System.out.println("[5] DELETED");
            System.out.println("");
            int choice = readInt("Enter State: ", 1, 5);
            switch (choice) {
                case 1 -> { return "OPEN"; }
                case 2 -> { return "IN_PROGRESS"; }
                case 3 -> { return "RESOLVED"; }
                case 4 -> { return "CLOSED"; }
                case 5 -> { return "DELETED"; }
            }
        }
    }

    // Prompts the user to select a user role
    protected String readRole() {
        while(true) {
            System.out.println("");
            System.out.println("[1] REQUESTOR");
            System.out.println("[2] SUPPORTAGENT");
            System.out.println("[3] ADMIN");
            System.out.println("");
            int choice = readInt("Enter Role: ", 1, 3);
            switch (choice) {
                case 1 -> { return "REQUESTOR"; }
                case 2 -> { return "SUPPORT_AGENT"; }
                case 3 -> { return "ADMIN"; }
            }
        }
    }

    // Prints a line of repeated characters
    protected void printLine(char c) {
        System.out.println(String.valueOf(c).repeat(WIDTH));
    }

    // Prints a formatted header title
    protected void printHeader(String title) {
        System.out.println();
        printLine('=');
        System.out.println(" " + title.toUpperCase());
        printLine('=');
    }

    // Prints a visual divider line
    protected void printDivider() {
        printLine('-');
    }

    // Prints a success message
    protected void printSuccess(String message) {
        System.out.println("[OK] " + message);
    }
 
    // Prints an error message
    protected void printError(String message) {
        System.out.println("[!] Error: " + message);
    }

    // Prints a labeled field and value
    protected void printField(String label, String value) {
        System.out.printf("%-25s: %s%n", label, value);
    }

    // Formats a date-time object into a standard string
    protected String formatDate(LocalDateTime dateTime) {
        return dateTime.format(DATE_FORMAT);
    }
   
    protected abstract void displayMenu(); // Abstract method to display menu options
    protected abstract void executeChoice(int choice); // Abstract method to execute a menu choice
    protected abstract int getMinChoice(); // Abstract method to get minimum choice
    protected abstract int getMaxChoice(); // // Abstract method to get maximum choice
    
}