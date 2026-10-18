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

    @Override
    public Ticket getTicket(String ticketId) {
        Ticket ticket = getRepository().findTicketById(ticketId);
        if (ticket == null) { 
            throw new IllegalArgumentException("Ticket " + ticketId +" was not found."); 
        }
        if (ticket.getSubmittedBy() == null || !ticket.getSubmittedBy().getUserId().equalsIgnoreCase(getUserId())) { 
            throw new SecurityException("You can only view your own tickets."); 
        }
        return ticket;  
    } // throws an exception if invalid Ticket ID / if you do not own ticket

    public Ticket createTicket(String priority, String category, String subject, String description) {
        return new Ticket(this, null, priority, category, subject, description);
    } 

    public Comment createComment(String message){
        return new Comment(this, message);
    }
    
    public void submitTicket(Ticket ticket) {
        if (ticket == null) { 
            throw new IllegalArgumentException("Cannot submit an empty Ticket"); 
        }
        autoAssignAgent(ticket); // tickets stays unassigned if no agent matches
        getRepository().addTicket(ticket);
     }
     
    public void cancelTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId); // throws an error if not found
        if (!(ticket.getState().equalsIgnoreCase("OPEN") || ticket.getState().equalsIgnoreCase("IN_PROGRESS"))) {
            throw new IllegalStateException("Ticket " + ticket.getTicketId() + " is " + ticket.getState() + ". Only OPEN or IN_PROGRESS tickets can be cancelled.");
        }
        ticket.changeState("CANCELLED", this, reason);
     }
     
    public void reopenTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId); // throws an error if not found
        if (!ticket.getState().equalsIgnoreCase("RESOLVED")) { 
            throw new IllegalStateException("Ticket " + ticket.getTicketId() + " is " + ticket.getState() + ". Only RESOLVED tickets can be reopened.");
        }
        ticket.changeState("IN_PROGRESS", this, reason);
    }
     
    public void addComment(String ticketId, String message) {
        Ticket ticket = getTicket(ticketId);
        ticket.addComment(createComment(message));
    }

    public void closeTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId); // throws an error if not found
        if (!ticket.getState().equalsIgnoreCase("RESOLVED")) { 
            throw new IllegalStateException("Ticket " + ticket.getTicketId() + " is " + ticket.getState() + ". Only RESOLVED tickets can be closed.");
        }
        ticket.changeState("CLOSED", this, reason);
    }

    public ArrayList<Ticket> getMyTickets() {
        ArrayList<Ticket> myTickets = new ArrayList<>();
        ArrayList<Ticket> allTickets = getRepository().getAllTickets();
        if (allTickets == null ) { return myTickets; }
        for (Ticket ticket : allTickets) {
                if (ticket.getSubmittedBy().getUserId().equalsIgnoreCase(getUserId())) {
                    myTickets.add(ticket);
                }
            }
        return myTickets;
    }
    
    public ArrayList<Ticket> getActiveTickets() {
        ArrayList<Ticket> myTickets = new ArrayList<>();
        for (Ticket ticket : getMyTickets()) {
            String state = ticket.getState();
            if (state.equalsIgnoreCase("OPEN") || state.equalsIgnoreCase("IN_PROGRESS")) {
                myTickets.add(ticket);
            }
        }
        return myTickets;
     } 

    private boolean autoAssignAgent(Ticket ticket) {
        ArrayList<User> users = getRepository().getAllUsers();
        if (users == null) { return false; }

        SupportAgent bestAgent = null;
        int fewestActiveTickets = Integer.MAX_VALUE;

        for (User user : users) {
            if (!(user instanceof SupportAgent agent)) { continue; }
            if (agent.getSpecialization() == null ) { continue; }
            if (!agent.getSpecialization().equalsIgnoreCase(ticket.getCategory())) { continue; }
            
            int activeCount = agent.getActiveTickets().size();
            if (activeCount < fewestActiveTickets) {
                fewestActiveTickets = activeCount;
                bestAgent = agent;
            }
        }

        if (bestAgent != null) { 
            ticket.setAssignedAgent(bestAgent);
            return true;
        } 
        return false;
    } 

}