package OOP;
public class CampusEventManagement {
    public static void main(String[] args) {
        
        CampusEvent event = new CampusEvent("Science Fair", "Gymnasium", 120);
        Seminar seminar = new Seminar("Data privacy Seminar", "Auditorium", 100, "Anton Mercadejas", "Introduction to Data Protection");
        JobFair jobFair= new JobFair("Campus Job Fair","Valencia Hall",80,"Accenture","Information Technology","Software Developer");

        event.displayEventDetails();
        event.registerParticipant();
        event.registerParticipant();
        event.cancelRegistration();
        event.displayEventDetails();

        System.out.println();
        seminar.displayEventDetails();
        seminar.registerParticipant();
        seminar.registerParticipant();
        seminar.registerParticipant();
        seminar.displayEventDetails();
        
        System.out.println();
        jobFair.registerParticipant();
        jobFair.cancelRegistration();
        jobFair.registerParticipant();
        jobFair.registerParticipant();
        jobFair.applyForJob();
        jobFair.displayEventDetails();
    }
}
