package entitylayer;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Ticket {

    private final LocalDateTime dateSubmitted;
    private String state;
    private final String ticketId;
    private final Requestor submittedBy;
    private SupportAgent assignedAgent;
    private String priority;
    private String category;
    private String subject;
    private String description;
    private final ArrayList<Comment> comments;
    private final ArrayList<TicketLogEntry> ticketLogEntries;
    private static int ticketCounter = 0;

    public Ticket(Requestor submittedBy, SupportAgent assignedTo, String priority, String category, String subject, String description) {
        this.ticketId = generateTicketId();
        this.submittedBy = submittedBy;
        this.assignedAgent = assignedTo;
        this.priority = priority;
        this.category = category;
        this.subject = subject;
        this.dateSubmitted = LocalDateTime.now();
        this.state = "OPEN";
        this.description = description;
        comments = new ArrayList<>();
        ticketLogEntries = new ArrayList<>();
        ticketLogEntries.add(new TicketLogEntry("OPEN", "OPEN", submittedBy.getUserId(), submittedBy.getFirstName(), "Ticket Created"));
    }
    
    // Getters
    public String getTicketId() { return ticketId; }
    public Requestor getSubmittedBy() { return submittedBy;}
    public SupportAgent getAssignedAgent() {return assignedAgent;}
    public String getPriority() {return priority;}
    public String getCategory() {return category;}
    public LocalDateTime getDateSubmitted() {return dateSubmitted;}
    public String getState() {return state;}
    public String getSubject() { return subject; }
    public String getDescription() {return description;}
    public ArrayList<Comment> getComments() {return new ArrayList<>(comments);}
    public ArrayList<TicketLogEntry> getTicketLogEntries() {return new ArrayList<>(ticketLogEntries);}

    // setters
    public void setAssignedAgent(SupportAgent agent) {this.assignedAgent = agent;}
    public void setCategory(String category) {this.category = category; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setDescription(String description) {this.description = description;}

    public boolean addComment(Comment comment) {
        if (comment == null) { return false; }
        if (state.equalsIgnoreCase("CLOSED") || state.equalsIgnoreCase("CANCELLED")) { return false; }
        comments.add(comment);
        return true;
    }

    public boolean changeState(String newState, User changedBy, String reason) {
        if (newState == null  || changedBy == null || reason == null) { return false; } 
        if (!isValidTransition(state, newState)) { return false; }
        String oldState = this.state;
        this.state = newState.toUpperCase();
        ticketLogEntries.add(new TicketLogEntry(oldState, this.state, changedBy.getUserId(), changedBy.getFirstName(), reason.trim()));
        return true;
    }
    
    public boolean reassignTicket(SupportAgent newAgent, User changedBy, String reason) {
        if (newAgent == null || changedBy == null || reason == null) { return false; }
        if (!(state.equalsIgnoreCase("OPEN") || state.equalsIgnoreCase("IN_PROGRESS"))) { return false; } 
        if (newAgent == this.assignedAgent) return false;
        setAssignedAgent(newAgent);
        ticketLogEntries.add(new TicketLogEntry(state, state, changedBy.getUserId(), changedBy.getFirstName(), "Reassigend to " + newAgent.getFirstName() + ": " + reason));
        return true;
    }

    private boolean isValidTransition(String oldState, String newState) {
        return (oldState.equalsIgnoreCase("OPEN") && newState.equalsIgnoreCase("IN_PROGRESS")) ||
               (oldState.equalsIgnoreCase("IN_PROGRESS") && newState.equalsIgnoreCase("RESOLVED")) ||
               (oldState.equalsIgnoreCase("RESOLVED") && newState.equalsIgnoreCase("CLOSED")) ||
               (oldState.equalsIgnoreCase("OPEN") && newState.equalsIgnoreCase("CANCELLED")) ||
               (oldState.equalsIgnoreCase("IN_PROGRESS") && newState.equalsIgnoreCase("CANCELLED")) ||
               (oldState.equalsIgnoreCase("RESOLVED") && newState.equalsIgnoreCase("IN_PROGRESS"));
    }    
    
    public boolean changePriority(String newPriority, User changedBy) {
        if (newPriority == null || changedBy == null) { return false; }
        if (newPriority.equalsIgnoreCase(getPriority())) { return false; } 
        if (!newPriority.equalsIgnoreCase("URGENT") && 
            !newPriority.equalsIgnoreCase("HIGH") && 
            !newPriority.equalsIgnoreCase("MEDIUM") && 
            !newPriority.equalsIgnoreCase("LOW")) {
            return false;
        } 
        ticketLogEntries.add(new TicketLogEntry(getState(), getState(), changedBy.getUserId(), changedBy.getFirstName(), "Changed Priority From: " + getPriority() + " -> " + newPriority));
        this.priority = newPriority;
        return true;
    } 

    private String generateTicketId() {
        ticketCounter++;
        return String.format("TK-%03d", ticketCounter);
    } // called whenever a new tikcet is created

    public String getFormattedTicket() {
        String agent = (assignedAgent == null) ? "Unassigned" : assignedAgent.getFullName();
        return String.format(
            "Ticket ID      : %s\nState          : %s\nPriority       : %s\nCategory       : %s\nDate Submitted : %s\nSubmitted By   : %s\nAssigned To    : %s\nDescription    : %s\nComments       : %d\nLog Entries    : %d",
            getTicketId(), getState(), getPriority(), getCategory(), getDateSubmitted(), 
            getSubmittedBy().getFirstName(), agent, getDescription(), getComments().size(), getTicketLogEntries().size()
        );
    } // will remove soon when UI, remove nalang when may sa UI sa

}
