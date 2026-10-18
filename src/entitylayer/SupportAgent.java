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
        if (ticket == null) {
            throw new IllegalArgumentException("Ticket " + ticketId + " was not found.");
        }
        if (ticket.getAssignedAgent() == null || !ticket.getAssignedAgent().getUserId().equalsIgnoreCase(getUserId()) ) { 
           throw new SecurityException("You can only view tickets assigned to you.");
        }
        return ticket;
    } 

    public Comment createComment(String message){
        return new Comment(this, message);
    }

    public void addComment(String ticketId, String message) {
        Ticket ticket = getTicket(ticketId);
        ticket.addComment(createComment(message));
    }

    public void updateTicketState(String ticketId, String newState, String reason) {
        if (newState == null || reason == null) {
            throw new IllegalArgumentException("New state and reason cannot be empty");
        }
        if (newState.equalsIgnoreCase("CLOSED") || newState.equalsIgnoreCase("CANCELLED")) {
            throw new SecurityException("agent cannot set state to " + newState.toUpperCase());
        }
        Ticket ticket = getTicket(ticketId);
        ticket.changeState(newState, this, reason);
    }

    public void updatePriority(String ticketId, String newPriority, String reason) {
        Ticket ticket = getTicket(ticketId);
        ticket.changePriority(newPriority, reason, this);
    }

    public ArrayList<Ticket> getAssignedTickets() {
        ArrayList<Ticket> assignedTickets = new ArrayList<>();
        ArrayList<Ticket> allTickets = getRepository().getAllTickets();
        if (allTickets == null) { return assignedTickets; }

        for (Ticket ticket : allTickets) {
            if (ticket.getAssignedAgent() != null && ticket.getAssignedAgent().getUserId().equalsIgnoreCase(getUserId())) {
                assignedTickets.add(ticket);
            }
        }
        return assignedTickets;
    }

    public ArrayList<Ticket> getActiveTickets() {
        ArrayList<Ticket> myTickets = new ArrayList<>();
          for (Ticket ticket : getAssignedTickets()) {
            if (ticket.isActive()) {
                    myTickets.add(ticket);
            }
          }
          return myTickets;
     }

}