package presentationlayer;

import datalayer.Repository;
import entitylayer.SupportAgent;
import entitylayer.Ticket;
import java.util.ArrayList;
import java.util.Scanner;

public class SupportAgentMenu extends BaseMenu {

    // It provides the menu for support agents, it allows them to view their assigned tickets, 
    // check ticket details, update ticket status and priority, and add comments.
    // Throws early for user experience
    // Uses try-catchs and confirmation for actions
    
    private final SupportAgent supportAgent;

    // Constructs a SupportAgentMenu instance
    public SupportAgentMenu(SupportAgent supportAgent, Scanner input, Repository repository) {
        super(supportAgent, input, repository);
        this.supportAgent = supportAgent;
    }

    // Displays the support agent menu options
    @Override
    protected void displayMenu() {
        printHeader("SUPPORTAGENT MENU");
        System.out.println("[1] View Assigned Tickets");
        System.out.println("[2] View Ticket Details");
        System.out.println("[3] Update Ticket Status");
        System.out.println("[4] Update Ticket Priority");
        System.out.println("[5] Add Comment");
        System.out.println("[0] Logout");
    }

    // Returns minimum menu choice
    @Override
    protected int getMinChoice() {
        return 0;
    }

    // Returns maximum menu choice
    @Override
    protected int getMaxChoice() {
        return 5;
    }

    // Executes the selected support agent menu choice
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

    // Handles viewing assigned tickets
    public void viewAssignedTickets() {
        while (true) {
            ArrayList<Ticket> tickets = supportAgent.getTickets();
            if (tickets.isEmpty()) {
                System.out.println("No assigned tickets found.");
                waitForEnter();
                return;
            }

            printHeader("View Tickets");
            System.out.println("[1] View all AssignedTickets");
            System.out.println("[2] View all Active Tickets");
            System.out.println("[0] Back");
            System.out.println();
            int subChoice = readInt("Enter chocie: ", 0, 2);

            switch (subChoice) { 
                case 1 ->  { displayTickets(tickets); }
                case 2 ->  { displayTickets(supportAgent.getActiveTickets()); }
                case 0 ->  { System.out.println("Returning to previous menu..."); return;} // returns to the previous menu
            }
            waitForEnter();
        }
    }

    // Handles viewing detailed information for a specific ticket
    public void viewTicketDetails() {
        printHeader("View Ticket Details");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = supportAgent.getTicket(ticketId);
            displayTicket(ticket);
        } catch (IllegalArgumentException | SecurityException e) {
            printError(e.getMessage());
        }
        waitForEnter();
    }

    // Handles updating a ticket's state 
    public void updateTicketState() {
        printHeader("Update Ticket Status");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = supportAgent.getTicket(ticketId);
            ticket.requireNotFinished();
            String oldState = ticket.getState();
            String newState = readAgentState();
            ticket.checkWorkFlowMoves(newState);
            if (confirm("Change ticket state from " + oldState + " to " + newState + "?")) {
                String reason = readLine("Enter a reason: ", false);
                supportAgent.updateTicketState(ticketId, newState, reason);
                System.out.println("Ticket state changed successful.");
            } else {
                System.out.println("Ticket state change Cancelled.");
            }
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
            printError(e.getMessage());
        }
        waitForEnter();
    }

    // Handles updating a ticket's priority 
    public void updateTicketPriority() {
        printHeader("Update Ticket Priority");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = supportAgent.getTicket(ticketId);
            ticket.requireActive();
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
            printError(e.getMessage());
        }
        waitForEnter();
    }

    // Handles adding a comment to an assigned ticket
    public void addComment() {
        printHeader("Add Comment");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = supportAgent.getTicket(ticketId);
            ticket.requireNotFinished();
            String message = readLine("Enter comment: ", false);
            supportAgent.addComment(ticketId, message);
            System.out.println("Success! Comment added to " + ticket.getTicketId() + ".");
        } catch (IllegalArgumentException | SecurityException | IllegalStateException e) {
           printError(e.getMessage());
        }
        waitForEnter();
    }

    // Prompts the agent to select an allowed ticket state transition
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
