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
        if (ticket == null || ticket.getSubmittedBy() == null) { return null; }
        if (!ticket.getSubmittedBy().getUserId().equalsIgnoreCase(getUserId())) { return null; }
        return ticket;  
    }

    public Ticket createTicket(String priority, String category, String description) {
        return new Ticket(this, null, priority, category, description);
    } 

    public Comment createComment(String message){
        return new Comment(getUserId(), message);
    }
    
    public boolean submitTicket(Ticket ticket) {
        if (ticket == null) {return false; }
        assignTicket(ticket);
        getRepository().addTicket(ticket);
        return true;
     }
     
    public boolean cancelTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId);
        if (ticket == null) { return false;}
        if (!(ticket.getState().equalsIgnoreCase("OPEN") || ticket.getState().equalsIgnoreCase("IN_PROGRESS"))) { return false; }
           
        return ticket.changeState("CANCELLED", this, reason);
     }
     
    public boolean reopenTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId);
        if (ticket == null) { return false; }
        if (!ticket.getState().equalsIgnoreCase("RESOLVED")) {return false; }
        return ticket.changeState("IN_PROGRESS", this, reason);
     }
     
    public boolean addComment(String ticketId, String message) {
        Ticket ticket = getTicket(ticketId);
        if (ticket == null) { return false; }
        return ticket.addComment(createComment(message));
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
     
    public int getSubmittedTicketCount() {
          return getMyTickets().size();
    }

    public boolean closeTicket(String ticketId, String reason) {
        Ticket ticket = getTicket(ticketId);
        if (ticket == null ) { return false; }
        if (!ticket.getSubmittedBy().getUserId().equalsIgnoreCase(getUserId())) { return false; }
        return ticket.changeState("CLOSED", this, reason);
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

    private boolean assignTicket(Ticket ticket) {
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