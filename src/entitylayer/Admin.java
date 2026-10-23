package entitylayer;

import datalayer.Repository;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Admin extends User {

    // Represents the administrator of the Campus Help Desk Ticketing System. It extends the User class so it inherits common user information and methods. 
    // The admin class has an additional responsibilities, including managing users, reassigning and deleting tickets, filtering records and generate systems summary

    private String employeeId;
    private String managedDepartment;
    private String deskExtension;

    // Constructs a new Admin instance with administrative attributes
    public Admin(String userId, String firstName, String lastName, String email, String password, Repository repository, String employeeId, String managedDepartment, String deskExtension) {
        super("ADMIN", userId, firstName, lastName, email, password, repository);
        this.employeeId = employeeId;
        this.managedDepartment = managedDepartment;
        this.deskExtension = deskExtension;
    }

    // Gets a ticket in the system even deleted ones or null if not found
    @Override
    public Ticket getTicket(String ticketId) {
        Ticket ticket =getRepository().findTicketById(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket " + ticketId + " was not found.");
        } // only throw an error if the ticket doesnt exists
        return ticket;
    } // Gets ALL tickets in the system even deleted ones

    // Gets all tickets in the system even deleted ones
    @Override
    public ArrayList<Ticket> getTickets() {
        return getRepository().getAllTickets();
    } // gets all tickets

    // Receives a userId and returns a user if found else null
    public User getUser(String userId) {
        User user = getRepository().findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User " + userId + " was not found.");
        } // Only throw an error if the user doesnt exists
        return user;
    } // Gets ALL users in the sysetm

    // gets all users
    public ArrayList<User> getAllUsers() {
        return getRepository().getAllUsers();
    } // gets all users
    
    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getManagedDepartment() { return managedDepartment; }
    public String getDeskExtension() { return deskExtension;  }

    // Setters
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setManagedDepartment(String managedDepartment) { this.managedDepartment = managedDepartment; }
    public void setDeskExtension(String deskExtension) { this.deskExtension = deskExtension; }

    // Accepts a ticketID, agentId and reason. Find the ticket and SupportAgent. Reassigns a ticket to another agent with amatching specialization by calling ticket methods 
    public void reassignTicket(String ticketId, String agentId, String reason) {
        Ticket ticket = getTicket(ticketId);
        User user = getRepository().findUserById(agentId);
        ticket.reassignTicket(user, this, reason);
    } 

    // Accepts a ticketID and reason, finds the ticket and call the ticket delete method.
    public void deleteTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId);
        ticket.deleteTicket(this, reason);
    } // Deletes any tickets that isnt already deleted

    // Returns filtered Ticket list based on the field and value
    public ArrayList<Ticket> filterTickets(String field, String value) {
        ArrayList<Ticket> result = new ArrayList<>();
        ArrayList<Ticket> allTickets = getTickets(); // gets all tickets
        for (Ticket ticket : allTickets) { // EX FIELD = CATEGORY, VALUE = MAINTENANCE
            String actual = switch (field.toUpperCase()) {
                case "ID"        -> ticket.getTicketId();
                case "STATE"     -> ticket.getState();
                case "CATEGORY"  -> ticket.getCategory();
                case "PRIORITY"  -> ticket.getPriority();
                case "USER"      -> ticket.getSubmittedBy().getUserId();
                case "AGENT"     -> ticket.getAssignedAgent() == null ? "UNASSIGNED" : ticket.getAssignedAgent().getUserId() ; 
                default          -> null;
            }; // compare the value to the ticket based on the given field
            if (actual != null && actual.equalsIgnoreCase(value.trim())) {
                result.add(ticket); // if yes add them
            }
        }
        return result;
    } // Returns filtered Ticket list based on the field and valuee

    // Returns filtered User list based on the field and value
    public ArrayList<User> filterUsers(String field, String value) {
        ArrayList<User> result = new ArrayList<>();
        String searchItem = value.trim().toUpperCase();
        for (User user : getAllUsers()) { // EX ROLE = CATEGORY, VALUE = REQUESTOR
            if (user == null) { continue; }
            String actual = switch(field.toUpperCase()) {
                case "ID"             -> user.getUserId();
                case "ROLE"           -> user.getRole();
                case "NAME"           -> user.getFullName();
                case "SPECIALIZATION" -> user instanceof SupportAgent supportAgent ? supportAgent.getSpecialization() : null;
                default               -> null;
            }; // compare the value to the ticket based on the given field
            if (actual  != null && actual.toUpperCase().contains(searchItem)) {
                result.add(user); // if yes add them
            }
        }
        return result;
    } // Returns filtered User list based on the field and value

    // this method uses the Filter Users and Filter Ticket method inputting a FIELD and VALUE and getting the SIZE() of the lists and adding it to a LinkedHashMap
    public LinkedHashMap<String, Integer> getSummaryCounts() {
        LinkedHashMap<String, Integer> counts = new LinkedHashMap<>(); // A linked Hash map is a series of KEY-VALUE pairs EX. TOTAL-TICKETS = KEY.  15 VALUE
        counts.put("TOTAL_TICKETS", getTickets().size()); // TICKET 
        counts.put("TOTAL_USERS", getAllUsers().size()); //  / USERS
       
        String[] states = {"OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED", "DELETED"};
        for (String state : states) { counts.put(state, filterTickets("STATE", state).size()); } // States
        String[] priorities = {"LOW", "MEDIUM", "HIGH", "URGENT"};
        for (String priority : priorities ) { counts.put(priority, filterTickets("PRIORITY", priority).size()); } // PRIORITY
        String[] categories = {"FACILITIES_MAINTENANCE", "SOFTWARE_SYSTEMS", "NETWORK", "HARDWARE", "ACCOUNT_SECURITY"};
        for (String category : categories) { counts.put(category, filterTickets("CATEGORY", category).size()); } // CATEGORY

        counts.put("UNASSIGNED_TICKETS", filterTickets("AGENT", "UNASSIGNED").size()); // Others
        return counts;
    } // this method uses the Filter Users and Filter Ticket method inputting a FIELD andf VALUE and getting the SIZE() of the lists

}