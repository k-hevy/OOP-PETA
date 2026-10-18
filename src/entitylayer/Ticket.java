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

    public Ticket(Requestor submittedBy, SupportAgent assignedAgent, String priority, String category, String subject, String description) {
        this.ticketId = generateTicketId();
        this.submittedBy = submittedBy;
        this.assignedAgent = assignedAgent;
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
    public String getDateSubmitted() {return dateSubmitted.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));}
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

    public void addComment(Comment comment) {
        if (comment == null) { 
            throw new IllegalArgumentException("Comment cannot be empty"); 
        }
        requireNotFinished();
        comments.add(comment);
    }

    public void changeState(String newState, User changedBy, String reason) {
        if (newState == null  || changedBy == null || reason == null) { 
            throw new IllegalArgumentException("State, user, and reason are required."); 
        } 
        requireTransition(newState);
        String oldState = this.state;
        this.state = newState.toUpperCase();
        ticketLogEntries.add(new TicketLogEntry(oldState, this.state, changedBy.getUserId(), changedBy.getFirstName(), reason.trim()));
    }
    
    public boolean reassignTicket(SupportAgent newAgent, User changedBy, String reason) {
        if (newAgent == null || changedBy == null || reason == null) { 
            throw new IllegalStateException("Agent, user, and reason cannot be empty"); 
        }
        requireActive();
        if (newAgent == this.assignedAgent) { 
            throw new IllegalArgumentException("Ticket " + ticketId + " is already assigned to " + newAgent.getFirstName() + "."); 
        }

        if (!newAgent.getSpecialization().equalsIgnoreCase(category)) {
            throw new IllegalArgumentException("Ticket " + ticketId + " category ("+ getCategory() +") does not match " + newAgent.getFirstName() +" ("+ newAgent.getSpecialization() + ") specialization."); 
        }

        String oldAgentName = (assignedAgent != null) ? getAssignedAgent().getFullName() : "Unassigned";
        String newAgentName = newAgent.getFullName();
        setAssignedAgent(newAgent);
        ticketLogEntries.add(new TicketLogEntry(oldAgentName, newAgentName, changedBy.getUserId(), changedBy.getFirstName(), reason));
        return true;
    }
    
    public void changePriority(String newPriority,  String reason, User changedBy) {
        if (newPriority == null || changedBy == null) { 
            throw new IllegalArgumentException("Priority and User are required"); 
        }
        requireNotFinished();
        if (newPriority.equalsIgnoreCase(getPriority())) { 
            throw new IllegalArgumentException("Ticket " + ticketId + " is already " + priority + " priority"); 
        } 
        ticketLogEntries.add(new TicketLogEntry(priority, newPriority.toUpperCase(), changedBy.getUserId(), changedBy.getFirstName(), reason));
        this.priority = newPriority;
    } 

    private String generateTicketId() {
        ticketCounter++;
        return String.format("TK-%03d", ticketCounter);
    } // called whenever a new tikcet is created

    private boolean isValidTransition(String oldState, String newState) {
        return (oldState.equalsIgnoreCase("OPEN") && newState.equalsIgnoreCase("IN_PROGRESS")) ||
               (oldState.equalsIgnoreCase("IN_PROGRESS") && newState.equalsIgnoreCase("RESOLVED")) ||
               (oldState.equalsIgnoreCase("RESOLVED") && newState.equalsIgnoreCase("CLOSED")) ||
               (oldState.equalsIgnoreCase("OPEN") && newState.equalsIgnoreCase("CANCELLED")) ||
               (oldState.equalsIgnoreCase("IN_PROGRESS") && newState.equalsIgnoreCase("CANCELLED")) ||
               (oldState.equalsIgnoreCase("IN_PROGRESS") && newState.equalsIgnoreCase("OPEN")) ||
               (oldState.equalsIgnoreCase("RESOLVED") && newState.equalsIgnoreCase("IN_PROGRESS"));
    }    

    public void requireNotFinished() {
        if (state.equalsIgnoreCase("CLOSED")|| state.equalsIgnoreCase("CANCELLED")) {
            throw new IllegalStateException("Ticket " + ticketId + " is " + state + " and can no longer be changed.");
        }
    }

    public void requireActive() {
        if (!(state.equals("OPEN") || state.equalsIgnoreCase("IN_PROGRESS"))) {
            throw new IllegalStateException("Ticket " + ticketId + " is " + state + ". Only OPEN or IN_PROGRESS tickets allow this.");
        }
    }

    public void requireResolved() {
        if (!state.equals("RESOLVED")) {
            throw new IllegalStateException("Ticket " + ticketId + " is " + state + ". Only RESOLVED tickets allow this.");
        }
    }

    public void requireTransition(String newState) {
        if (!isValidTransition(state, newState)) {
            throw new IllegalStateException("Ticket " + ticketId + " cannot move from " + state + " to " + newState + ".");
        }
    }

    public boolean isActive() {
        return (state.equalsIgnoreCase("OPEN") || state.equalsIgnoreCase("IN_PROGRESS"));
    }

}