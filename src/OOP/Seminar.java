package OOP;
public class Seminar extends CampusEvent {
    String speakerName;
    String seminarTopic;
    
    Seminar(String eventName,String venue, int maximumParticipants, String speakerName, String seminarTopic){
      super(eventName, venue, maximumParticipants);  
      this.speakerName= speakerName;
      this.seminarTopic = seminarTopic;
    }
    
    void displayEventDetails(){
        super.displayEventDetails();
        System.out.println("Speaker: "+speakerName);
        System.out.println("Topic: "+seminarTopic);
    }
}
