package presentationlayer;

// import java.util.Scanner;
// import entitylayer.SupportAgent;
import datalayer.Repository;
import entitylayer.Comment;
import entitylayer.SupportAgent;
import entitylayer.Ticket;
import entitylayer.TicketLogEntry;
import java.util.ArrayList;
import java.util.Scanner;

public class SupportAgentMenu extends BaseMenu {

    private final SupportAgent supportAgent;

    public SupportAgentMenu(SupportAgent supportAgent, Scanner input, Repository repository) {
        super(supportAgent, input, repository);
        this.supportAgent = supportAgent;
    }

    @Override
    protected void displayMenu() {
        System.out.println("---- SUPPORTAGENT MENU ----");
        System.out.println("[1] View Assigned Tickets");
        System.out.println("[2] View Ticket Details");
        System.out.println("[3] Update Ticket Status");
        System.out.println("[4] Update Ticket Priority");
        System.out.println("[5] Add Comment");
        System.out.println("[0] Logout");
    }

    @Override
    protected int getMinChoice() {
        return 0;
    }

    @Override
    protected int getMaxChoice() {
        return 5;
    }

    @Override
    protected void executeChoice(int choice) {
        switch (choice) {
            case 1 -> { viewAssignedTickets(); }
            case 2 -> { viewTicketDetails(); } 
            case 3 -> { updateTicketState(); } 
            case 4 -> { updateTicketPriority(); }
            case 5 -> { addComment(); }
            case 0 -> { System.out.println("Logging out. Goodbye!"); stop();}
        }
    }

    public void viewAssignedTickets() {
        while (true) {
            ArrayList<Ticket> tickets = supportAgent.getAssignedTickets();
            if (tickets.isEmpty()) {
                System.out.println("No assigned tickets found.");
                waitForEnter();
                return;
            }

            System.out.println("\n===== View Tickets =====");
            System.out.println("[1] View all AssignedTickets");
            System.out.println("[2] View all Active Tickets");
            System.out.println("[0] Back");
            System.out.println();
            int subChoice = readInt("Enter chocie: ", 0, 2);

            switch (subChoice) { 
                case 1 ->  { displaySummarizedTickets(tickets); }
                case 2 ->  { displaySummarizedTickets(supportAgent.getActiveTickets()); }
                case 0 ->  { System.out.println("Returning to previous menu..."); return;} // returns to the previous menu
            }
            waitForEnter();
        }
    }

    public void viewTicketDetails() {
        System.out.println("\n====== View Ticket Details ======");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = supportAgent.getTicket(ticketId);
            displayDetailedTicket(ticket);
        } catch (IllegalArgumentException | SecurityException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    public void updateTicketState() {
        System.out.println("\n====== Update Ticket Status ======");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = supportAgent.getTicket(ticketId);
            ticket.requireNotFinished();
            String oldState = ticket.getState();
            String newState = readAgentState();
            ticket.requireTransition(newState);
            if (confirm("Change ticket state from " + oldState + " to " + newState + "?")) {
                String reason = readLine("Enter a reason: ", false);
                supportAgent.updateTicketState(ticketId, newState, reason);
                System.out.println("Ticket state changed successful.");
            } else {
                System.out.println("Ticket state change Cancelled.");
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    public void updateTicketPriority() {
        System.out.println("\n====== Update Ticket Priority ======");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = supportAgent.getTicket(ticketId);
            ticket.requireNotFinished();
            String oldPriority = ticket.getPriority();
            String newPriority = readPriority();
            if (confirm("Change ticket priority from " + oldPriority + " to " + newPriority + "?")) {
                String reason = readLine("Enter a reason: ", false);
                supportAgent.updatePriority(ticketId, newPriority, reason);
                System.out.println("Success! Ticket priority has been changed from " + oldPriority  + " to " + newPriority);
            } else { 
                System.out.println("Ticket priority change Cancelled.");
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    public void addComment() {
        System.out.println("\n====== Add Comment ======");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = supportAgent.getTicket(ticketId);
            ticket.requireNotFinished();
            String message = readLine("Enter comment: ", false);
            supportAgent.addComment(ticketId, message);
            System.out.println("Success! Comment added to " + ticket.getTicketId() + ".");
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    private void displaySummarizedTickets(ArrayList<Ticket> tickets) {
        if (tickets.isEmpty()) {
            System.out.println("No tickets found.");
            waitForEnter();
            return;
        }
        System.out.printf("%-10s | %-15s | %-20s | %-10s | %-12s | %-15s | %s%n",
        "ID", "Category", "Subject", "Priority", "State", "Requestor", "Description");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        for (Ticket t : tickets) {
            String requestorName = t.getSubmittedBy().getLastName() + ", " + t.getSubmittedBy().getFirstName().charAt(0) + ".";
            System.out.printf("%-10s | %-15s | %-20s | %-10s | %-12s | %-15s | %s%n",
                truncate(t.getTicketId(), 10),
                truncate(t.getCategory(), 15),
                truncate(t.getSubject(), 20),
                truncate(t.getPriority(), 10),
                truncate(t.getState(), 12),
                truncate(requestorName, 15),
                truncate(t.getDescription(), 30)
            );
        }     
    }

    private void displayDetailedTicket(Ticket t) {
        String requestorName = (t.getSubmittedBy() != null) ? t.getSubmittedBy().getFullName() + " (" + t.getSubmittedBy().getRole() + ")" : "Unknown";

        System.out.println("===== TICKET " + t.getTicketId() + " =====");
        System.out.printf("%-13s : %s%n", "Requestor", requestorName);
        System.out.printf("%-13s : %s%n", "Category", t.getCategory());
        System.out.printf("%-13s : %s%n", "Subject", t.getSubject());
        System.out.printf("%-13s : %s%n", "Priority", t.getPriority());
        System.out.printf("%-13s : %s%n", "State", t.getState());
        System.out.printf("%-13s : %s%n", "Submitted", t.getDateSubmittedFormatted());
        System.out.printf("%-13s : %s%n", "Description", t.getDescription());
        System.out.println();

        System.out.println("--- Comments (" + t.getComments().size() + ") ---");
        for (Comment c : t.getComments()) {
            System.out.printf("[%s] %-20s : %s%n", c.getTime(), c.getAuthorName(), c.getMessage());
        }
        System.out.println();

        System.out.println("--- History (" + t.getTicketLogEntries().size() + ") ---");
        for (TicketLogEntry log : t.getTicketLogEntries()) {
            String modifierName = log.getChangedByUserId().equals(supportAgent.getUserId()) ? "You" : log.getChangedByUserName();
            System.out.printf("[%s] %-25s by %-12s | %s%n", log.getTime(), (log.getPreviousState() + " -> " + log.getNewState()), modifierName, log.getReason());
        }
        System.out.println(LINE);
    }

    private String readAgentState() { 
        while(true) {
            System.out.println("");
            System.out.println("[1] OPEN");
            System.out.println("[2] IN-PROGRESS");
            System.out.println("[3] RESOLVED");
            System.out.println("");
            int choice = readInt("choice: ", 1, 3);
            switch (choice) {
                case 1 -> { return "OPEN"; }
                case 2 -> { return "IN_PROGRESS"; }
                case 3 -> { return "RESOLVED"; }
            }
        }
    }

}
