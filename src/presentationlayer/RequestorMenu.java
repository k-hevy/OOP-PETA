package presentationlayer;

import datalayer.Repository;
import entitylayer.Comment;
import entitylayer.Requestor;
import entitylayer.Ticket;
import entitylayer.TicketLogEntry;
import java.util.ArrayList;
import java.util.Scanner;

public class RequestorMenu extends BaseMenu {
    private final Requestor requestor;

    public RequestorMenu(Requestor requestor, Scanner input, Repository repository) {
        super(requestor, input, repository);
        this.requestor = requestor;
    }

    @Override
    protected void displayMenu() {
        System.out.println("REQUESTOR MENU");
        System.out.println("[1] Create Ticket");
        System.out.println("[2] View My Tickets");
        System.out.println("[3] View Detailed Ticket");
        System.out.println("[4] Add Comment To Ticket");
        System.out.println("[5] Cancel Ticket");
        System.out.println("[6] Reopen Ticket");
        System.out.println("[7] Close Ticket");
        System.out.println("[0] Logout");
    }
    
    @Override
    protected void executeChoice(int choice) {
        switch (choice) {
            case 1 -> { createTicket(); }
            case 2 -> { viewMyTickets(); } 
            case 3 -> { displayTicketDetails(); } 
            case 4 -> { addCommentToTicket(); } 
            case 5 -> { cancelTicket(); } 
            case 6 -> { reopenTicket(); } 
            case 7 -> { closeTicket(); } 
            case 0 -> { System.out.println("Logging out. Goodbye!"); stop(); } // Ends the console loop after displaying the logout message.
        }
    }

    @Override
    protected int getMinChoice() {
        return 0;
    } 

    @Override
    protected int getMaxChoice() {
        return 7;
    }

    public void createTicket() {
        System.out.println("\n====== Submit a New Ticket ======");

        String priority = readPriority();
        String category = readCategory();
        String subject = readSubject(category);
        String description = readLine("Enter Description: ", false);

        try {
            if (confirm("Submit this ticket?")) {
                Ticket newTicket = requestor.createTicket(priority, category, subject, description);
                requestor.submitTicket(newTicket);
                displayDetailedTicket(newTicket);
                System.out.println("Ticket submitted successfully!");
            } else {
                System.out.println("Cancelled submitting ticket.");
            } 
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }

        waitForEnter();
    } 

    public void viewMyTickets() {
        System.out.println("\n====== View Submitted Tickets ======");

        while (true) {
            ArrayList<Ticket> tickets = requestor.getMyTickets();
            if (tickets.isEmpty()) {
                System.out.println("No submitted tickets found.");
                waitForEnter();
                return;
            }

            System.out.println("\n===== View Tickets =====");
            System.out.println("[1] View all Tickets");
            System.out.println("[2] View all Active Tickets");
            System.out.println("[0] Back");
            System.out.println();
            int subChoice = readInt("Enter chocie: ", 0, 2);

            switch (subChoice) { 
                case 1 ->  { displaySummarizedTickets(tickets); }
                case 2 ->  { displaySummarizedTickets(requestor.getActiveTickets()); }
                case 0 ->  { System.out.println("Returning to previous menu..."); return;} // returns to the previous menu
            }
            waitForEnter();
        }
    }

    public void displayTicketDetails() {
        System.out.println("\n====== View Ticket details ======");

        String ticketId = readLine("Enter Ticket ID to view details: ", false);
        try {
            Ticket ticket = requestor.getTicket(ticketId); 
            displayDetailedTicket(ticket);
        } catch (IllegalArgumentException | SecurityException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    public void addCommentToTicket() {
        System.out.println("\n====== Add comment to Ticket ======");

        String ticketId = readLine("Enter ticket iD: ", false);

        try {
            requestor.getTicket(ticketId);
            String text = readLine("Enter comment: ", false);
            if (confirm("Are you sure you top submit this comment?")) {
                requestor.addComment(ticketId, text);
                System.out.println("Success! Comment added to " + ticketId.toUpperCase());
            } else {
                System.out.println("Commenting Cancelled");
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    public void cancelTicket() {
        System.out.println("\n====== Cancel a Ticket ======");
        String ticketId = readLine("Enter ticket iD: ", false);
        try {
            Ticket t = requestor.getTicket(ticketId);
            if(confirm("Are you sure you want to Cancel ticket " + t.getTicketId() + "?")) {
                String reason = readLine("Please provide a reason for cancellation: ", false);
                requestor.cancelTicket(ticketId, reason);
                System.out.println("Success! Ticket " + t.getTicketId() + " has successfully been cancelled.");
            } else {
                System.out.println("Cancellation aborted");
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    public void reopenTicket() {
        System.out.println("\n====== Re-open a Ticket ======");

        String ticketId = readLine("Enter ticket iD: ", false);
        try {
            Ticket t = requestor.getTicket(ticketId);
            if (confirm("Are you sure you want to Re-open ticket " + t.getTicketId() + "?")) {
                String reason = readLine("Please provide a reason for reopening: ", false);
                requestor.reopenTicket(ticketId, reason);
                System.out.println("Success! Ticket " + t.getTicketId() + " has successfully been re-opened.");
            } else {
                System.out.println("Reopening aborted");
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }
    
    public void closeTicket() {
        System.out.println("\n====== Close a Ticket ======");

        String ticketId = readLine("Enter ticket iD: ", false);
        try {
            Ticket t = requestor.getTicket(ticketId);
            if(confirm("Are you sure you want to Close ticket " + t.getTicketId() + "?")) {
                String reason = readLine("Please provide a reason for Closing: ", false);
                requestor.closeTicket(ticketId, reason);
                System.out.println("Success! Ticket " + t.getTicketId() + " has successfully been Closed.");
            } else {
                System.out.println("Ticket closing aborted");
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }
    

    private void displayDetailedTicket(Ticket t) {
        String agentName = (t.getAssignedAgent() != null) ? t.getAssignedAgent().getFullName() : "Unassigned";

        System.out.println("===== TICKET " + t.getTicketId() + " =====");
        System.out.printf("%-13s : %s%n", "Category", t.getCategory());
        System.out.printf("%-13s : %s%n", "Subject", t.getSubject());
        System.out.printf("%-13s : %s%n", "Priority", t.getPriority());
        System.out.printf("%-13s : %s%n", "State", t.getState());
        System.out.printf("%-13s : %s%n", "Agent", agentName);
        System.out.printf("%-13s : %s%n", "Submitted", t.getDateSubmitted());
        System.out.printf("%-13s : %s%n", "Description", t.getDescription());
        System.out.println();

        System.out.println("--- Comments (" + t.getComments().size() + ") ---");
        for (Comment c : t.getComments()) {
            System.out.printf("[%s] %-20s : %s%n", c.getTime(), c.getAuthorName(), c.getMessage());
        }
        System.out.println();

        System.out.println("--- History (" + t.getTicketLogEntries().size() + ") ---");
        for (TicketLogEntry log : t.getTicketLogEntries()) {
        System.out.printf("[%s] %-19s | %s%n", log.getTime(), (log.getPreviousState() + " -> " + log.getNewState()),  log.getReason() );

        }
        System.out.println(LINE);
    }

    private void displaySummarizedTickets(ArrayList<Ticket> tickets) {
        if (tickets.isEmpty()) {
                System.out.println("No submitted tickets found.");
                return;
        }
        System.out.printf("%-10s | %-15s | %-20s | %-10s | %-12s | %-15s | %s%n",
        "ID", "Category", "Subject", "Priority", "State", "Agent", "Description");
        System.out.println("---------------------------------------------------------------------------------------------------------");

        for (Ticket ticket : tickets) {
            String agentName = (ticket.getAssignedAgent() != null) ? ticket.getAssignedAgent().getFullName() : "Unassigned";
            System.out.printf("%-10s | %-15s | %-20s | %-10s | %-12s | %-15s | %s%n",
                truncate(ticket.getTicketId(), 10),
                truncate(ticket.getCategory(), 15),
                truncate(ticket.getSubject(), 20),
                truncate(ticket.getPriority(), 10),
                truncate(ticket.getState(), 12),
                truncate(agentName, 15),
                truncate(ticket.getDescription(), 30)
            );
        }
        System.out.println();
    }
}