package presentationlayer;

import datalayer.Repository;
import entitylayer.Requestor;
import entitylayer.Ticket;
import java.util.Scanner;

public class RequestorMenu extends BaseMenu {
    Requestor requestor;

    public RequestorMenu(Requestor requestor, Scanner input, Repository repository) {
        super(requestor, input, repository);
        this.requestor = requestor;
    }

    @Override
    protected void displayMenu() {
        System.out.println("REQUESTOR MENU");
        System.out.println("[1] Submit Ticket");
        System.out.println("[2] View My Tickets");
        System.out.println("[3] View Ticket Details");
        System.out.println("[4] Add Comment");
        System.out.println("[5] Cancel Ticket");
        System.out.println("[6] Reopen Ticket");
        System.out.println("[7] Close Ticket");
        System.out.println("[0] Logout");
    }

    @Override
    protected void executeChoice(int choice) {
        switch (choice) {
            case 2 -> displayMyTickets();
            case 0 -> stop();
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

    public void displayMyTickets() {

        System.out.printf("%-10s | %-24s | %-15s | %-10s | %-15s | %-18s | %s%n",
        "ID", "Category", "Subject", "Priority", "State", "Agent", "Description");
        System.out.println("----------------------------------------------------------------------------------------------------------------");

        for (Ticket ticket : requestor.getMyTickets()) {
        String agentName = (ticket.getAssignedAgent() != null) ? ticket.getAssignedAgent().getFullName() : "Unassigned";
            System.out.printf("%-10s | %-24s | %-15s | %-10s | %-15s | %-18s | %s%n",
                ticket.getTicketId(),
                truncate(ticket.getCategory(), 24),   // Safe truncation matching width 24
                truncate(ticket.getSubject(), 24),
                ticket.getPriority(),
                ticket.getState(),
                truncate(agentName, 18),              // Safe truncation matching width 18
                ticket.getDescription()
            );
        }
        System.out.println();
        
    }
    
}
