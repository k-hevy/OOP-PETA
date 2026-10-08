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
        input = new Scanner(System.in);
        //  Login menu here then store User --> call appropriate menu

        Requestor requestor = new Requestor("001", "kean", "Danganan", "keandangan", "requestor123", repository);
        SupportAgent supportAgent = new SupportAgent("002", "Grace", "Mendiola", "mendiola@gmail.com", "supportAgent123", repository, "001", "FACILITIES_MAINTENANCE", "09123456789");
        Admin admin = new Admin("003", "trisha", "Garcia", "garcia@gmail.com", "admin123", repository, "002", "cict", "10234546578"); // create accounts for each type
        
        repository.addUser(requestor);
        repository.addUser(supportAgent);
        repository.addUser(admin); // store accounts in repo
        

        Ticket t1 = new Ticket("001", requestor, supportAgent, "LOW", "FACILITIES_MAINTENANCE", ""); // create ticket 
        repository.addTicket(t1); //store ticket


        System.out.println();     
        System.out.println(t1.getFormattedTicket()); // view ticketdetails


        t1.addComment(new Comment("001", requestor.getUserId(), requestor.getFirstName(), "hello test comment")); // Commenting
        t1.changeState("IN_PROGRESS", supportAgent, "Starting to work on it"); // changing status
        
        
        // System.out.println(t1.changeState("OPEN", supportAgent, "IDK")); // Sample of invalid satte change
        

        System.out.println();    
        for (Comment comment : t1.getComments()) {
            System.out.println(comment.getFormattedComment());
        } // looking at all comments

        System.out.println();    
        for (TicketLogEntry ticketLogEntry : t1.getTicketLogs()) {
            System.out.println(ticketLogEntry.getFormattedLogEntry());
        } // looking at logs

        System.out.println();    
        System.out.println(t1.getFormattedTicket());
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
