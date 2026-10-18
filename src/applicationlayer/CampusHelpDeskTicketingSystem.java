package applicationlayer;

import datalayer.Repository;
import entitylayer.Admin;
import entitylayer.Faculty;
import entitylayer.Requestor;
import entitylayer.Student;
import entitylayer.SupportAgent;
import entitylayer.Ticket;
import entitylayer.User;
import java.util.Scanner;
import presentationlayer.AdminMenu;
import presentationlayer.BaseMenu;
import presentationlayer.LoginMenu;
import presentationlayer.RequestorMenu;
import presentationlayer.SupportAgentMenu;

public class CampusHelpDeskTicketingSystem {

    private static Repository repository;
    private static Scanner input;
    
    public static void main(String[] args) {
        repository = new Repository();
        input = new Scanner(System.in);
        seedData(repository);
        run();
        input.close();
    } // Initialize Repository and Scanner

    public static void run() {
        while(true) {
            LoginMenu loginMenu = new LoginMenu(input, repository);
            loginMenu.run();
            User user = loginMenu.getAuthenticatedUser();
            if (user == null ) { break; }
            launchMenu(user).run();
            System.out.println("You have been logged Out");
        }
    } // Main Loop of whole System

    public static BaseMenu launchMenu(User user) {
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user instanceof Admin admin)         return new AdminMenu(admin, input, repository);
        if (user instanceof SupportAgent agent)  return new SupportAgentMenu(agent, input, repository);
        if (user instanceof Requestor requestor) return new RequestorMenu(requestor, input, repository);
        throw new IllegalStateException("No menu for " + user.getRole());
    }

    public static void seedData(Repository repo) {

        if (repo == null) { return; }

        // 1. Seed Users (Admin, SupportAgents, Student, Faculty)
        Admin admin = new Admin("ADM-001", "Alice", "Admin", "alice@univ.edu", "pass123", repo, "EMP-100", "IT", "ext111");
        SupportAgent agent1 = new SupportAgent("AGT-001", "Bob", "Builder", "bob@univ.edu", "pass123", repo, "EMP-201", "FACILITIES_MAINTENANCE", "ext201");
        SupportAgent agent2 = new SupportAgent("AGT-002", "Charlie", "Coder", "charlie@univ.edu", "pass123", repo, "EMP-202", "SOFTWARE_SYSTEMS", "ext202");
        SupportAgent agent3 = new SupportAgent("AGT-003", "Dave", "Debug", "dave@univ.edu", "pass123", repo, "EMP-203", "SOFTWARE_SYSTEMS", "ext203");
        Student student = new Student("REQ-001", "David", "Student", "david@univ.edu", "pass123", repo, "S-001", "BSCS", 3, "3A");
        Faculty faculty = new Faculty("REQ-002", "Eve", "Professor", "eve@univ.edu", "pass123", repo, "F-001", "CS Dept", "Professor", "ext301");

        repo.addUser(admin);
        repo.addUser(agent1);
        repo.addUser(agent2);
        repo.addUser(agent3);
        repo.addUser(student);
        repo.addUser(faculty);

        // 2. Seed Tickets with Lifecycles, Priorities, Comments, and Admin Operations
        Ticket t1 = student.createTicket("HIGH", "FACILITIES_MAINTENANCE",  "Broeken airocn", "Aircon is leaking in Room 402");
        student.submitTicket(t1);
        agent1.updatePriority(t1.getTicketId(), "URGENT", "urgent matters");
        agent1.updateTicketState(t1.getTicketId(), "IN_PROGRESS", "Inspecting the aircon unit");
        agent1.updateTicketState(t1.getTicketId(), "RESOLVED", "Replaced filter and sealed leak");
        student.reopenTicket(t1.getTicketId(), "Still leaking a bit");
        agent1.updateTicketState(t1.getTicketId(), "IN_PROGRESS", "Checking again");
        agent1.updateTicketState(t1.getTicketId(), "RESOLVED", "Fixed permanently");
        student.closeTicket(t1.getTicketId(), "Confirmed fixed, thank you!");

        Ticket t2 = faculty.createTicket("MEDIUM", "FACILITIES_MAINTENANCE",  "Broeken airocn",  "Door handle broken");
        faculty.submitTicket(t2);
        student.addComment(t2.getTicketId(), "Please check soon.");
        agent1.addComment(t2.getTicketId(), "Will check tomorrow morning.");

        Ticket t4 = student.createTicket("LOW", "SOFTWARE_SYSTEMS",  "Broeken Portal",  "Portal login error");
        student.submitTicket(t4);
        admin.reassignTicket(t4.getTicketId(), "AGT-003", "Load balancing task");

        Ticket t5 = faculty.createTicket("URGENT", "NETWORK", "Broeken wifi",  "Wi-Fi down in hall");
        faculty.submitTicket(t5);
        admin.rejectTicket(t5.getTicketId(), "Duplicate ticket request");
    }

}
