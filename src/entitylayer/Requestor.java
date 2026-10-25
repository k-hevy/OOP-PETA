package entitylayer;

import datalayer.Repository;
import java.util.ArrayList;

public class Requestor extends User {

    // It represents users who submit help desk tickets, such as students and faculty members. Requestors can only access their own tickets. 
    // Has the capability to create, submit, delete, reopen, close and add Comments on tickets. 

    // Constructs a Requestor instance
    public Requestor(String userId, String firstName, String lastName, String email, String password, Repository repository) {
        super("REQUESTOR",userId, firstName, lastName, email, password, repository);
    }

    // Accepts a ticket id and finds it. throws an exception if invalid Ticket ID / if you do not own ticket
    @Override
    public Ticket getTicket(String ticketId) {
        Ticket ticket = getRepository().findTicketById(ticketId);
        if (ticket == null || ticket.getState().equalsIgnoreCase("DELETED")) { 
            throw new IllegalArgumentException("Ticket " + ticketId +" was not found."); 
        } // Throw an error if you try to acces a ticket that doesnt exist
        if (!ticket.getSubmittedBy().getUserId().equalsIgnoreCase(getUserId())) { 
            throw new SecurityException("Ticket " + ticketId + " is not yours."); 
        } // Throw an Error if you try to access a ticket that isnt yours
        return ticket;  
    } 

    // Returns all tickets submitted by the requestor except Deleted ones
    @Override
    public ArrayList<Ticket> getTickets() {
       ArrayList<Ticket> myTickets = new ArrayList<>();
        ArrayList<Ticket> allTickets = getRepository().getAllTickets();
        for (Ticket ticket : allTickets) {
                if (ticket.getSubmittedBy().getUserId().equalsIgnoreCase(getUserId()) && !ticket.isDeleted()) {
                    myTickets.add(ticket);
                }
            }
        return myTickets;
    } 

    // Receives a priority, category, subject and description and Create a tickets then returns it.
    public Ticket createTicket(String priority, String category, String subject, String description) {
        return new Ticket(this, null, priority, category, subject, description);
    } 
    
    // after creating a ticket it calls ticket methods to auto-assign to agent and store in the repository
    public void submitTicket(Ticket ticket) {
        ticket.autoAssignAgent(getRepository().getAllUsers());
        getRepository().addTicket(ticket);
     } 
     
    // Receives a ticketID and reason, find the ticket. Only allows deletion if a ticket if it is OPEN - IN-PROGRESS then calls ticket methods
    public void deleteTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId); // throws an error if not found
        ticket.requireActive(); // The ticket should be OPEN / IN_PROGRESS
        ticket.deleteTicket(this, reason); 
     } 
     
    // Receives a ticketID and reason, find the ticket. Reopens a ticket only if its RESOLVED
    public void reopenTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId); // throws an error if not found
        ticket.reopenTicket(this, reason);
    } 
     
    // Receives a ticketID and reason, find the ticket. Adds a comment to the specified ticket
    public void addComment(String ticketId, String message) {
        Ticket ticket = getTicket(ticketId);
        ticket.addComment(new Comment(this, message));
    }

    // Receives a ticketID and reason, find the ticket. Close a ticket that is resolved and
    public void closeTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId); // throws an error if not found
        ticket.closeTicket(this, reason);
    } // Close aticket that is resolved

    

}