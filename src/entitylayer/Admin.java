package entitylayer;

import datalayer.Repository;

public class Admin extends User {

    private String employeeId;
    private String managedDepartment;
    private String deskExtension;

    public Admin(String userId, String firstName, String lastName, String email, String password, Repository repository, 
                String employeeId, String managedDepartment, String deskExtension) {
        super(userId, firstName, lastName, email, password, repository);
        this.employeeId = employeeId;
        this.managedDepartment = managedDepartment;
        this.deskExtension = deskExtension;
    }


    @Override
    public String getRole() {
       return "ADMIN";
    }

    // String getEmployeeId()
    // void setEmployeeId(String employeeId)
    // String getManagedDepartment()
    // void setManagedDepartment(String managedDepartment)
    // String getDeskExtension()
    // void setDeskExtension(String deskExtension)
    
    // boolean reassignTicket(String ticketId, String agentId)
    // boolean rejectTicket(String ticketId, String reason)
    // ArrayList<User> filterUsers(String field, String value)
    // ArrayList<Ticket> filterTickets(String field, String value)
    // ArrayList<TicketLogEntry> getTicketHistory(String ticketId)
    // ArrayList<Ticket> getAllTickets()
    // ArrayList<User> getAllUsers()
    // HashMap<String, Integer> getSummaryCounts() 


}