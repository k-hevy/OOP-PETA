package applicationlayer;

import datalayer.Repository;
import entitylayer.Admin;
import entitylayer.Faculty;
import entitylayer.Student;
import entitylayer.SupportAgent;
import entitylayer.Ticket;
import java.util.Scanner;
import presentationlayer.LoginMenu;

public class CampusHelpDeskTicketingSystem {

    private static Repository repository;
    private static Scanner input;

    public CampusHelpDeskTicketingSystem() {

    }

    public static void main(String[] args) {
        
        // Initialize Repository and Scanner
        repository = new Repository();
        seedData(repository);
        
        input = new Scanner(System.in);
        
        Admin admin = new Admin("ADM-001", "Alice", "Admin", "alice@univ.edu", "pass123", repository, "EMP-100", "IT", "ext111");
        SupportAgent agent1 = new SupportAgent("AGT-001", "Bob", "Builder", "bob@univ.edu", "pass123", repository, "EMP-201", "FACILITIES_MAINTENANCE", "ext201");
        SupportAgent agent2 = new SupportAgent("AGT-002", "Charlie", "Coder", "charlie@univ.edu", "pass123", repository, "EMP-202", "SOFTWARE_SYSTEMS", "ext202");
        Student student = new Student("REQ-001", "David", "Student", "david@univ.edu", "pass123", repository, "S-001", "BSCS", 3, "3A");
        Faculty faculty = new Faculty("REQ-002", "Eve", "Professor", "eve@univ.edu", "pass123", repository, "F-001", "CS Dept", "Professor", "ext301");

        repository.addUser(admin);
        repository.addUser(agent1);
        repository.addUser(agent2);
        repository.addUser(student);
        repository.addUser(faculty);

        LoginMenu loginMenu = new LoginMenu(input, repository);
        loginMenu.run();
        
        input.close();

    }

    public static void debug() {

         System.out.println("=== INITIALIZING TICKETING SYSTEM SIMULATION ===\n");
        
        // Initialize Repository
        Repository repo = new Repository();

        // 1. Seed Users (Roles: Admin, SupportAgent, Requestor - Student & Faculty)
        // User IDs: ADM-001, AGT-001/002, REQ-001/002[cite: 1]
        Admin admin = new Admin("ADM-001", "Alice", "Admin", "alice@univ.edu", "pass123", repo, "EMP-100", "IT", "ext111");
        
        SupportAgent agent1 = new SupportAgent("AGT-001", "Bob", "Builder", "bob@univ.edu", "pass123", repo, "EMP-201", "FACILITIES_MAINTENANCE", "ext201");
        SupportAgent agent2 = new SupportAgent("AGT-002", "Charlie", "Coder", "charlie@univ.edu", "pass123", repo, "EMP-202", "SOFTWARE_SYSTEMS", "ext202");
        
        Student student = new Student("REQ-001", "David", "Student", "david@univ.edu", "pass123", repo, "S-001", "BSCS", 3, "3A");
        Faculty faculty = new Faculty("REQ-002", "Eve", "Professor", "eve@univ.edu", "pass123", repo, "F-001", "CS Dept", "Professor", "ext301");

        repo.addUser(admin);
        repo.addUser(agent1);
        repo.addUser(agent2);
        repo.addUser(student);
        repo.addUser(faculty);

        
        System.out.println("--- TEST 1: TICKET SUBMISSION & AUTO-ASSIGNMENT ---");
        // Student submits a FACILITIES_MAINTENANCE ticket. 
        // Rule: Auto-assigns to matching-specialization agent with the fewest active tickets[cite: 1].
        Ticket t1 = student.createTicket("HIGH", "FACILITIES_MAINTENANCE", "Aircon is leaking in Room 402");
        boolean submitted1 = student.submitTicket(t1);
        System.out.println("Ticket 1 Submitted: " + submitted1);
        System.out.println("Ticket 1 Assigned To: " + (t1.getAssignedAgent() != null ? t1.getAssignedAgent().getFullName() : "Unassigned")); // Should be Bob (AGT-001)

        // Submit second ticket in same category to check load balancing / fewest active tickets
        Ticket t2 = faculty.createTicket("MEDIUM", "FACILITIES_MAINTENANCE", "Door handle broken");
        faculty.submitTicket(t2);
        System.out.println("Ticket 2 Assigned To: " + t2.getAssignedAgent().getFullName() + " (Active tickets check)");

        System.out.println("\n--- TEST 2: PRIORITY RULES ---");
        // Rule: Requestor sets priority at submission and can't change it afterwards[cite: 1].
        // (Requestor class has no changePriority method).
        
        // Rule: Agent can use changePriority on an assigned ticket in OPEN or IN_PROGRESS[cite: 1].
        boolean priorityChange1 = agent1.changePriority(t1.getTicketId(), "URGENT");
        System.out.println("Agent changed priority (HIGH -> URGENT): " + priorityChange1);
        System.out.println("New Priority: " + t1.getPriority());

        // Edge case: Agent tries to change priority to an invalid value or same value
        boolean invalidPriority = agent1.changePriority(t1.getTicketId(), "URGENT"); // Same value
        System.out.println("Agent change to same priority (should fail): " + invalidPriority);
        
        boolean badPriority = agent1.changePriority(t1.getTicketId(), "SUPER_HIGH"); // Invalid name
        System.out.println("Agent change to invalid priority name (should fail): " + badPriority);

        System.out.println("\n--- TEST 3: LIFECYCLE & STATE TRANSITIONS ---");
        // Rule: Path is OPEN -> IN_PROGRESS -> RESOLVED -> CLOSED[cite: 1].
        // Edge case: Try invalid transition (OPEN -> RESOLVED directly)
        boolean invalidTransition = agent1.updateTicketState(t1.getTicketId(), "RESOLVED", "Fixing it now");
        System.out.println("Invalid transition OPEN -> RESOLVED (should fail): " + invalidTransition);

        // Valid transition 1: OPEN -> IN_PROGRESS
        boolean startWork = agent1.updateTicketState(t1.getTicketId(), "IN_PROGRESS", "Inspecting the aircon unit");
        System.out.println("Start work OPEN -> IN_PROGRESS: " + startWork);

        // Edge case: Non-assigned agent (agent2) tries to update t1
        boolean unauthorizedUpdate = agent2.updateTicketState(t1.getTicketId(), "RESOLVED", "Trying to steal ticket");
        System.out.println("Unauthorized agent state update (should fail): " + unauthorizedUpdate);

        // Valid transition 2: IN_PROGRESS -> RESOLVED
        boolean resolveTicket = agent1.updateTicketState(t1.getTicketId(), "RESOLVED", "Replaced filter and sealed leak");
        System.out.println("Resolve ticket IN_PROGRESS -> RESOLVED: " + resolveTicket);

        // Valid transition 3: RESOLVED -> IN_PROGRESS (Reopen by Requestor, same agent stays)[cite: 1]
        boolean reopenTicket = student.reopenTicket(t1.getTicketId(), "Still leaking a bit");
        System.out.println("Requestor reopens ticket RESOLVED -> IN_PROGRESS: " + reopenTicket);
        System.out.println("Assigned agent after reopen (should remain Bob): " + t1.getAssignedAgent().getFullName());

        // Resolve again so we can test closing
        agent1.updateTicketState(t1.getTicketId(), "IN_PROGRESS", "Checking again");
        agent1.updateTicketState(t1.getTicketId(), "RESOLVED", "Fixed permanently");

        // Valid transition 4: RESOLVED -> CLOSED (by Requestor)[cite: 1]
        boolean closeTicket = student.closeTicket(t1.getTicketId(), "Confirmed fixed, thank you!");
        System.out.println("Requestor closes ticket RESOLVED -> CLOSED: " + closeTicket);
        System.out.println("Final State: " + t1.getState());

        // Edge case: Try to reopen or cancel a CLOSED ticket (Terminal state)[cite: 1]
        boolean tryReopenClosed = student.reopenTicket(t1.getTicketId(), "Want to reopen");
        System.out.println("Reopen closed ticket (should fail): " + tryReopenClosed);

        System.out.println("\n--- TEST 4: COMMENTS & HISTORY ---");
        // Rule: Only ticket's requestor and assigned agent can comment[cite: 1].
        Ticket t3 = student.createTicket("MEDIUM", "FACILITIES_MAINTENANCE", "Lightbulb out");
        student.submitTicket(t3);

        boolean studentComment = student.addComment(t3.getTicketId(), "Please check soon.");
        System.out.println("Student adds comment: " + studentComment);

        boolean agentComment = agent1.addComment(t3.getTicketId(), "Will check tomorrow morning.");
        System.out.println("Assigned agent adds comment: " + agentComment);

        // Edge case: Unassigned agent (agent2) tries to comment
        boolean unassignedAgentComment = agent2.addComment(t3.getTicketId(), "My comment");
        System.out.println("Unassigned agent adds comment (should fail): " + unassignedAgentComment);

        // Edge case: Add comment on a CANCELLED or CLOSED ticket
        agent1.updateTicketState(t3.getTicketId(), "IN_PROGRESS", "Starting work");
        agent1.updateTicketState(t3.getTicketId(), "RESOLVED", "Done");
        student.closeTicket(t3.getTicketId(), "Done");
        boolean commentOnClosed = student.addComment(t3.getTicketId(), "Late comment");
        System.out.println("Add comment on CLOSED ticket (should fail): " + commentOnClosed);

        System.out.println("\n--- TEST 5: ADMIN OPERATIONS (REASSIGN & REJECT) ---");
        // Create an open ticket for testing admin actions
        Ticket t4 = student.createTicket("LOW", "SOFTWARE_SYSTEMS", "Portal login error");
        student.submitTicket(t4); // Auto-assigned to AGT-002 (Charlie) since it's SOFTWARE_SYSTEMS

        // Admin reassigns ticket to another agent of the same specialization[cite: 1]
        // Let's create another software agent to reassign to
        SupportAgent agent3 = new SupportAgent("AGT-003", "Dave", "Debug", "dave@univ.edu", "pass123", repo, "EMP-203", "SOFTWARE_SYSTEMS", "ext203");
        repo.addUser(agent3);

        boolean reassign = admin.reassignTicket(t4.getTicketId(), "AGT-003", "Load balancing task");
        System.out.println("Admin reassigns ticket to AGT-003: " + reassign);
        System.out.println("New Assigned Agent: " + t4.getAssignedAgent().getFullName());

        // Admin rejects an open/in-progress ticket -> CANCELLED[cite: 1]
        Ticket t5 = faculty.createTicket("URGENT", "NETWORK", "Wi-Fi down in hall");
        faculty.submitTicket(t5); // Unassigned (no network agent yet)

        boolean rejectTicket = admin.rejectTicket(t5.getTicketId(), "Duplicate ticket request");
        System.out.println("Admin rejects ticket -> CANCELLED: " + rejectTicket);
        System.out.println("Ticket 5 State: " + t5.getState());

        System.out.println("\n--- TEST 6: SUMMARY REPORTS & FILTERS ---");
        var summary = admin.getSummaryCounts();
        System.out.println("System Summary Counts:");
        summary.forEach((k, v) -> System.out.println("  " + k + " : " + v));

        var unassignedFilter = admin.filterTickets("AGENT", "UNASSIGNED");
        System.out.println("Unassigned Tickets Count via Filter: " + unassignedFilter.size());

        System.out.println("\n=== SIMULATION COMPLETED SUCCESSFULLY ===");

    } // For testing

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
        Ticket t1 = student.createTicket("HIGH", "FACILITIES_MAINTENANCE", "Aircon is leaking in Room 402");
        student.submitTicket(t1);
        agent1.changePriority(t1.getTicketId(), "URGENT");
        agent1.updateTicketState(t1.getTicketId(), "IN_PROGRESS", "Inspecting the aircon unit");
        agent1.updateTicketState(t1.getTicketId(), "RESOLVED", "Replaced filter and sealed leak");
        student.reopenTicket(t1.getTicketId(), "Still leaking a bit");
        agent1.updateTicketState(t1.getTicketId(), "IN_PROGRESS", "Checking again");
        agent1.updateTicketState(t1.getTicketId(), "RESOLVED", "Fixed permanently");
        student.closeTicket(t1.getTicketId(), "Confirmed fixed, thank you!");

        Ticket t2 = faculty.createTicket("MEDIUM", "FACILITIES_MAINTENANCE", "Door handle broken");
        faculty.submitTicket(t2);
        student.addComment(t2.getTicketId(), "Please check soon.");
        agent1.addComment(t2.getTicketId(), "Will check tomorrow morning.");

        Ticket t4 = student.createTicket("LOW", "SOFTWARE_SYSTEMS", "Portal login error");
        student.submitTicket(t4);
        admin.reassignTicket(t4.getTicketId(), "AGT-003", "Load balancing task");

        Ticket t5 = faculty.createTicket("URGENT", "NETWORK", "Wi-Fi down in hall");
        faculty.submitTicket(t5);
        admin.rejectTicket(t5.getTicketId(), "Duplicate ticket request");
    }

}



// void run()
// void launchMenu(User user)
// private void seedData()
// private void logout()
// private void exit() 
