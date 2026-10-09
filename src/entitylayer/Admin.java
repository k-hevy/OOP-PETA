package entitylayer;

import datalayer.Repository;

public class Admin extends User{
    private String employeeId;
    private String managedDepartment;
    private String deskExtension;

}
public Admin(String userId, String firstName, String lastName, String email, String password, Repository repository, String employeeId, String managedDepartment, String deskExtensiion) {
    super(userId, firstName, lastName, email, password, repository);

@Override 
public String getRole() {
    return "ADMIN";
}

public void setEmployeeId(String employeeId){
    this.employeeId = employeeId;
}
public void setManagedDepartment(String managedDepartment){
    this.managedDepartment = managedDepartment;
}
public void setDeskExtension(String deskExtensiion){
    this.deskExtensiion = deskExtensiion;
}
   public boolean reassignTicket(String ticketId, String agentId){
            Ticket targetTicket = findTicketbyId(ticketId);
        if(targetTicket==null) {
            return false;
        }
}
   public boolean rejectTicket(String ticketId, String reason){
            Ticket targetTicket = rejectTicketbyId(ticketId);
        if(targetTicket==null) {
            return false;
        }
}
    
    public ArrayList<TicketLogEntry> getTicketHistory(String ticketId){
        ArrayList<TicketLogEntry> history = new ArrayList();

        if(ticketId==null || ticketId.trim().isEmpty()){
            return history;
        }
    }

    public ArrayList<Ticket> getAllTickets(){
        if (this.ticket == null) {
            return new ArrayList<>(this.tickets);
        }
    }
    public ArrayList<User> getAllUsers(){
        if(this.users==null{
            return new ArrayList<>(this.users)
        })
    }
    public Hashmap<String, Integer> getSummaryCounts() {
        HashMap <String, Integer> counts = new HashMap<>();

        int totalTickets = (this.tickets != null) ? this.ticket.size() : 0;
        int totalUsers = (this.users != null) ? this.users.size() : 0;

        counts.put("totalTickets", totalTickets);
        counts.put("totalUsers", totalUsers);

        int openCount = 0; 
        int inProgressCount = 0;
        int closedCount = 0;

        if(this.tickets != null) {
            for (Ticket ticket : this.tickets) {
                if(ticket.getState() != null) {
                    switch(tickets.getState().toUpperCase()){
                        case "OPEN":
                            openCount++;
                            break;
                        case "RESOLVED":
                        case "CLOSED":
                            closedCount++;
                            break;
                        case "IN_PROGRESS":
                            inProgressCount+=;
                            break;
                        
                            
                    }
                }
            }
        }
        counts.put("openTickets", openCount);
        counts.put("closedTickets", closedCount);
        counts.put("inProgressTickets", inProgressCount);
        
        return counts;
    }

}