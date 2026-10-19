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
            try {
                launchMenu(user).run();
            } catch (IllegalStateException e) {
                System.out.println("Error: " + e.getMessage());
            }
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

        SupportAgent agent1 = new SupportAgent("AGT-001", "Bob", "Builder", "bob@univ.edu", "pass123", repo, "EMP-201", "FACILITIES_MAINTENANCE", "ext201");
        SupportAgent agent2 = new SupportAgent("AGT-002", "Charlie", "Coder", "charlie@univ.edu", "pass123", repo, "EMP-202", "SOFTWARE_SYSTEMS", "ext202");
        SupportAgent agent3 = new SupportAgent("AGT-003", "Dave", "Debug", "dave@univ.edu", "pass123", repo, "EMP-203", "SOFTWARE_SYSTEMS", "ext203");
        SupportAgent agent4 = new SupportAgent("AGT-004", "Nina", "Network", "nina@univ.edu", "pass123", repo, "EMP-204", "NETWORK", "ext204");
        SupportAgent agent5 = new SupportAgent("AGT-005", "Hank", "Hardware", "hank@univ.edu", "pass123", repo, "EMP-205", "HARDWARE", "ext205");
        SupportAgent agent6 = new SupportAgent("AGT-006", "Sam", "Secure", "sam@univ.edu", "pass123", repo, "EMP-206", "ACCOUNT_SECURITY", "ext206");

        Student student1 = new Student("REQ-001", "David", "Santos", "david@univ.edu", "pass123", repo, "S-001", "BSCS", 3, "3A");
        Student student2 = new Student("REQ-002", "Maria", "Reyes", "maria@univ.edu", "pass123", repo, "S-002", "BSIT", 2, "2B");
        Student student3 = new Student("REQ-003", "Paolo", "Cruz", "paolo@univ.edu", "pass123", repo, "S-003", "BSCS", 4, "4A");
        Faculty faculty1 = new Faculty("REQ-004", "Eve", "Lopez", "eve@univ.edu", "pass123", repo, "F-001", "CS Dept", "Professor", "ext301");
        Faculty faculty2 = new Faculty("REQ-005", "Frank", "Tan", "frank@univ.edu", "pass123", repo, "F-002", "IT Dept", "Instructor", "ext302");

        Admin admin = new Admin("ADM-001", "Alice", "Admin", "alice@univ.edu", "pass123", repo, "EMP-100", "IT", "ext111");

        repo.addUser(admin);
        repo.addUser(agent1);
        repo.addUser(agent2);
        repo.addUser(agent3);
        repo.addUser(agent4);
        repo.addUser(agent5);
        repo.addUser(agent6);
        repo.addUser(student1);
        repo.addUser(student2);
        repo.addUser(student3);
        repo.addUser(faculty1);
        repo.addUser(faculty2);


        // T1: full lifecycle with a reopen -> CLOSED (FACILITIES -> Bob)
        Ticket t1 = student1.createTicket("HIGH", "FACILITIES_MAINTENANCE", "HVAC & Temperature", "Aircon is leaking in Room 402");
        student1.submitTicket(t1);
        agent1.updatePriority(t1.getTicketId(), "URGENT", "Water is reaching the outlets");
        agent1.updateTicketState(t1.getTicketId(), "IN_PROGRESS", "Inspecting the aircon unit");
        agent1.updateTicketState(t1.getTicketId(), "RESOLVED", "Replaced filter and sealed leak");
        student1.reopenTicket(t1.getTicketId(), "Still leaking a bit");
        agent1.updateTicketState(t1.getTicketId(), "RESOLVED", "Fixed permanently");
        student1.closeTicket(t1.getTicketId(), "Confirmed fixed, thank you!");

        // T2: OPEN with comments from both sides (FACILITIES -> Bob)
        Ticket t2 = faculty1.createTicket("MEDIUM", "FACILITIES_MAINTENANCE", "Carpentry & Keys", "Door handle broken in faculty room");
        faculty1.submitTicket(t2);
        faculty1.addComment(t2.getTicketId(), "Please check soon.");
        agent1.addComment(t2.getTicketId(), "Will check tomorrow morning.");

        // T3: IN_PROGRESS, reassigned by admin to the other software agent
        Ticket t3 = student2.createTicket("LOW", "SOFTWARE_SYSTEMS", "LMS / Portal Issue", "Portal login error");
        student2.submitTicket(t3);
        admin.reassignTicket(t3.getTicketId(), "AGT-003", "Load balancing task");
        SupportAgent t3Agent = (SupportAgent) repo.findUserById("AGT-003");
        t3Agent.updateTicketState(t3.getTicketId(), "IN_PROGRESS", "Reproducing the login error");

        // T4: DELETED by admin (NETWORK -> Nina)
        Ticket t4 = faculty2.createTicket("URGENT", "NETWORK", "Campus Wi-Fi Disconnect", "Wi-Fi down in hall");
        faculty2.submitTicket(t4);
        admin.deleteTicket(t4.getTicketId(), "Duplicate ticket request");

        // T5: DELETED by requestor
        Ticket t5 = student3.createTicket("LOW", "HARDWARE", "Printer & Copier Jam", "Printer jam in Lab 2");
        student3.submitTicket(t5);
        student3.deleteTicket(t5.getTicketId(), "Jam cleared by a classmate");

        // T6: RESOLVED, waiting for requestor to close (HARDWARE -> Hank)
        Ticket t6 = student3.createTicket("HIGH", "HARDWARE", "Classroom AV & Projector", "Projector has no signal in Room 210");
        student3.submitTicket(t6);
        agent5.updateTicketState(t6.getTicketId(), "IN_PROGRESS", "Checking HDMI cable");
        agent5.updateTicketState(t6.getTicketId(), "RESOLVED", "Replaced HDMI cable");

        // T7: ACCOUNT_SECURITY ticket, auto-assigned to Sam
        Ticket t7 = student2.createTicket("URGENT", "ACCOUNT_SECURITY", "Phishing & Scams", "Received a suspicious email asking for my password");
        student2.submitTicket(t7);

        // T8: second software ticket, auto-assigned by workload
        Ticket t8 = faculty2.createTicket("MEDIUM", "SOFTWARE_SYSTEMS", "Software Installation", "Need MATLAB installed on office PC");
        faculty2.submitTicket(t8);
        faculty2.addComment(t8.getTicketId(), "Same problem on the lab PCs.");

        // T9: NETWORK in progress
        Ticket t9 = student1.createTicket("HIGH", "NETWORK", "VPN Connection", "VPN keeps dropping at home");
        student1.submitTicket(t9);
        agent4.updateTicketState(t9.getTicketId(), "IN_PROGRESS", "Checking VPN logs");
        agent4.addComment(t9.getTicketId(), "Please send the exact error message.");
    }

}
