package entitylayer;

import datalayer.Repository;
import java.util.ArrayList;

public class Requestor extends User {

    public Requestor(String userId, String firstName, String lastName, String email, String password, Repository repository) {
        super("REQUESTOR",userId, firstName, lastName, email, password, repository);
    }

    @Override
    public Ticket getTicket(String ticketId) {
        Ticket ticket = getRepository().findTicketById(ticketId);
        if (ticket == null || ticket.getState().equalsIgnoreCase("DELETED")) { 
            throw new IllegalArgumentException("Ticket " + ticketId +" was not found."); 
        } // Throw an error if you try to acces a ticket that doesnt exist
        if (!ticket.getSubmittedBy().getUserId().equalsIgnoreCase(getUserId())) { 
            throw new SecurityException("Ticket " + ticketId + " is not yours."); 
        } // Throw an Error if you try to access a ticket that isnt your
        return ticket;  
    } // throws an exception if invalid Ticket ID / if you do not own ticket

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
    } // Returns all tickets submitted by the requestor except Deleted ones

    public Ticket createTicket(String priority, String category, String subject, String description) {
        return new Ticket(this, null, priority, category, subject, description);
    } // Create a ticket 
    
    public void submitTicket(Ticket ticket) {
        autoAssignAgent(ticket); // tickets stays unassigned if no agent matches
        getRepository().addTicket(ticket);
     } // after creating a ticket auto assign to agent and store in the repo
     
    public void deleteTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId); // throws an error if not found
        ticket.requireActive(); // The ticket should be OPEN / IN_PROGRESS
        ticket.deleteTicket(this, reason); 
     } // Deletes a ticket if it is OPEN - IN-PROGRESS
     
    public void reopenTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId); // throws an error if not found
        ticket.reopenTicket(this, reason);
    } // Reopens a ticket only if its RESOLVED
     
    public void addComment(String ticketId, String message) {
        Ticket ticket = getTicket(ticketId);
        ticket.addComment(new Comment(this, message));
    }

    public void closeTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId); // throws an error if not found
        ticket.closeTicket(this, reason);
    } // Close aticket that is resolved

    private void autoAssignAgent(Ticket ticket) {

        ArrayList<User> users = getRepository().getAllUsers();

        SupportAgent bestAgent = null;
        int fewestActiveTickets = Integer.MAX_VALUE;

        for (User user : users) {
            if (!(user instanceof SupportAgent agent)) { continue; } // User must be a supportAgent
            if (!agent.getSpecialization().equalsIgnoreCase(ticket.getCategory())) { continue; } // SupportAgent specializaiton must be thhe same as ticket category
            
            int activeCount = agent.getActiveTickets().size();
            if (activeCount < fewestActiveTickets) {
                fewestActiveTickets = activeCount;
                bestAgent = agent;
            } // Pick the agent with fewest active tickets that specialization matchhes the category of the ticket 
        }

        if (bestAgent != null) { 
            ticket.setAssignedAgent(bestAgent);
        } // if there is a best agent assign the ticket

        // do nothing if there isnt an agent available

    } // Picks the best agent for the job

}