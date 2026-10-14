package presentationlayer;

import datalayer.Repository;
import entitylayer.Admin;
import java.util.Scanner;


public class AdminMenu extends BaseMenu {

    Admin admin;

    public AdminMenu(Admin admin, Scanner input, Repository repository) {
        super(admin, input, repository);
        this.admin = admin;
    }

    @Override
    protected void displayMenu() {
        System.out.println("ADMIN MENU");
        System.out.println("[1] View All Users (with filters)");
        System.out.println("[2] View All Tickets (with filters)");
        System.out.println("[3] View Ticket Details");
        System.out.println("[4] Reassign Ticket");
        System.out.println("[5] Reject Ticket");
        System.out.println("[6] Summary Report");
        System.out.println("[0]  Logout");
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
