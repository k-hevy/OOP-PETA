package OOP;
public class JobFair extends CampusEvent{
   String companyName;
   String industry;
   String jobPosition;
   
   JobFair(String eventName,String venue, int maximumParticipants, String companyName, String industry, String jobPosition){
      super(eventName, venue, maximumParticipants);  
      this.companyName = companyName;
      this.industry =  industry;
      this.jobPosition = jobPosition;
   }
   
   void applyForJob() {
        System.out.println("Application is submitted for the position of: " + jobPosition);
    }

    void displayEventDetails(){
        super.displayEventDetails();
        System.out.println("Name of the Company: "+companyName);
         System.out.println("Industry: " + industry);
        System.out.println("Job position open: "+jobPosition);
    }
}
