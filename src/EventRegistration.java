import java.util.*;
public class EventRegistration {
    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);
     EventDetails per1 = new EventDetails();
     per1.eventName = "Debut";
     per1.eventID = "09080706";
     per1.venue = "Siria Resort";
     per1.eventDate = "7/18/26";
     per1.numberOfParticipants = 200;
     
  System.out.println("[1] Register\n[2] Cancel Registration");
  System.out.print("Enter here: ");
  int inp = scan.nextInt();
     switch(inp){
         case 1:{
               registerParticipants();
               per1.numberOfParticipants++;
               per1.displayEventDetails();
         break;}
         case 2:{
            System.out.println("Below is your registered details, Please check the details before you proceed to cancel.");
                per1.displayEventDetails();
                System.out.println("[P] tO Proceed\n[B] to terminate cancelling");
                System.out.print("Enter: ");
                        char per=scan.next().charAt(0);
              switch(per){
                   case 'P':{
                      cancelRegistration(); 
                      per1.numberOfParticipants--;
                      System.out.print("Number of Participants becomes: "+per1.numberOfParticipants);
                   break;}
                   case 'B':{
                       System.out.print("Great! You stop the cancellation.");
                   break;    }}
         break;
       }}}
    static void cancelRegistration(){
        System.out.println("Successfull cancelation of registration.");  }
    static void registerParticipants(){
        System.out.println("You have been registered. Thank You.");      
 }}
