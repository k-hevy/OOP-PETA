package entitylayer;

import datalayer.Repository;
import java.util.ArrayList;

public class SupportAgent extends User {

    private String employeeId;
    private String specialization;
    private String deskExtension;


    public SupportAgent(String userId, String firstName, String lastName, String email, String password, Repository repository, 
                        String employeeId, String specialization, String deskExtension) {
        super(userId, firstName, lastName, email, password, repository);
        this.employeeId = employeeId;
        this.specialization = specialization;
        this.deskExtension = deskExtension;
    }

    @Override
    public String getRole() {
       return "SUPPORTAGENT";
    }

    public String getEmployeeId() {
        return employeeId;
    }
    
    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }
    
    public String getSpecialization() {
        return specialization;
    }
    
    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }
    
    public String getDeskExtension() {
        return deskExtension;
    }
    
    public void setDeskExtension(String deskExtension) {
        this.deskExtension = deskExtension;
    }

    Comment createComment(String message){
        String commentId = "C-" + System.currentTimeMillis();
        return new Comment(commentId, this.getUserId(), this.getFirstName(), message);
    }

    public boolean addComment(String ticketId, String message) {
        if(message == null || message.trim().isEmpty()) {
            return false;
        }
        
        Ticket ticket = getAssignedTicket(ticketId);
        if (ticket != null) {
            Comment comment = createComment(message);
            ticket.addComment(comment);
            return true;
        }
        return false;
    }

    public boolean updateTicketStatus(String ticketId, String newState, String reason) {
        Ticket ticket = getAssignedTicket(ticketId);
        if (ticket != null && newState != null && reason != null && !reason.isEmpty()) {
            return ticket.changeState(newState, this, reason);
        }
        return false;
    }

    public boolean escalateTicket(String ticketId, String newPriority) {
        Ticket ticket = getAssignedTicket(ticketId);

        if(ticket == null || newPriority == null || newPriority.isEmpty()) {
            return false;
        }

    if(!newPriority.equalsIgnoreCase("URGENT") && !newPriority.equalsIgnoreCase("HIGH") && !newPriority.equalsIgnoreCase("MEDIUM") && !newPriority.equalsIgnoreCase("LOW")) {
            return false;
        }

        ticket.setPriority(newPriority.toUpperCase());
        return true;
    }

    public Ticket getAssignedTicket(String ticketId) {
        if (ticketId == null || ticketId.isEmpty()) {
            return null;
        }
        
        Ticket ticket = getRepository().findTicketById(ticketId);
        if (ticket != null && ticket.getAssignedAgent() != null && ticket.getAssignedAgent().getUserId().equals(this.getUserId())) {
            return ticket;
        }
        return null;
    }

    public ArrayList<Ticket> getAssignedTickets() {
        ArrayList<Ticket> assignedTickets = new ArrayList<>();
        for (Ticket ticket : getRepository().getAllTickets()) {
            if (ticket.getAssignedAgent() != null && ticket.getAssignedAgent().getUserId().equals(this.getUserId())) {
                assignedTickets.add(ticket);
            }
        }
        return assignedTickets;
    }

    public int getTicketsResolvedCount() {
        int count = 0;
        for (Ticket ticket : getAssignedTickets()) {
            if (ticket.getState().equalsIgnoreCase("RESOLVED")) {
                count++;
            }
        }
        return count;
    }
}
