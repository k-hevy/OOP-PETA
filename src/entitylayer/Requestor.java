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
    // boolean cancelTicket(String ticketId, String reason)
    // boolean reopenTicket(String ticketId, String reason)
    // boolean addComment(String ticketId, String message)
    // Ticket getMyTicket(String ticketId)
    // ArrayList<Ticket> getMyTickets()
    // int getSubmittedTicketCount()
    // ArrayList<TicketLogEntry> getTicketHistory(String ticketId)
    // boolean closeTicket(String ticketId, String reason)
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