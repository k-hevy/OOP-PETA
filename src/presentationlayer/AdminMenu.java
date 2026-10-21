package presentationlayer;

import datalayer.Repository;
import entitylayer.Admin;
import entitylayer.Comment;
import entitylayer.Faculty;
import entitylayer.Student;
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
        printHeader("ADMIN MENU");
        System.out.println("[1] View All Users");
        System.out.println("[2] View User Details");
        System.out.println("[3] View All Tickets");
        System.out.println("[4] View Ticket Details");
        System.out.println("[5] Reassign Ticket");
        System.out.println("[6] Delete Ticket");
        System.out.println("[7] Generate Summary Report");
        System.out.println("[0] Logout");
    }

    @Override
    protected void executeChoice(int choice) {
        switch(choice) {
            case 1 -> { viewAllUsers(); }
            case 2 -> { viewUserDetails();}
            case 3 -> { viewAllTickets(); }
            case 4 -> { viewTicketDetails(); }
            case 5 -> { reassignTicket(); }
            case 6 -> { deleteTicket();}
            case 7 -> { generateSummaryReport(); }
            case 0 -> { System.out.println("Logging out. Goodbye!"); stop(); }
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

    public void viewAllUsers() {
        while (true) {
            printHeader("View All Users");
            System.out.println("[1] All users");
            System.out.println("[2] Filter by ID");
            System.out.println("[3] Filter by Role (REQUESTOR, SUPPORTAGENT, ADMIN)");
            System.out.println("[4] Filter by Name");
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
            System.out.println();
            displayUsers(users);
            waitForEnter();
        }
    }

    public void viewUserDetails() {
        printHeader("View User Details");
        String userId = readLine("Enter User ID: ", false);
        try {
            User foundUser = admin.getUser(userId);
            viewUserDetails(foundUser);
        } catch (IllegalArgumentException e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    public void viewAllTickets() {
        while (true) {
            printHeader("View All Tickets");
            System.out.println("[1] All tickets");
            System.out.println("[2] Filter by State (OPEN, IN_PROGRESS, RESOLVED, CLOSED, DELETED)");
            System.out.println("[3] Filter by Category");
            System.out.println("[4] Filter by Priority (URGENT, HIGH, MEDIUM, LOW)");
            System.out.println("[5] Filter by Requestor ID");
            System.out.println("[6] Filter by Agent ID");
            System.out.println("[0] Back");
            System.out.println();
            int choice = readInt("Choice: ", 0, 6);

            if (choice == 0) { System.out.println("Returning to previous menu..."); return;}
            ArrayList<Ticket> tickets;
            if (choice == 1) { tickets = admin.getTickets(); } else {
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
            System.out.println();
            tickets.sort((t1, t2) -> {
                if (t1.getState().equals("DELETED") && !t2.getState().equals("DELETED")) return 1; // swap if t1 if deleted and t2 isnt
                if (!t1.getState().equals("DELETED") && t2.getState().equals("DELETED")) return -1; // dont swap t1 isnt deleted but t2 is
                return 0; 
            });
            displayTickets(tickets);
            waitForEnter();
        }
    }

    public void viewTicketDetails() {
        printHeader("View Ticket Details");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = admin.getTicket(ticketId);
            viewTicketDetails(ticket);
        } catch (IllegalArgumentException e) {
            printError(e.getMessage());
        }
        waitForEnter();
    }

    public void reassignTicket() {
        printHeader("Reassign a Ticket");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = admin.getTicket(ticketId);
            ticket.requireActive();
            String agentId = readLine("Enter SupportAgent Id: ", false);
            if (confirm("Reassign Ticket " + ticket.getTicketId() + " to agent " + agentId + "?")) {
                String reason = readLine("Provide a reason for reassignment: ", false);
                admin.reassignTicket(ticketId, agentId, reason);
                System.out.println("Success! Ticket " + ticket.getTicketId() + " has been reassigned. to " + agentId);
            } else {
                 System.out.println("Reassignment Cancelled");
            }
        } catch (IllegalArgumentException | IllegalStateException  e) {
            System.out.println("Error: " + e.getMessage());
        }
        waitForEnter();
    }

    public void deleteTicket() {
        printHeader("Delete a Ticket");
        String ticketId = readLine("Enter ticket ID: ", false);
        try {
            Ticket ticket = admin.getTicket(ticketId);
            ticket.requireNotDeleted();
            if (confirm("Delete Ticket " + ticket.getTicketId() + "?")) {
                String reason = readLine("Provide a reason for Deletion: ", false);
                admin.deleteTicket(ticketId, reason);
                System.out.println("Success! Ticket " + ticket.getTicketId() + " has been Deleted.");
            } else {
                 System.out.println("Deletion Cancelled");
            }
        } catch (IllegalArgumentException | IllegalStateException e) {
            printError(e.getMessage());
        }
        waitForEnter();
    }

    public void generateSummaryReport() {
        printHeader("Summary Report");
        LinkedHashMap<String, Integer> counts = admin.getSummaryCounts();
        String titles[] = {"Overview", "By State", "By Priority", "By Category"};
        String groups[][] = {
            {"TOTAL_TICKETS", "TOTAL_USERS", "UNASSIGNED_TICKETS"},
            {"OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED", "DELETED"},
            {"URGENT", "HIGH", "MEDIUM", "LOW"},
            {"FACILITIES_MAINTENANCE", "SOFTWARE_SYSTEMS", "NETWORK", "HARDWARE", "ACCOUNT_SECURITY"}
        };
        for(int i = 0; i < titles.length; i++) {
            System.out.println("----" + titles[i] + "----");
            for (String key : groups[i]) {
                printField(key, String.valueOf(counts.get(key)));
                
            }
            System.out.println();
        }
        waitForEnter();
    }

    private void viewTicketDetails(Ticket t) {

        System.out.println();
        String requestorName = (t.getSubmittedBy() != null) ? t.getSubmittedBy().getFullName() : "Unknown";
        String agentName = (t.getAssignedAgent() != null) ? t.getAssignedAgent().getFullName() : "Unassigned";

        System.out.println("===== TICKET " + t.getTicketId() + " =====");
        System.out.printf("%-13s : %s%n", "Requestor", requestorName);
        System.out.printf("%-13s : %s%n", "Agent", agentName);
        System.out.printf("%-13s : %s%n", "Category", t.getCategory());
        System.out.printf("%-13s : %s%n", "Subject", t.getSubject());
        System.out.printf("%-13s : %s%n", "Priority", t.getPriority());
        System.out.printf("%-13s : %s%n", "State", t.getState());
        System.out.printf("%-13s : %s%n", "Submitted", t.getDateSubmittedFormatted());
        System.out.printf("%-13s : %s%n", "Description", t.getDescription());
        System.out.println();

        System.out.println("--- Comments (" + t.getComments().size() + ") ---");
        for (Comment c : t.getComments()) {
            System.out.printf("[%s] %-20s : %s%n", formatDate(c.getTimestamp()), c.getAuthorName(), c.getMessage());
        }
        System.out.println();

        System.out.println("--- History (" + t.getTicketLogEntries().size() + ") ---");
        for (TicketLogEntry log : t.getTicketLogEntries()) {
            System.out.printf("[%s] %-30s by %-12s | %s%n",
                formatDate(log.getTimestamp()),
                log.getPreviousState() + " -> " + log.getNewState(),
                log.getChangedByUserName(),
                log.getReason()
            );
        }
        System.out.println(LINE);
    }

    private void viewUserDetails(User user) {

        System.out.println();
        System.out.println("======== User " + user.getUserId() + " ========");
        System.out.printf("%-16s : %s%n", "Full Name", user.getFullName());
        System.out.printf("%-16s : %s%n", "Email", user.getEmail());
        System.out.printf("%-16s : %s%n", "Date Registered", formatDate(user.getDateRegistered()));

        if (user instanceof Student student) {
            System.out.printf("%-16s : %s%n", "StudentId", student.getStudentId());
            System.out.printf("%-16s : %s%n", "Program", student.getProgram());
            System.out.printf("%-16s : %s%n", "Year-Level", student.getYearLevel());
            System.out.printf("%-16s : %s%n", "Section", student.getSection());
        }

        if (user instanceof Faculty faculty) {
            System.out.printf("%-16s : %s%n", "EmployeeId", faculty.getEmployeeId());
            System.out.printf("%-16s : %s%n", "Departmment", faculty.getDepartment());
            System.out.printf("%-16s : %s%n", "Position", faculty.getPosition());
            System.out.printf("%-16s : %s%n", "Desk-Extension", faculty.getDeskExtension());
        }


        if (user instanceof SupportAgent supportAgent) {
            System.out.printf("%-16s : %s%n", "EmployeeId", supportAgent.getEmployeeId());
            System.out.printf("%-16s : %s%n", "Specialization", supportAgent.getSpecialization());
            System.out.printf("%-16s : %s%n", "Desk-Extension", supportAgent.getDeskExtension());
        }

        if (user instanceof Admin adminUser) {
            System.out.printf("%-16s : %s%n", "EmployeeId", adminUser.getEmployeeId());
            System.out.printf("%-16s : %s%n", "Specialization", adminUser.getManagedDepartment());
            System.out.printf("%-16s : %s%n", "Desk-Extension", adminUser.getDeskExtension());
        }


    }

    private void displayUsers(ArrayList<User> users) {
        if (users.isEmpty()) {
            printError("No users found.");
            return;
        }
        System.out.printf("%-10s | %-13s | %-20s | %s%n ", "ID", "Role", "Name", "Email");
        System.out.println("-".repeat(90));
        for (User u : users) {
            if (u == null) {
                continue;
            }
            System.out.printf("%-10s | %-13s | %-20s | %s%n",
                truncate(u.getUserId(), 10),
                truncate(u.getRole(), 13),
                truncate(u.getFullName(), 20),
                truncate(u.getEmail(), 25)
            );
        }
        System.out.println();
    }


}
