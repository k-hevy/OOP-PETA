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

   
    public Ticket createTicket(String priority, String category, String description){
        String ticketId = getRepository().generateTicketId();
         
         Ticket ticket = new Ticket(ticketId, this, null, priority, category, description);
        
        return ticket;
    } 
    
    Comment createComment(String message){
      String commentId = getRepository().generateCommentId();

        return new Comment(commentId,this.getUserId(),this.getFirstName(),message);
    }
    
     public boolean submitTicket(Ticket ticket) {
            assignTicket(ticket);
            getRepository().addTicket(ticket);
        return true;
     }
     
     boolean cancelTicket(String ticketId, String reason){
          Ticket ticket = getRepository().findTicketById(ticketId);

            if (ticket != null && ticket.getSubmittedBy() == this) {
                return ticket.changeState("CANCELLED", this, reason);
            }
         return false;
     }
     
     boolean reopenTicket(String ticketId, String reason){
            Ticket ticket = getRepository().findTicketById(ticketId);

                if (ticket != null && ticket.getSubmittedBy() == this) {
                    return ticket.changeState("IN_PROGRESS", this, reason);
                }
         return false;
     }
     
     public boolean addComment(String ticketId, String message){
          Ticket ticket = getRepository().findTicketById(ticketId);
         
          if (ticket != null && ticket.getSubmittedBy() == this) {
                Comment comment = createComment(message);
                ticket.addComment(comment);
                return true;
               }
        return false;
     }
     
     Ticket getMyTicket(String ticketId){
         Ticket ticket = getRepository().findTicketById(ticketId);

           if (ticket != null && ticket.getSubmittedBy() == this) {
              return ticket;
           }

        return null;  
     }
     
     ArrayList<Ticket> getMyTickets(){
          ArrayList<Ticket> myTickets = new ArrayList<>();

            for (Ticket ticket : getRepository().getAllTickets()) {
                if (ticket.getSubmittedBy() == this) {
                    myTickets.add(ticket);
                }
              }

          return myTickets;
        }
     
       int getSubmittedTicketCount(){
          return getMyTickets().size();
      }
     
      ArrayList<TicketLogEntry> getTicketHistory(String ticketId){
         Ticket ticket = getMyTicket(ticketId);

            if (ticket != null) {
                return ticket.getTicketLogs();
            }
          return new ArrayList<>();
       }
     
     boolean closeTicket(String ticketId, String reason){
        Ticket ticket = getMyTicket(ticketId);

            if (ticket != null) {
                return ticket.changeState("TICKET CLOSED", this, reason);
            }
          return false;
      }

     private int getActiveTicketCount(SupportAgent supportAgent){
         int count = 0;

          for (Ticket ticket : getRepository().getAllTickets()) {
              if (ticket.getAssignedAgent() == supportAgent &&
                  (ticket.getState().equalsIgnoreCase("ACTIVE") ||
                   ticket.getState().equalsIgnoreCase("IN PROGRESS"))) {
                  count++;
              }
          }
          return count;
     }
     
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