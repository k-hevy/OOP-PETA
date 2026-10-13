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
        System.err.println("[1] View My Tickets ");
    }

    @Override
    protected void executeChoice(int choice) {
        switch (choice) {
            case 1 -> displayMyTickets();
        }
    }

    @Override
    protected int getMinChoice() {
        return 1;
    }

    @Override
    protected int getMaxChoice() {
        return 1;
    }

    public void displayMyTickets() {

        System.out.printf("%-10s | %-24s | %-10s | %-15s | %-18s | %s%n",
        "ID", "Category", "Priority", "State", "Agent", "Description"
        );
        System.out.println("----------------------------------------------------------------------------------------------------------------");

        for (Ticket ticket : requestor.getMyTickets()) {
        String agentName = (ticket.getAssignedAgent() != null) ? ticket.getAssignedAgent().getFullName() : "Unassigned";
            System.out.printf("%-10s | %-24s | %-10s | %-15s | %-18s | %s%n",
                ticket.getTicketId(),
                truncate(ticket.getCategory(), 24),   // Safe truncation matching width 24
                ticket.getPriority(),
                ticket.getState(),
                truncate(agentName, 18),              // Safe truncation matching width 18
                ticket.getDescription()
            );
        }
        System.out.println();
        
    }
    
}
