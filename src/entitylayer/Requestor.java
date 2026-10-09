package entitylayer;

import datalayer.Repository;
import java.util.ArrayList;


public class Requestor extends User {

    public Requestor(String userId, String firstName, String lastName, String email, String password, Repository repository) {
        super(userId, firstName, lastName, email, password, repository);
    }

    @Override
    public String getRole() {
        return "REQUESTOR";
    }

    public Ticket createTicket(String priority, String category, String description) {
        Ticket ticket = new Ticket("001", this, null, priority, category, description);
        assignTicket(ticket);
        return ticket;
    }

    // Comment createComment(String message)
    
    // boolean submitTicket(Ticket ticket)
    public boolean submitTicket(Ticket ticket){
        if (ticket != null && getRepository() != null){
            getRepository().addTicket(ticket);
            return true;
        }
        return false;
    }
    
    // boolean cancelTicket(String ticketId, String reason)
    public boolean cancelTicket(String ticket, String reason) {
        if (ticket != null){
            return ticket.changeState("Ticket Cancelled", this, reason);
        }
        return false;
    }
    
    // boolean reopenTicket(String ticketId, String reason)
    public boolean reopenTicket(String ticketId, String reason) {
        Ticket ticket = getMyTicket(ticketId);
        if (ticket != null) {
            return ticket.changeState("IN_PROGRESS", this, reason);
        }
        return false;
    }
    
    // boolean addComment(String ticketId, String message)
    
    // ArrayList<Ticket> getMyTickets()
    public Ticket getMyTicket(String ticketId) {
        if (getRepository() == null) return null;

        for (Ticket ticket : getRepository().getAllTickets()) {
            if (ticket.getTicketId().equalsIgnoreCase(ticketId) && 
                ticket.getSubmittedBy() != null &&
                ticket.getSubmittedBy().getUserId().equalsIgnoreCase(getUserId())) {
                return ticket;
            }
        }
        return null;
    }
    
    // int getSubmittedTicketCount()
    public int getSubmittedTicketCount(){
        return getMyTickets().size();
    }
    
    // ArrayList<TicketLogEntry> getTicketHistory(String ticketId)
    public ArrayList<TicketLogEntry> getTicketHistory(String ticketId) {
        Ticket ticket = getMyTicket(ticketId);
        if (ticket != null) {
            return ticket.getTicketLogs();
        }
        return new ArrayList<>();
    }
    
    // boolean closeTicket(String ticketId, String reason)
    public boolean closeTicket(String ticketId, String reason) {
        Ticket ticket = getMyTicket(ticketId);
        if (ticket != null) {
            return ticket.changeState("CLOSED", this, reason);
        }
        return false;
        
    // private int getActiveTicketCount(SupportAgent supportAgent)
    private boolean assignTicket(Ticket ticket) {

        ArrayList<User> allUSers = getRepository().getAllUsers();
        
        for (User user : allUSers) {
            if (user instanceof SupportAgent) {
                SupportAgent supportAgent = (SupportAgent) user;
                if (supportAgent.getSpecialization() != null && 
                supportAgent.getSpecialization().equalsIgnoreCase(ticket.getCategory())) {
                    ticket.setAssignedAgent(supportAgent);
                    return true;
                }
            }
        }
        return false;

    }


}
