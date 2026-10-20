package entitylayer;

import datalayer.Repository;
import java.util.ArrayList;

public class SupportAgent extends User {

    private String employeeId;
    private String specialization;
    private String deskExtension;

    public SupportAgent(String userId, String firstName, String lastName, String email, String password, Repository repository, String employeeId, String specialization, String deskExtension) {
        super(userId, firstName, lastName, email, password, repository);
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

    @Override
    public String getRole() { return "SUPPORT_AGENT"; }

    @Override
    public Ticket getTicket(String ticketId) {
        Ticket ticket = getRepository().findTicketById(ticketId);
        if (ticket == null || ticket.getState().equalsIgnoreCase("DELETED")) {
            throw new IllegalArgumentException("Ticket " + ticketId + " was not found.");
        } // Throw an error if the ticket doesnt exist
        if (ticket.getAssignedAgent() == null || !ticket.getAssignedAgent().getUserId().equalsIgnoreCase(getUserId()) ) { 
           throw new SecurityException("You can only view tickets assigned to you.");
        } // Throw an error if the tickett isnt assigend to you
        return ticket; 
    } // Gets an existing Ticket that is assigned to you

    public Comment createComment(String message){
        return new Comment(this, message);
    } // create a new Comment

    public void addComment(String ticketId, String message) {
        Ticket ticket = getTicket(ticketId);
        ticket.addComment(createComment(message));
    } // add a comment to a ticket

    public void updateTicketState(String ticketId, String newState, String reason) {
        Ticket ticket = getTicket(ticketId);
        ticket.changeState(newState, this, reason);
    } // Updates a ticket state Form picking From OPEN / IN PROGRESS / RESOLVED

    public void updatePriority(String ticketId, String newPriority, String reason) {
        Ticket ticket = getTicket(ticketId);
        ticket.changePriority(newPriority, this, reason);
    } // Updates aticket priority by picking from the 4. doesnt allow to assign the same priority

    public ArrayList<Ticket> getAssignedTickets() {
        ArrayList<Ticket> assignedTickets = new ArrayList<>();
        ArrayList<Ticket> allTickets = getRepository().getAllTickets();
        if (allTickets == null) { return assignedTickets; }

        for (Ticket ticket : allTickets) {
            if (ticket.getAssignedAgent() != null && ticket.getAssignedAgent().getUserId().equalsIgnoreCase(getUserId()) && !ticket.isDeleted()) {
                assignedTickets.add(ticket);
            } // Get all tickets that assignedAgent ID matches mine that are also not deleted
        }
        return assignedTickets;
    } // Gets all assigned tickets that arent deleted

    public ArrayList<Ticket> getActiveTickets() {
        ArrayList<Ticket> myTickets = new ArrayList<>();
          for (Ticket ticket : getAssignedTickets()) { // get All my assigned tickets
            if (ticket.isActive()) { // chekc if they are OPEN / IN_PROGRESS
                    myTickets.add(ticket); // if yes add them in this list
            }
          } 
          return myTickets;
    } // Returns a list of all OPEN / IN-PROGRESS agent tickets that are the Agent's

}