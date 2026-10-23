package presentationlayer;

import datalayer.Repository;
import entitylayer.Requestor;
import entitylayer.Ticket;
import java.util.ArrayList;
import java.util.Scanner;

public class RequestorMenu extends BaseMenu {

    // It provides the menu for requestors, such as students and faculty, to manage their ticket. 
    // It allows them to create tickets, view their submitted tickets, add comments, delete active tickets, reopen resolved tickets and close resolved tickets. 
    // Throws early for user experience
    // Uses try-catchs and confirmation for actions

    private final Requestor requestor;

    // // Constructs a RequestorMenu instance
    public RequestorMenu(Requestor requestor, Scanner input, Repository repository) {
        super(requestor, input, repository);
        this.requestor = requestor;
    }

    // Displays the requestor menu options
    @Override
    protected void displayMenu() {
        printHeader("REQUESTOR MENU");
        System.out.println("[1] Create Ticket");
        System.out.println("[2] View My Tickets");
        System.out.println("[3] View Detailed Ticket");
        System.out.println("[4] Add Comment To Ticket");
        System.out.println("[5] Delete Ticket");
        System.out.println("[6] Reopen Ticket");
        System.out.println("[7] Close Ticket");
        System.out.println("[0] Logout");
    }
    
    // Executes the selected requestor menu choice
    @Override
    protected void executeChoice(int choice) {
        switch (choice) {
            case 1 -> { createTicket(); }
            case 2 -> { viewMyTickets(); } 
            case 3 -> { viewTicketDetails(); } 
            case 4 -> { addComment(); } 
            case 5 -> { deleteTicket(); } 
            case 6 -> { reopenTicket(); } 
            case 7 -> { closeTicket(); } 
            case 0 -> { printHeader("Logging out. Goodbye!"); stop(); } // Ends the console loop after displaying the logout message.
        }
    }

    // Returns minimum menu choice 
    @Override
    protected int getMinChoice() {
        return 0;
    } 

    // Returns maximum menu choice
    @Override
    protected int getMaxChoice() {
        return 7;
    }

    // Handles submitting a new support ticket
    public void createTicket() {
        printHeader("Submit a New Ticket");

        String priority = readPriority();
        String category = readCategory();
        String subject = readSubject(category);
        String description = readLine("Enter Description: ", false);

        try {
            if (confirm("Submit this ticket?")) {
                Ticket newTicket = requestor.createTicket(priority, category, subject, description);
                requestor.submitTicket(newTicket);
                System.out.println("");
                displayTicket(newTicket);
                System.out.println("");
                printSuccess("Ticket submitted successfully!");
                System.out.println();
            } else {
                System.out.println("Cancelled submitting ticket.");
            } 
        } catch (IllegalArgumentException e) {
            printError(e.getMessage());
        }

        waitForEnter();
    } 

    // Handles viewing submitted tickets
    public void viewMyTickets() {
        while (true) {
            ArrayList<Ticket> tickets = requestor.getTickets();
            if (tickets.isEmpty()) {
                System.out.println("No submitted tickets found.");
                waitForEnter();
                return;
            }

            printHeader("View Submitted Tickety");
            System.out.println("[1] View all Tickets");
            System.out.println("[2] View all Active Tickets");
            System.out.println("[0] Back");
            System.out.println();
            int subChoice = readInt("Enter Choice: ", 0, 2);

            switch (subChoice) { 
                case 1 ->  { displayTickets(tickets); }
                case 2 ->  { displayTickets(requestor.getActiveTickets()); }
                case 0 ->  { System.out.println("Returning to previous menu..."); return;} // returns to the previous menu
            }
            waitForEnter();
        }
    }

    // Handles viewing detailed information for a specific ticket
    public void viewTicketDetails() {
        printHeader("View Ticket details ");

        String ticketId = readLine("Enter Ticket ID to view details: ", false);
        try {
            Ticket ticket = requestor.getTicket(ticketId); 
            displayTicket(ticket);
        } catch (IllegalArgumentException | SecurityException e) {
            printError(e.getMessage());
        }
        waitForEnter();
    }

    // Handles adding a comment to a ticket
    public void addComment() {
        printHeader("Add comment to Ticket");
        String ticketId = readLine("Enter ticket iD: ", false);

        try {
            Ticket ticket = requestor.getTicket(ticketId);
            ticket.requireNotFinished();
            String text = readLine("Enter comment: ", false);
            if (confirm("Are you sure you to submit this comment?")) {
                requestor.addComment(ticketId, text);
                System.out.println("Success! Comment added to " + ticketId.toUpperCase());
            } else {
                System.out.println("Commenting Cancelled");
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            printError(e.getMessage());
        }
        waitForEnter();
    }

    // Handles deleting an active ticket
    public void deleteTicket() {
        printHeader("Delete a Ticket");
        
        String ticketId = readLine("Enter ticket iD: ", false);
        try {
            Ticket ticket = requestor.getTicket(ticketId);
            ticket.requireActive();
            if(confirm("Are you sure you want to Delete ticket " + ticket.getTicketId() + "?")) {
                String reason = readLine("Please provide a reason for Deletion: ", false);
                requestor.deleteTicket(ticketId, reason);
                System.out.println("Success! Ticket " + ticket.getTicketId() + " has successfully been Deleted.");
            } else {
                System.out.println("Deletion cancelled");
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            printError(e.getMessage());
        }
        waitForEnter();
    }

    // Handles reopening a resolved ticket
    public void reopenTicket() {
        printHeader("Re-open a Ticket");

        String ticketId = readLine("Enter ticket iD: ", false);
        try {
            Ticket ticket = requestor.getTicket(ticketId);
            ticket.requireResolved(); 
            if (confirm("Are you sure you want to Re-open ticket " + ticket.getTicketId() + "?")) {
                String reason = readLine("Please provide a reason for reopening: ", false);
                requestor.reopenTicket(ticketId, reason);
                System.out.println("Success! Ticket " + ticket.getTicketId() + " has successfully been re-opened.");
            } else {
                System.out.println("Reopening Cancelled");
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            printError(e.getMessage());
        }
        waitForEnter();
    }
    
    // Handles closing a resolved ticket
    public void closeTicket() {
        printHeader("Close a Ticket");

        String ticketId = readLine("Enter ticket iD: ", false);
        try {
            Ticket t = requestor.getTicket(ticketId);
            t.requireResolved();
            t.requireNotDeleted();
            if(confirm("Are you sure you want to Close ticket " + t.getTicketId() + "?")) {
                String reason = readLine("Please provide a reason for Closing: ", false);
                requestor.closeTicket(ticketId, reason);
                System.out.println("Success! Ticket " + t.getTicketId() + " has successfully been Closed.");
            } else {
                System.out.println("Ticket closing Cancelled");
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            printError(e.getMessage());
        }
        waitForEnter();
    }
    
}