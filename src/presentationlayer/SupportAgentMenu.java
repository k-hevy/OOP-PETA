package presentationlayer;

import datalayer.Repository;
import entitylayer.SupportAgent;
import java.util.Scanner;

public class SupportAgentMenu extends BaseMenu {
    SupportAgent supportAgent;

    public SupportAgentMenu(SupportAgent supportAgent, Scanner input, Repository repository) {
        super(supportAgent, input, repository);
        this.supportAgent = supportAgent;
    }

    @Override
    protected void displayMenu() {
        System.out.println("SUPPORTAGENT MENU");
        System.out.println("[1] View Assigned Tickets");
        System.out.println("[2] View Ticket Details");
        System.out.println("[3] Update Ticket Status");
        System.out.println("[4] Change Priority");
        System.out.println("[5] Add Comment");
        System.out.println("[6] View My Resolved Count");
        System.out.println("[0] Logout");
    }

    @Override
    protected void executeChoice(int choice) {
        
    }

    @Override
    protected int getMinChoice() {
        return 0;
    }

    @Override
    protected int getMaxChoice() {
        return 6;
    }

}
