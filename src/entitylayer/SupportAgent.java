package entitylayer;

import datalayer.Repository;
import java.util.ArrayList;

public class SupportAgent extends User {

    // Represents the help desk personnel responsible for handling and resolving campus-related tickets. 
    // It extends the User class and provides methods to view assigned tickets, update ticket statuses and priorities, and add comments.

    private String employeeId;
    private String specialization;
    private String deskExtension;

    // Constructs a SupportAgent instance with specialization and extension details
    public SupportAgent(String userId, String firstName, String lastName, String email, String password, Repository repository, String employeeId, String specialization, String deskExtension) {
        super("SUPPORT_AGENT", userId, firstName, lastName, email, password, repository);
        this.employeeId = employeeId;
        this.specialization = specialization;
        this.deskExtension = deskExtension;
    }

    // Getters
    public String getEmployeeId() { return employeeId; }
    public String getSpecialization() { return specialization; }
    public String getDeskExtension() { return deskExtension; }

    // Setters
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId; }
    public void setSpecialization(String specialization) { this.specialization = specialization; }
    public void setDeskExtension(String deskExtension) { this.deskExtension = deskExtension; }

    // Gets an existing Ticket that is assigned to you throws an error is it does not exist or does not belong to you
    @Override
    public Ticket getTicket(String ticketId) {
        Ticket ticket = getRepository().findTicketById(ticketId);
        if (ticket == null || ticket.isDeleted()) {
            throw new IllegalArgumentException("Ticket " + ticketId + " was not found.");
        } // Throw an error if the ticket doesnt exist
        if (ticket.getAssignedAgent() == null || !ticket.getAssignedAgent().getUserId().equalsIgnoreCase(getUserId()) ) { 
           throw new SecurityException("Ticket " + ticketId + " is not assigned to you.");
        } // Throw an error if the tickett isnt assigend to you
        return ticket; 
    }

    // Gets all assigned tickets that arent deleted
    @Override
    public ArrayList<Ticket> getTickets() {
        ArrayList<Ticket> assignedTickets = new ArrayList<>();
        for (Ticket ticket : getRepository().getAllTickets()) {
            if (ticket.getAssignedAgent() != null && ticket.getAssignedAgent().getUserId().equalsIgnoreCase(getUserId()) && !ticket.isDeleted()) {
                assignedTickets.add(ticket);
            } // Get all tickets that assignedAgent ID matches mine that are also not deleted
        }
        return assignedTickets;
    } 

    // Receives a ticketID and reason, find the ticket. Adds a comment to an assigned ticket. calls ticket method
    public void addComment(String ticketId, String message) {
        Ticket ticket = getTicket(ticketId);
        ticket.addComment(new Comment(this, message));
    }

    // Receives a ticketID and reason, find the ticket. Updates a ticket state Form picking From OPEN / IN PROGRESS / RESOLVED. Calls ticket methods
    public void updateTicketState(String ticketId, String newState, String reason) {
        Ticket ticket = getTicket(ticketId);
        ticket.updateTicketState(newState, this, reason);
    } 

    // Receives a ticketID and reason and newPriority, find the ticket. Updates a ticket priority. calls ticket method to update
    public void updatePriority(String ticketId, String newPriority, String reason) {
        Ticket ticket = getTicket(ticketId);
        ticket.changePriority(newPriority, this, reason);
    } 

}