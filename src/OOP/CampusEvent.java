package OOP;
public class CampusEvent {
    String eventName;
    String venue;
    int maximumParticipants;
    int registeredParticipants;

 CampusEvent(String eventName, String venue, int maximumParticipants){
     this.eventName=eventName;
     this.venue=venue;
     this.maximumParticipants = maximumParticipants;
     registeredParticipants = 0;
   }
 void registerParticipant(){
     if(registeredParticipants<maximumParticipants){
      registeredParticipants++;
      System.out.println("Registration successful!");}
     else{
         System.out.println("Registration failed. The event is already full. Thank you!");
     }  
  }
 void cancelRegistration(){
     if(registeredParticipants<=0){
         System.out.println("Cancellation failed. There are no registered participants.");
     }
         else{
        registeredParticipants--;
        System.out.println("Cancellation of participant registration was successful!");
  }}
 void displayEventDetails(){
     System.out.println("Name of the event: "+eventName);
     System.out.println("Venue: "+venue);
     System.out.println("Maximum number of Participants: "+maximumParticipants);
     System.out.println("Registered number of participants: "+registeredParticipants);
    }
}

