package entitylayer;

import datalayer.Repository;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Admin extends User {

    private String employeeId;
    private String managedDepartment;
    private String deskExtension;

    public Admin(String userId, String firstName, String lastName, String email, String password, Repository repository, String employeeId, String managedDepartment, String deskExtension) {
        super(userId, firstName, lastName, email, password, repository);
        this.employeeId = employeeId;
        this.managedDepartment = managedDepartment;
        this.deskExtension = deskExtension;
    }

    @Override
    public String getRole() {
       return "ADMIN";
    }

    @Override
    public Ticket getTicket(String ticketId) {
        Ticket ticket =getRepository().findTicketById(ticketId);
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket " + ticketId + " was not found.");
        }
        return ticket;
    }

    public User getUser(String userId) {
        User user = getRepository().findUserById(userId);
        if (user == null) {
            throw new IllegalArgumentException("User " + userId + " was not found.");
        }
        return user;
    }
    
    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getManagedDepartment() { return managedDepartment; }
    public String getDeskExtension() { return deskExtension;  }

    // Setters
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setManagedDepartment(String managedDepartment) { this.managedDepartment = managedDepartment; }
    public void setDeskExtension(String deskExtension) { this.deskExtension = deskExtension; }

    public void reassignTicket(String ticketId, String agentId, String reason) {
        Ticket ticket = getTicket(ticketId);
        User user = getRepository().findUserById(agentId);

        if (!(user instanceof SupportAgent agent)) { 
            throw new IllegalArgumentException("User with " + agentId + " is not an agent.");
        }
        ticket.reassignTicket(agent, this, reason);
    } 

    public void deleteTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId);
        ticket.deletTicket(this, true, reason);
    }

    public ArrayList<Ticket> getAllTickets() {
        ArrayList<Ticket> allTickets = getRepository().getAllTickets();
        if (allTickets == null) { return new ArrayList<>(); }
        return allTickets;
    }

    public ArrayList<User> getAllUsers() {
        ArrayList<User> allUsers = getRepository().getAllUsers();
        if (allUsers == null) { return new ArrayList<>(); }
        return allUsers;
    }

    public ArrayList<Ticket> filterTickets(String field, String value) {
        ArrayList<Ticket> result = new ArrayList<>();
        if (field == null || value == null) { return result; }

        ArrayList<Ticket> allTickets = getAllTickets();
        for (Ticket ticket : allTickets) {
            String actual = switch (field.toUpperCase()) {
                case "ID"        -> ticket.getTicketId();
                case "STATE"     -> ticket.getState();
                case "CATEGORY"  -> ticket.getCategory();
                case "PRIORITY"  -> ticket.getPriority();
                case "USER"      -> ticket.getSubmittedBy().getUserId();
                case "AGENT"     -> ticket.getAssignedAgent() == null ? "UNASSIGNED" : ticket.getAssignedAgent().getUserId() ; 
                default          -> null;
            };
            if (actual != null && actual.equalsIgnoreCase(value.trim())) {
                result.add(ticket);
            }
        }
        return result;
    }

    public ArrayList<User> filterUsers(String field, String value) {
        ArrayList<User> result = new ArrayList<>();
        if (field == null || value == null) { return result; }

        String searchItem = value.toUpperCase();
        ArrayList<User> allUsers = getAllUsers();
        for (User user : allUsers ) {
            if (user == null) { continue; }
            String actual = switch(field.toUpperCase()) {
                case "ID"             -> user.getUserId();
                case "ROLE"           -> user.getRole();
                case "NAME"           -> user.getFullName();
                case "SPECIALIZATION" -> user instanceof SupportAgent supportAgent ? supportAgent.getSpecialization() : null;
                default               -> null;
            };
            if (actual  != null && actual.toUpperCase().contains(searchItem)) {
                result.add(user);
            }
        }
        return result;
    }

    public LinkedHashMap<String, Integer> getSummaryCounts() {
        LinkedHashMap<String, Integer> counts = new LinkedHashMap<>();
        counts.put("TOTAL_TICKETS", getAllTickets().size()); // TICKET
        counts.put("TOTAL_USERS", getAllUsers().size()); //  / USERS
       
        String[] states = {"OPEN", "IN_PROGRESS", "RESOLVED", "CLOSED", "DELETED"};
        for (String state : states) { counts.put(state, filterTickets("STATE", state).size()); } // States
        String[] priorities = {"LOW", "MEDIUM", "HIGH", "URGENT"};
        for (String priority : priorities ) { counts.put(priority, filterTickets("PRIORITY", priority).size()); } // PRIORITY
        String[] categories = {"FACILITIES_MAINTENANCE", "SOFTWARE_SYSTEMS", "NETWORK", "HARDWARE", "ACCOUNT_SECURITY"};
        for (String category : categories) { counts.put(category, filterTickets("CATEGORY", category).size()); } // CATEGORY

        counts.put("UNASSIGNED_TICKETS", filterTickets("AGENT", "UNASSIGNED").size()); // Others
        return counts;
    }

}