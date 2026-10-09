package applicationlayer;

import datalayer.Repository;
import entitylayer.Admin;
import entitylayer.Comment;
import entitylayer.Requestor;
import entitylayer.SupportAgent;
import entitylayer.Ticket;
import entitylayer.TicketLogEntry;
import entitylayer.User;
import java.util.Scanner;
import presentationlayer.BaseMenu;

public class CampusHelpDeskTicketingSystem {

    private static Repository repository;
    private static Scanner input;
    private static User currentUser;
    private static BaseMenu menu;

    public static void main(String[] args) {

        System.out.println("Campus Help Desk Ticketing System");

        repository = new Repository();
        // input = new Scanner(System.in);
        //  Login menu here then store User --> call appropriate menu

        Requestor requestor = new Requestor("001", "kean", "Danganan", "keandangan", "requestor123", repository);
        SupportAgent supportAgent = new SupportAgent("002", "Grace", "Mendiola", "mendiola@gmail.com", "supportAgent123", repository, "001", "FACILITIES_MAINTENANCE", "09123456789");
        Admin admin = new Admin("003", "trisha", "Garcia", "garcia@gmail.com", "admin123", repository, "002", "cict", "10234546578"); // create accounts for each type
        
        repository.addUser(requestor);
        repository.addUser(supportAgent);
        repository.addUser(admin); // store accounts in repo
        

//        Ticket t1 = new Ticket("001", requestor, supportAgent, "LOW", "FACILITIES_MAINTENANCE", ""); // create ticket 
//        repository.addTicket(t1); //store ticket
         Ticket t1 = requestor.createTicket("LOW", "FACILITIES_MAINTENANCE", "Broken Chair");
         requestor.submitTicket(t1);
        
        System.out.println("Ticket ID: " + t1.getTicketId());
        System.out.println("Total tickets: " + repository.getAllTickets().size());
        
        Ticket t2 = requestor.createTicket("HIGH", "FACILITIES_MAINTENANCE", "Broken PC");
             requestor.submitTicket(t2);

            System.out.println("Ticket ID: " + t2.getTicketId());
            System.out.println("Total tickets: " + repository.getAllTickets().size());

        System.out.println();     
        System.out.println(t1.getFormattedTicket()); // view ticketdetails
        System.out.println(); 
        System.out.println(t2.getFormattedTicket()); 

        requestor.addComment(t1.getTicketId(), "hello test comment");
         requestor.addComment(t1.getTicketId(), "hello hello test comment");
        requestor.addComment(t2.getTicketId(), "hello test comment, no comment");
       // t1.addComment(new Comment("001", requestor.getUserId(), requestor.getFirstName(), "hello test comment")); // Commenting
        t1.changeState("IN_PROGRESS", supportAgent, "Starting to work on it"); // changing status
        // System.out.println(t1.changeState("OPEN", supportAgent, "IDK")); // Sample of invalid state change
        t1.changeState("RESOLVED", supportAgent, "Resolved");
       // requestor.addComment(new Comment("001", requestor.getUserId(), requestor.getFirstName(), "hello test comment, no comment")); // Commenting
        t2.changeState("IN_PROGRESS", supportAgent, "Starting to work on it"); 
      
        System.out.println();    
        
        
        for (Comment comment : t1.getComments()) {
            System.out.println(comment.getFormattedComment());
        } // looking at all comments

        System.out.println();    
        for (TicketLogEntry ticketLogEntry : t1.getTicketLogs()) {
            System.out.println(ticketLogEntry.getFormattedLogEntry());
        } // looking at logs
        System.out.println(); 
          for (Comment comment : t2.getComments()) {
            System.out.println(comment.getFormattedComment());
        } // looking at all comments

        System.out.println();    
        for (TicketLogEntry ticketLogEntry : t2.getTicketLogs()) {
            System.out.println(ticketLogEntry.getFormattedLogEntry());
        } // looking at logs 
        
        System.out.println();    
        System.out.println(t1.getFormattedTicket());
         System.out.println();    
        System.out.println(t2.getFormattedTicket());
        // Looking at updated Tickte







        


    }

// CampusHelpDeskTicketingSystem()
// static void main(String[] args)
// void run()
// void launchMenu(User user)
// private void seedData()
// private void logout()
// private void exit() 



}
