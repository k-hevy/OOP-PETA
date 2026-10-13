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
    public String getRole() { return "SUPPORTAGENT"; }

    @Override
    public Ticket getTicket(String ticketId) {
        Ticket ticket = getRepository().findTicketById(ticketId);
        if (ticket == null || ticket.getAssignedAgent() == null) { return null; }
        if (!ticket.getAssignedAgent().getUserId().equalsIgnoreCase(getUserId())) { return null; }
        return ticket;
    } 

    public Comment createComment(String message){
        return new Comment(getUserId(), message);
    }

    public boolean addComment(String ticketId, String message) {
        Ticket ticket = getTicket(ticketId);
        if (ticket == null) { return false; }
        return ticket.addComment(createComment(message));
    }

    public boolean updateTicketState(String ticketId, String newState, String reason) {
        if (newState == null || reason == null) { return false; }
        Ticket ticket = getTicket(ticketId);
        if (ticket == null) { return false; }

        String currentState = ticket.getState();
        boolean startWork   = currentState.equalsIgnoreCase("OPEN")        && newState.equalsIgnoreCase("IN_PROGRESS");
        boolean resolve     = currentState.equalsIgnoreCase("IN_PROGRESS") && newState.equalsIgnoreCase("RESOLVED");
        boolean undoResolve = currentState.equalsIgnoreCase("RESOLVED")    && newState.equalsIgnoreCase("IN_PROGRESS");
        if (!startWork && !resolve && !undoResolve) { return false; }
        return ticket.changeState(newState, this, reason);
    }

    public boolean changePriority(String ticketId, String newPriority) {
        Ticket ticket = getTicket(ticketId);
        if (ticket == null) return false;
        return ticket.changePriority(newPriority, this);
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

    public int getTicketsResolvedCount() {
        int count = 0;
        for (Ticket ticket : getAssignedTickets()) {
            String state = ticket.getState();
            if (state.equalsIgnoreCase("RESOLVED") || state.equalsIgnoreCase("CLOSED")) {
                count++;
            }
        }
        return count;
    }

    public ArrayList<Ticket> getActiveTickets() {
        ArrayList<Ticket> myTickets = new ArrayList<>();
          for (Ticket ticket : getAssignedTickets()) {
            String state = ticket.getState();
            if (ticket.getAssignedAgent().getUserId().equalsIgnoreCase(getUserId()) &&
                  (state.equalsIgnoreCase("OPEN") || state.equalsIgnoreCase("IN_PROGRESS"))) {
                    myTickets.add(ticket);
            }
          }
          return myTickets;
     }

}