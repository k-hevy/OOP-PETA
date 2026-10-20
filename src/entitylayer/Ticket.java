package entitylayer;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Ticket {

    private final LocalDateTime dateSubmitted;
    private final String ticketId;
    private final Requestor submittedBy;
    private final ArrayList<Comment> comments;
    private final ArrayList<TicketLogEntry> ticketLogEntries;
    private static int ticketCounter = 0;
    private String state;
    private SupportAgent assignedAgent;
    private String priority;
    private String category;
    private String subject;
    private String description;

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
    public LocalDateTime getDateSubmitted() { return dateSubmitted; }
    public String getDateSubmittedFormatted() {return dateSubmitted.format(java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));}
    public String getState() {return state;}
    public String getSubject() { return subject; }
    public String getDescription() {return description;}
    public ArrayList<Comment> getComments() {return new ArrayList<>(comments);}
    public ArrayList<TicketLogEntry> getTicketLogEntries() {return new ArrayList<>(ticketLogEntries);}

    // setters
    public void setAssignedAgent(SupportAgent agent) {this.assignedAgent = agent;} // Used by auto assign later
    public void setCategory(String category) {this.category = category; }
    public void setSubject(String subject) { this.subject = subject; }
    public void setDescription(String description) {this.description = description;}

    // Ticket actions

    public void addComment(Comment comment) {
        requireNotFinished(); // A ticket should not be CLOSED / DELETED
        comments.add(comment);
    } // Used by requestor / SupportAgent

    public void changeState(String newState, User changedBy, String reason) {
        checkTransition(newState); // validates if valid state change from Current --> newState. Throws error if yes
        String oldState = state; // remember oldState
        state = newState.toUpperCase(); // assign new State
        log(oldState, state, changedBy, reason); // Creates a tickert log if succesful
    } // Used by SupportAgent 

    public void changePriority(String newPriority, User changedBy, String reason) {
        requireActive(); // A ticket should still be OPEN / IN-PROGRESS to be able to change its priority
        if (newPriority.equalsIgnoreCase(getPriority())) { 
            throw new IllegalArgumentException("Ticket " + ticketId + " is already " + priority + " priority"); 
        } // Fail if the current priority and newPriority are the same
        String oldPriority = this.priority;
        this.priority = newPriority;
        log(oldPriority, priority, changedBy, reason); // Create a log
    } // Used by SupportAgent, checks for rules in changing a priority
    
    public void closeTicket(User by, String reason) {
        changeState("CLOSED", by, reason); // Check this method
    }  // Used by Requestor

    public void reopenTicket(User by, String reason) {
        requireResolved(); // A ticket can only be reopened if its RESOLVED
        changeState("IN_PROGRESS", by, reason);
    } // Used by Requestor

    public void deletTicket(User by, String reason) {
        requireNotDeleted(); // You cannot delete a ticket that is already deleted
        String oldState = state; // Remember Old State
        log(oldState, state, by, reason);
    } // Used by Requestor (can only delete OPEN/ INPROGRESS Tickets) and Admin (Admin can delete a ticket from any state except DELETED)

    public void reassignTicket(User user, User changedBy, String reason) {
        requireActive(); // A ticket should still be OPEN / IN-PROGRESS
        
        if (user == null) {
            throw new IllegalArgumentException("This User doesnt exist");
        } // throw an error if thhe user doesnt exist

        if (!(user instanceof SupportAgent)) { 
            throw new IllegalArgumentException("User with " + user.getFullName() + " is not an agent.");
        } // Throw an error if the user isnt a supportAgent

        SupportAgent newAgent = (SupportAgent) user; // downcast User if they are asupportAgent
        
        if (assignedAgent != null && assignedAgent.equals(newAgent)) { 
            throw new IllegalArgumentException("Ticket " + ticketId + " is already assigned to " + newAgent.getFirstName() + "."); 
        } // Cannot assign to same agent

        if (!newAgent.getSpecialization().equalsIgnoreCase(category)) {
            throw new IllegalArgumentException("Ticket " + ticketId + " category ("+ getCategory() +") does not match " + newAgent.getFirstName() +" ("+ newAgent.getSpecialization() + ") specialization."); 
        } // cannot assign to an agent with a different category from the ticket

        String oldAgentName = (assignedAgent != null) ? getAssignedAgent().getFullName() : "Unassigned"; // if the there is a currently assigned agent get their name else put Unassigned

        setAssignedAgent(newAgent); // Set assigned Agent
        log(oldAgentName, newAgent.getFullName(), changedBy, reason); // Create a ticket log if successful
    } // Used by Admin. Reassign a active ticket to a different agent with the same specialization

    // State Helpers

    public boolean isActive() { return (state.equalsIgnoreCase("OPEN") || state.equalsIgnoreCase("IN_PROGRESS")); } // Returns if a ticket is OPEN OR IN_PROGRESS
    public boolean isResolved() { return (state.equalsIgnoreCase("RESOLVED")); } // returns if a ticket is RESOLVED
    public boolean isFinished() { return state.equals("CLOSED") || state.equals("DELETED");  } // Returns if a ticket is CLOSED / DELTED. Finished tickets should no longer be edited
    public boolean isDeleted() { return state.equals("DELETED"); } // Returns if a ticket is Deleted

    // Throwers (early throw for UI Experiecne)

    public void checkTransition(String newState) {
        boolean valid = switch(this.state) {
            case "OPEN"        -> newState.equalsIgnoreCase("IN_PROGRESS");
            case "IN_PROGRESS" -> newState.equalsIgnoreCase("OPEN") || newState.equalsIgnoreCase("RESOLVED");
            case "RESOLVED"    -> newState.equalsIgnoreCase("IN_PROGRESS") || newState.equalsIgnoreCase("CLOSED");
            default            -> false;
        }; // Compares the current state and its allowed state change EX. An open ticket is only allowed to change to IN_PROGRESS
        if (!valid) {
            throw new IllegalStateException("Ticket " + ticketId + " cannot move from " + state + " to " + newState + ".");
        }  // Throws an error if it is an invalid state transition
    } 

    public void requireNotFinished() {
        if (isFinished()) {
            throw new IllegalStateException("Ticket " + ticketId + " is " + state + " and can no longer be changed.");
        }
    } // throw and error if a ticket is already CLOSED / DELETED

    public void requireActive() {
        if (!isActive()) {
            throw new IllegalStateException("Ticket " + ticketId + " is " + state + ". Only OPEN or IN_PROGRESS tickets allow this.");
        }
    } // Throw an error if a Ticket isnt OPEN / IN-PROGRESS

    public void requireResolved() {
        if (!isResolved()) {
            throw new IllegalStateException("Ticket " + ticketId + " is " + state + ". Only RESOLVED tickets allow this.");
        }
    } // Throw an error if a ticket isnt Resolved

    public void requireNotDeleted() {
    if (isDeleted())
        throw new IllegalStateException("Ticket " + ticketId + " is already DELETED.");
    } // Throw an error if a Ticketr is Deleted

    private String generateTicketId() {
        ticketCounter++;
        return String.format("TK-%03d", ticketCounter);
    } // Creates an automatic TicketID when ticket is created 

    private void log(String from, String to, User by, String reason) {
        TicketLogEntry log = new TicketLogEntry(from, to, by.getUserId(), by.getFirstName(), reason.trim());
        ticketLogEntries.add(log);
    } // Creates a new ticketLog whenever something changes within a ticket EX: Priority, Agent, or a State change

}