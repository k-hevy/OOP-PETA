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
        System.out.println("\n==============ADMIN MENU===============");
        System.out.println("[1] View All Users (with filters)");
        System.out.println("[2] View All Tickets (with filters)");
        System.out.println("[3] View Ticket Details");
        System.out.println("[4] Reassign Ticket");
        System.out.println("[5] Reject Ticket");
        System.out.println("[6] Summary Report");
        System.out.println("[0]  Logout");
        System.out.println("\n========================================");
    }

    @Override
    protected void executeChoice(int choice) {
        switch(choice) {
        case 1: 
            viewAllUsers();
            break;
        case 2: 
            viewAllTickets();
            break;
        case 3:
            viewTicketDetails();
            break;
        case 4:
            reassignTicket();
            break;
        case 5:
            rejectTicket();
            break;
        case 6:
            summaryReport();
            break;
        case 0:
            System.out.println("Logging out from Admin Menu...");
            break;
        default:
            System.out.println("Invalid Choice. Please select between 0 and 6.");
        }
    }

    @Override
    protected int getMinChoice() { return 0;}
    @Override
    protected int getMaxChoice() { return 6;}

    protected void viewAllUsers(){
        System.out.println("\nAdmin - SINGLE LINE");
        System.out.printf("%-10s | %-15s | %-15s | %-10s%n", "ID", "Username", "Role" , "Status");
        System.out.printf("%-10s | %-15s | %-15s | %-10s%n", "U-0001", "A.Reyes", "SupportAgent" , "Active");
    }
    protected void viewTicketDetails(){
        System.out.println("\nAdmin - TICKET DETAILED");
        System.out.print("Enter Ticket Id to view: ");
        String ticketId = input.nextLine().trim();

        if(ticketId.isEmpty()) { 
            System.out.println("Error Ticket Id cannot be found.");
        }

        System.out.println("\n==== TICKET "+ ticketId + "======");
        System.out.println("Requestor : Juan Dela Cruz (Student)");
        System.out.println("Agent : A. Reyes");
        System.out.println("Category : NETWORK");
        System.out.println("Subject :  Broken Aircon");
        System.out.println("Priority : HIGH");
        System.out.println("State : IN_PROGRESS");
        System.out.println("Submitted: 2026-10-12 09:14");
        System.out.println("Description : Wi-Fi in Dorm B 3rd Floor keeps dropping.");
        
        System.out.println("\n--- Comment (2) ---");
        System.out.println("[10-12 09:40] A. Reyes (Agent) : Checking the access point.");
        System.out.println("[10-12 10:05] Juan Dela Cruz (Requestor) : Still down as of 10am.");

        System.out.println("\n--- History (2) ---");
        System.out.println("[10-12 09:14] OPEN -> OPEN     by Juan Dela Cruz | Ticket Created" );
        System.out.println("[10-12 09:40] OPEN -> IN_PROGRESS    by A.Reyes | Started Work");
    }

    protected void reassignTicket(){
        System.out.println("\n--- REASSIGN TICKET ---");
        System.out.print("Enter Ticket ID: ");
        String ticketId = input.nextLine().trim();
        if(ticketId.isEmpty()) {
            System.out.println("Error: Ticket ID Cannot be empty.");
            return;
        }

        System.out.print("Enter new Support Agent: ");
        String newAgent = input.nextLine().trim();
        if(newAgent.isEmpty()) {
            System.out.println("Error: Support Agent Cannot be empty.");
            return;
        }
        System.out.print("Are you sure you want to reassign this ticket? (Y/N)");
        String confirm = input.nextLine().trim();

        if(confirm.equalsIgnoreCase("y")) {
            System.out.println("Success: Ticket " +ticketId+ "reassigned to" +newAgent+ ".");
        } else {
            System.out.println("Reassignment cancelled.");
        }
    } 

    protected void rejectTicket(){
        System.out.println("\n--- REJECT TICKET ---");
        System.out.print("Enter Ticket ID: ");
        String ticketId = input.nextLine().trim();
        if(ticketId.isEmpty()) {
            System.out.println("Error: Ticket ID Cannot be empty.");
            return;
        }

        System.out.print("Enter reason for rejection: ");
        String reason = input.nextLine().trim();
        if(reason.isEmpty()) {
            System.out.println("Error: Reason cannot be empty.");
            return;
        }
        System.out.print("Are you sure you want to cancel/reject this ticket? (Y/N)");
        String confirm = input.nextLine().trim();

        if(confirm.equalsIgnoreCase("y")) {
            System.out.println("Success: Ticket " +ticketId+ "has been rejected.");
        } else {
            System.out.println("Action cancelled.");
        }
    } 

    protected void summaryReport() {
        System.out.println("\n--- SUMMARY REPORT ---");
        System.out.printf("%-25s | %-10s%n ", "Metric", "Total");
        System.out.println("\n--------------------------------");
        System.out.printf("%-25s | %-10d%n ", "Total Tickets", 15);
        System.out.printf("%-25s | %-10d%n ", "Open Tickets", 3);
        System.out.printf("%-25s | %-10d%n ", "In Progress Tickets", 5);
        System.out.printf("%-25s | %-10d%n ", "Resolved Tickets", 5);
        System.out.printf("%-25s | %-10d%n ", "Rejected Tickets", 2);
    }
}
