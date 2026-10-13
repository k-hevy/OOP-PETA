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
        System.err.println("SUPPORTAGENT MENU");
    }

    @Override
    protected void executeChoice(int choice) {
        
    }

    @Override
    protected int getMinChoice() {
        return -1;
    }

    @Override
    protected int getMaxChoice() {
        return -1;
    }

}
