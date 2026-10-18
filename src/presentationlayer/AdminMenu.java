package presentationlayer;

import datalayer.Repository;
import entitylayer.Admin;
import entitylayer.Comment;
import entitylayer.SupportAgent;
import entitylayer.Ticket;
import entitylayer.TicketLogEntry;
import entitylayer.User;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Scanner;


public class AdminMenu extends BaseMenu {

    private final Admin admin;

    public AdminMenu(Admin admin, Scanner input, Repository repository) {
        super(admin, input, repository);
        this.admin = admin;
    }

    @Override
    protected void displayMenu() {
        System.out.println("---- ADMIN MENU ----");
        System.out.println("[1] View All Users");
        System.out.println("[2] View All Tickets");
        System.out.println("[3] View Ticket Details");
        System.out.println("[4] Reassign Ticket");
        System.out.println("[5] Reject Ticket");
        System.out.println("[6] Generate Summary Report");
        System.out.println("[0] Logout");
    }

    @Override
    protected void executeChoice(int choice) {
        switch(choice) {
            case 1 -> { viewAllUsers(); }
            case 2 -> { viewAllTickets(); }
            case 3 -> { viewTicketDetails(); }
            case 4 -> { reassignTicket(); }
            case 5 -> { rejectTicket();}
            case 6 -> { generateSummaryReport(); }
            case 0 -> { System.out.println("Logging out. Goodbye!"); stop(); }
        }
    }

     @Override
    protected int getMinChoice() {
        return 0;
    }

    @Override
    protected int getMaxChoice() {
        return 6;
    }

    public void viewAllUsers() {
        while (true) {
            System.out.println("\n====== View All Users ======");
            System.out.println("[1] All users");
            System.out.println("[2] Filter by ID");
            System.out.println("[3] Filter by Role (REQUESTOR, SUPPORTAGENT, ADMIN)");
            System.out.println("[4] Filter by Full Name");
            System.out.println("[5] Filter by Specialization");
            System.out.println("[0] Back");
            System.out.println();
            int choice = readInt("Choice: ", 0, 5);

            if (choice == 0) { System.out.println("Returning to previous menu..."); return;}
            ArrayList<User> users;
            if (choice == 1) { users = admin.getAllUsers(); } else {
                String field = switch(choice) {
                    case 2  ->  "ID";
                    case 3  ->  "ROLE"; 
                    case 4  ->  "NAME";
                    default -> "SPECIALIZATION"; 
                };
                String value = switch (field) {
                    case "ROLE" -> readRole();
                    case "SPECIALIZATION" -> readCategory();
                    case "ID", "NAME" -> readLine("Enter " + field.toLowerCase() + " to match: ", false);
                    default -> throw new IllegalArgumentException("Unsupported filter field: " + field);
                };
                 users = admin.filterUsers(field, value);
            }
            displaySummarizedUser(users);
            waitForEnter();
        }
    }

    public void viewAllTickets() {
        while (true) {
            System.out.println("\n====== View All Tickets ======");
            System.out.println("[1] All tickets");
            System.out.println("[2] Filter by State (OPEN, IN_PROGRESS, RESOLVED, CLOSED, CANCELLED)");
            System.out.println("[3] Filter by Category");
            System.out.println("[4] Filter by Priority (URGENT, HIGH, MEDIUM, LOW)");
            System.out.println("[5] Filter by Requestor ID");
            System.out.println("[6] Filter by Agent ID");
            System.out.println("[0] Back");
            System.out.println();
            int choice = readInt("Choice: ", 0, 6);

            if (choice == 0) { System.out.println("Returning to previous menu..."); return;}
            ArrayList<Ticket> tickets;
            if (choice == 1) { tickets = admin.getAllTickets(); } else {
                String field = switch (choice) {
                    case 2  -> "STATE";
                    case 3  -> "CATEGORY";
                    case 4  -> "PRIORITY";
                    case 5  -> "USER";
                    default -> "AGENT";
                };
                String value = switch(field) {
                    case "STATE" -> readState();
                    case "CATEGORY" -> readCategory();
                    case "PRIORITY" -> readPriority();
                    case "USER", "AGENT" -> readLine("Enter " + field.toLowerCase() + " to match: ", false);
                    default -> throw new IllegalArgumentException("Unsupported filter field: " + field);
                };
                tickets = admin.filterTickets(field, value);
            }
            displaySummarizedTickets(tickets);
            waitForEnter();
        }
    }

    public void viewTicketDetails() {
        System.out.println("\n====== View Ticket Details ======");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = admin.getTicket(ticketId);
            displayDetailedTicket(ticket);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    public void reassignTicket() {
        System.out.println("\n====== Reassign a Ticket ======");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = admin.getTicket(ticketId);
            String agentId = readLine("Enter SupportAgent Id: ", false);
            if (confirm("Reassign Ticket " + ticket.getTicketId() + " to agent " + agentId + "?")) {
                String reason = readLine("Provide a reason for reassignment: ", false);
                admin.reassignTicket(ticketId, agentId, reason);
                System.out.println("Success! Ticket " + ticket.getTicketId() + " has been reassigned.");
            } else {
                 System.out.println("Reassignment Aborted");
            }
        } catch (IllegalArgumentException | IllegalStateException  e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    public void rejectTicket() {
        System.out.println("\n====== Reject a Ticket ======");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = admin.getTicket(ticketId);
            if (confirm("Reject Ticket " + ticket.getTicketId() + "?")) {
                String reason = readLine("Provide a reason for Rejection: ", false);
                admin.rejectTicket(ticketId, reason);
                System.out.println("Success! Ticket " + ticket.getTicketId() + " has been rejected.");
            } else {
                 System.out.println("Rejection Aborted");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    public void generateSummaryReport() {
        System.out.println("\n====== Summary Report ======");
        LinkedHashMap<String, Integer> counts = admin.getSummaryCounts();
        String titles[] = {"Overview", "By State", "By Priority", "By Category"};
        String groups[][] = {
            {"totalTickets", "totalUsers", "UnassignedTickets"},
            {"OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED", "CANCELLED"},
            {"URGENT", "HIGH", "MEDIUM", "LOW"},
            {"FACILITIES_MAINTENANCE", "SOFTWARE_SYSTEMS", "NETWORK", "HARDWARE", "ACCOUNT_SECURITY"}
        };
        for(int i = 0; i < titles.length; i++) {
            System.out.println("----" + titles[i] + "----");
            for (String key : groups[i]) {
                System.out.printf("%-24s : %d%n", key, counts.get(key));
            }
            System.out.println();
        }
        waitForEnter();
    }

    private void displayDetailedTicket(Ticket t) {
        String requestorName = (t.getSubmittedBy() != null) ? t.getSubmittedBy().getFullName() : "Unknown";
        String agentName = (t.getAssignedAgent() != null) ? t.getAssignedAgent().getFullName() : "Unassigned";

        System.out.println("===== TICKET " + t.getTicketId() + " =====");
        System.out.printf("%-13s : %s%n", "Requestor", requestorName);
        System.out.printf("%-13s : %s%n", "Agent", agentName);
        System.out.printf("%-13s : %s%n", "Category", t.getCategory());
        System.out.printf("%-13s : %s%n", "Subject", t.getSubject());
        System.out.printf("%-13s : %s%n", "Priority", t.getPriority());
        System.out.printf("%-13s : %s%n", "State", t.getState());
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
            System.out.printf("[%s] %-30s by %-12s | %s%n",
                log.getTime(),
                log.getPreviousState() + " -> " + log.getNewState(),
                log.getChangedByUserName(),
                log.getReason()
            );
        }
        System.out.println(LINE);
    }

    private void displaySummarizedTickets(ArrayList<Ticket> tickets) {
        if (tickets.isEmpty()) {
            System.out.println("No tickets found.");
            return;
        }

        System.out.printf("%-10s | %-15s | %-15s | %-15s | %-20s | %-10s | %-12s | %s%n", "ID", "Requestor", "Agent", "Category", "Subject", "Priority", "State", "Description");
        System.out.println("-".repeat(148));

        for (Ticket t : tickets) {
            String requestorName = (t.getSubmittedBy() != null) ? t.getSubmittedBy().getFullName() : "Unknown";
            String agentName = (t.getAssignedAgent() != null) ? t.getAssignedAgent().getFullName() : "Unassigned";
            System.out.printf("%-10s | %-15s | %-15s | %-15s | %-20s | %-10s | %-12s | %s%n",
                truncate(t.getTicketId(), 10),
                truncate(requestorName, 15),
                truncate(agentName, 15),
                truncate(t.getCategory(), 15),
                truncate(t.getSubject(), 20),
                truncate(t.getPriority(), 10),
                truncate(t.getState(), 12),
                truncate(t.getDescription(), 30)
            );
        }
        System.out.println();
    }

    private void displaySummarizedUser(ArrayList<User> users) {
         if (users.isEmpty()) {
            System.out.println("No users found.");
            return;
        }
        System.out.printf("%-10s | %-13s | %-20s | %s%n", "ID", "Role", "Name", "Specialization");
        System.out.println("-".repeat(72));
        for (User u : users) {
            if (u == null) {
                continue;
            }
            String specialization = (u instanceof SupportAgent agent) ? agent.getSpecialization() : "-";
            System.out.printf("%-10s | %-13s | %-20s | %s%n",
                truncate(u.getUserId(), 10),
                truncate(u.getRole(), 13),
                truncate(u.getFullName(), 20),
                truncate(specialization, 25)
            );
        }
        System.out.println();
    }
}
