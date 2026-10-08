package entitylayer;

import java.time.LocalDate;
import java.util.ArrayList;

public class Ticket {

    private String ticketId;
    private Requestor submittedBy;
    private SupportAgent assignedAgent;
    private String priority;
    private String category;
    private LocalDate dateSubmitted;
    private String state;
    private String description;
    private ArrayList<Comment> comments;
    private ArrayList<TicketLogEntry> ticketLogEntries;

    public Ticket(String ticketId, Requestor submittedBy, SupportAgent assignedTo, String priority, String category, String description) {
        this.ticketId = ticketId; // automatically assigned
        this.submittedBy = submittedBy;
        this.assignedAgent = assignedTo;
        this.priority = priority;
        this.category = category;
        this.dateSubmitted = LocalDate.now();
        this.state = "OPEN";
        this.description = description;
        comments = new ArrayList<>();
        ticketLogEntries = new ArrayList<>();
    }
    
    // Getters
    public String getTicketId() { return ticketId; }
    public Requestor getSubmittedBy() { return submittedBy;}
    public SupportAgent getAssignedAgent() {return assignedAgent;}
    public String getPriority() {return priority;}
    public String getCategory() {return category;}
    public LocalDate getDateSubmitted() {return dateSubmitted;}
    public String getState() {return state;}
    public String getDescription() {return description;}
    public ArrayList<Comment> getComments() {return comments;}
    public ArrayList<TicketLogEntry> getTicketLogs() {return ticketLogEntries;}

    // setters
    public void setAssignedAgent(SupportAgent agent) {this.assignedAgent = agent;}
    public void setPriority(String priority) {this.priority = priority;}
    public void setCategory(String category) {this.category = category;}
    public void setDescription(String description) {this.description = description;}
    public void addComment(Comment comment) {comments.add(comment);}
    public boolean changeState(String newState, User changedBy, String reason) {
        String oldState = this.state;
        if (isValidTransition(state, newState)) {
            this.state = newState;
            ticketLogEntries.add(new TicketLogEntry(oldState, newState, changedBy.getUserId(), changedBy.getFirstName(), reason));
            return true;
        } else {
            return false;
        }
    }
    public String getFormattedTicket() {
        return String.format(
            "Ticket ID      : " + getTicketId() + "\n" + 
            "State          : " + getState() + "\n" + 
            "Priority       : " + getPriority() + "\n" + 
            "Category       : " + getCategory() + "\n" + 
            "Date Submitted : " + getDateSubmitted() + "\n" + 
            "Submitted By   : " + getSubmittedBy().getFirstName() + "\n" + 
            "Assigned To    : " + getAssignedAgent().getFirstName() + "\n" + 
            "Description    : " + getDescription() + "\n" + 
            "Comments       : " + getComments().size() + "\n" +  
            "Log Entries    : " + getTicketLogs().size()
        );
}

    // Helper methods
    private boolean isValidTransition(String oldState, String newState) {
        if (
            (oldState.equalsIgnoreCase("OPEN") && newState.equalsIgnoreCase("IN_PROGRESS")) ||
            (oldState.equalsIgnoreCase("IN_PROGRESS") && newState.equalsIgnoreCase("RESOLVED")) ||
            (oldState.equalsIgnoreCase("RESOLVED") && newState.equalsIgnoreCase("CLOSED")) ||
            (oldState.equalsIgnoreCase("OPEN") && newState.equalsIgnoreCase("CANCELLED")) ||
            (oldState.equalsIgnoreCase("IN_PROGRESS") && newState.equalsIgnoreCase("CANCELLED")) ||
            (oldState.equalsIgnoreCase("RESOLVED") && newState.equalsIgnoreCase("IN_PROGRESS"))
        ) {
            return true;
        } else {
            return false;
        }
    }
    private boolean isOverdue() {return false;} // N/A for now sicne this is for SLAs
    
    
}
