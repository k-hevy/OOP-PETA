package entitylayer;

import datalayer.Repository;


public class Requestor extends User {

    public Requestor(String userId, String firstName, String lastName, String email, String password, Repository repository) {
        super(userId, firstName, lastName, email, password, repository);
    }

    @Override
    public String getRole() {
        return "REQUESTOR";
    }

    // void setRepository(Repository repository)
    // Ticket createTicket(String priority, String category, String description)
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
    // private boolean assignTicket(Ticket ticket)
    // private int getActiveTicketCount(SupportAgent supportAgent)


}