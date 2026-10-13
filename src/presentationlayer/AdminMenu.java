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
