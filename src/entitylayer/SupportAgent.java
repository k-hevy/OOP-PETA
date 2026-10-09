package entitylayer;

import datalayer.Repository;

public class SupportAgent extends User {

    private String employeeId;
    private String specialization;
    private String deskExtension;


    public SupportAgent(String userId, String firstName, String lastName, String email, String password, Repository repository, 
                        String empoyeeId, String specialization, String deskExtension) {
        super(userId, firstName, lastName, email, password, repository);
        this.employeeId = empoyeeId;
        this.specialization = specialization;
        this.deskExtension = deskExtension;
    }

    @Override
    public String getRole() {
       return "SUPPORTAGENT";
    }

    // String getEmployeeId()
    // void setEmployeeId(String employeeId)
    public String getSpecialization() {
        return specialization;
    }
    // void setSpecialization(String specialization)
    // String getDeskExtension()
    // void setDeskExtension(String deskExtension)


    // Comment createComment(String message)
    // boolean addComment(String ticketId, String message)
    // boolean updateTicketStatus(String ticketId, String newState, String reason)
    // boolean escalateTicket(String ticketId, String newPriority)
    // Ticket getAssignedTicket(String ticketId)
    // ArrayList<Ticket> getAssignedTickets()
    // int getTicketsResolvedCount()
    // ArrayList<Ticket> getOverdueTickets()


}