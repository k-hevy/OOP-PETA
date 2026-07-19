 class EventDetails {
    String eventName;
    String eventID;
    String venue;
    String eventDate;
    int numberOfParticipants;
    
  void displayEventDetails(){
      System.out.println("Event Name: "+eventName);
      System.out.println("Event ID: "+eventID);
      System.out.println("Event venue: "+venue);
      System.out.println("Event Date: "+eventDate); 
      System.out.println("Number of participants: " +numberOfParticipants);
  }
}
