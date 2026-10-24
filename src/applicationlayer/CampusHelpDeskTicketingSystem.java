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

    // Serves as the main application class, it is the start of the program. It initializes the repository and scanner, 
    // creates sample users and tickets, handles log in loop, and opens correct menu based on authenticated user role

    private static Repository repository;
    private static Scanner input;
    
    // Initializes Repository and Scanner, the main entry point of the system.
    public static void main(String[] args) {
        repository = new Repository();
        input = new Scanner(System.in);
        seedData(repository);
        run();
        input.close();
    } // Initialize Repository and Scanner 

    // Executes the main application runtime loop, managing user login, session handling, 
    // menu launching, and logout sequences.
    public static void run() {
        while(true) {
            LoginMenu loginMenu = new LoginMenu(input, repository);
            loginMenu.run();
            User user = loginMenu.getAuthenticatedUser();
            if (user == null ) { break; }
            try {
                getMenu(user).run();
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
            System.out.println("You have been logged Out");
        }
    } // Main Loop of whole System


    // Instantiates and returns the appropriate menu view based on the authenticated user's role.
    // @param user The currently authenticated user.
    // @return The corresponding BaseMenu implementation for the user's role.
    // @throws IllegalArgumentException if the provided user is null.
    // @throws IllegalStateException if no menu mapping exists for the user's role.
    public static BaseMenu getMenu(User user) {
        
        if (user == null) throw new IllegalArgumentException("User cannot be null");
        if (user instanceof Admin admin)         return new AdminMenu(admin, input, repository);
        if (user instanceof SupportAgent agent)  return new SupportAgentMenu(agent, input, repository);
        if (user instanceof Requestor requestor) return new RequestorMenu(requestor, input, repository);
        throw new IllegalStateException("No menu for " + user.getRole());

    }
    
    // Receives the a Repository, populates the repository with initial mock data including users, support agents, and sample tickets.
    public static void seedData(Repository repo) {

        if (repo == null) { return; }

        Admin admin = new Admin("ACC-001", "Alice", "Admin", "alice@univ.edu", "pass", repo, "EMP-100", "IT", "ext111");
        Student student1 = new Student("ACC-002", "David", "Santos", "david@univ.edu", "pass", repo, "S-001", "BSCS", 3, "3A");
        Faculty faculty1 = new Faculty("ACC-003", "Eve", "Lopez", "eve@univ.edu", "pass", repo, "F-001", "CS Dept", "Professor", "ext301");
        
        SupportAgent agent1 = new SupportAgent("ACC-004", "Bob", "Builder", "bob@univ.edu", "pass", repo, "EMP-201", "FACILITIES_MAINTENANCE", "ext201");
        SupportAgent agent2 = new SupportAgent("ACC-005", "Charlie", "Coder", "charlie@univ.edu", "pass", repo, "EMP-202", "FACILITIES_MAINTENANCE", "ext202");
        SupportAgent agent3 = new SupportAgent("ACC-006", "Dave", "Debug", "dave@univ.edu", "pass", repo, "EMP-203", "SOFTWARE_SYSTEMS", "ext203");
        SupportAgent agent4 = new SupportAgent("ACC-007", "Nina", "Network", "nina@univ.edu", "pass", repo, "EMP-204", "NETWORK", "ext204");
        SupportAgent agent5 = new SupportAgent("ACC-008", "Hank", "Hardware", "hank@univ.edu", "pass", repo, "EMP-205", "HARDWARE", "ext205");
        SupportAgent agent6 = new SupportAgent("ACC-009", "Sam", "Secure", "sam@univ.edu", "pass", repo, "EMP-206", "ACCOUNT_SECURITY", "ext206");

        repo.addUser(admin);
        repo.addUser(student1);
        repo.addUser(faculty1);
        repo.addUser(agent1);
        repo.addUser(agent2);
        repo.addUser(agent3);
        repo.addUser(agent4);
        repo.addUser(agent5);
        repo.addUser(agent6);

         Ticket[] samples = {
            student1.createTicket("HIGH",   "NETWORK",                "Campus Wi-Fi Disconnect",   "Wi-Fi keeps dropping every few minutes in the library 2nd floor."),
            student1.createTicket("MEDIUM", "SOFTWARE_SYSTEMS",       "LMS / Portal Issue",        "I can't open the course page for CS 101 in the LMS."),
            faculty1.createTicket("MEDIUM", "SOFTWARE_SYSTEMS",       "Software Installation",     "Need the latest statistics software installed on my office PC."),
            faculty1.createTicket("MEDIUM", "NETWORK",                "VPN Connection",            "VPN disconnects when I try to access the faculty drive from home.")
        };
        for (Ticket ticket : samples) {
            ticket.autoAssignAgent(repo.getAllUsers());
            repo.addTicket(ticket);
        }

        
    }

}
