package entitylayer;

import java.time.LocalDateTime;
import java.util.ArrayList;

public class Ticket {

    // Represents a ticket submitted by a requestor. It stores the ticket details and ticket logs. 
    // It manages its own state actions and ensures that changes follow the system’s rules adn throws error if something is wrong.

    private final String ticketId;
    private final LocalDateTime dateSubmitted;
    private final Requestor submittedBy;
    private final String category;
    private final String subject;
    private final String description;
    private final ArrayList<Comment> comments;
    private final ArrayList<TicketLogEntry> ticketLogEntries;
    private static int ticketCounter = 0;
    private String state;
    private SupportAgent assignedAgent;
    private String priority;

    // Constructs a Ticket instance and initializes logs
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
    private void setAssignedAgent(SupportAgent agent) {this.assignedAgent = agent;} // Only used by Requestor's auto-assignOnly used by Requestor's auto-assign

    // Ticket actions

    // Receives a comment and add it to the ticket's list. Used by requestor / SupportAgent
    public void addComment(Comment comment) {
        requireNotFinished(); // A ticket should not be CLOSED / DELETED
        comments.add(comment);
    } 

    // Receives a list of users, picks the best user with the fewest tickets matching the ticket's category.
    public void autoAssignAgent(ArrayList<User> users) {

        SupportAgent bestAgent = null;
        int fewestActiveTickets = Integer.MAX_VALUE;

        for (User user : users) {
            if (!(user instanceof SupportAgent agent)) { continue; } // User must be a supportAgent
            if (!agent.getSpecialization().equalsIgnoreCase(category)) { continue; } // SupportAgent specializaiton must be thhe same as ticket category
            
            int activeCount = agent.getActiveTickets().size();
            if (activeCount < fewestActiveTickets) {
                fewestActiveTickets = activeCount;
                bestAgent = agent;
            } // Pick the agent with fewest active tickets that specialization matchhes the category of the ticket 
        }

        if (bestAgent != null) { 
            setAssignedAgent(bestAgent);
        } // if there is a best agent assign the ticket

        // do nothing if there isnt an agent available

    } // Picks the best agent for the job

    // Receives a newstate, the agent, and reason. Checks for valid agent transitions first before moving adn logging. Used by SupportAgent
    public void updateTicketState(String newState, User changedBy, String reason) {
        checkWorkFlowMoves(newState);
        move(newState, changedBy, reason);
    } // Used by SupportAgent 

    // Receives a new priority and the agent. checks for rules in changing a priority before changing and logging. Used by SupportAgent, 
    public void changePriority(String newPriority, User changedBy, String reason) {
        requireActive(); // A ticket should still be OPEN / IN-PROGRESS to be able to change its priority
        if (newPriority.equalsIgnoreCase(priority)) { 
            throw new IllegalArgumentException("Ticket " + ticketId + " is already " + priority + " priority"); 
        } // Fail if the current priority and newPriority are the same
        String oldPriority = this.priority;
        this.priority = newPriority;
        log(oldPriority, priority, changedBy, reason); // Create a log
    } 
    
    // Receives who made the change, requries a ticket to be RESOLVED first before moving. Used by Requestor
    public void closeTicket(User by, String reason) {
        requireResolved();
        move("CLOSED", by, reason);
    }  

    // Receives who made the change, requries a ticket to be RESOLVED first before moving. Used by Requestor
    public void reopenTicket(User by, String reason) {
        requireResolved(); // A ticket can only be reopened if its RESOLVED
        move("OPEN", by, reason);
    } 

    // Receives the use who made the change and reason. Used by Requestor (can only delete OPEN/ INPROGRESS Tickets) and Admin (Admin can delete a ticket from any state except DELETED)
    public void deleteTicket(User by, String reason) {
        requireNotFinished();
        move("DELETED", by, reason);
    } 

    // Receives a agentm and the user who mad ethe change and reason. Reassign a active ticket to a different agent with the same specialization. Used by Admin. 
    public void reassignTicket(User user, User changedBy, String reason) {
        checkReassign(user);
        SupportAgent newAgent = (SupportAgent) user;
        String oldAgentName = (assignedAgent != null) ? getAssignedAgent().getFullName() : "Unassigned"; // if the there is a currently assigned agent get their name else put Unassigned
        setAssignedAgent(newAgent); // Set assigned Agent
        log(oldAgentName, newAgent.getFullName(), changedBy, reason); // Create a ticket log if successful
    } 

    // State Helpers - Asks a quetsion of thhe current state of a ticket

    public boolean isActive() { return (state.equalsIgnoreCase("OPEN") || state.equalsIgnoreCase("IN_PROGRESS")); } // Returns if a ticket is OPEN OR IN_PROGRESS
    public boolean isResolved() { return (state.equalsIgnoreCase("RESOLVED")); } // returns if a ticket is RESOLVED
    public boolean isFinished() { return state.equalsIgnoreCase("CLOSED") || state.equals("DELETED");  } // Returns if a ticket is CLOSED / DELTED. Finished tickets should no longer be edited
    public boolean isDeleted() { return state.equalsIgnoreCase("DELETED"); } // Returns if a ticket is Deleted

    // Throwers (early throw for UI Experiecne)

    // throw and error if a ticket is already CLOSED / DELETED
    public void requireNotFinished() {
        if (isFinished()) {
            throw new IllegalStateException("Ticket " + ticketId + " is " + state + " and can no longer be changed.");
        }
    } 

    // Throw an error if a Ticket isnt OPEN / IN-PROGRESS
    public void requireActive() {
        if (!isActive()) {
            throw new IllegalStateException("Ticket " + ticketId + " is " + state + ". Only OPEN or IN_PROGRESS tickets allow this.");
        }
    } 

    // Throw an error if a ticket isnt Resolved
    public void requireResolved() {
        if (!isResolved()) {
            throw new IllegalStateException("Ticket " + ticketId + " is " + state + ". Only RESOLVED tickets allow this.");
        }
    } // Throw an error if a ticket isnt Resolved

    // Throw an error if a Ticketr is Deleted
    public void requireNotDeleted() {
    if (isDeleted())
        throw new IllegalStateException("Ticket " + ticketId + " is already DELETED.");
    } 

    // Receives a new state, checks for valid supportagent state transitions. Used by SupportAgent
    public void checkWorkFlowMoves(String newState) {
        boolean valid = switch(state) {
            case "OPEN"        -> newState.equalsIgnoreCase("IN_PROGRESS");
            case "IN_PROGRESS" -> newState.equalsIgnoreCase("OPEN") || newState.equalsIgnoreCase("RESOLVED");
            case "RESOLVED"    -> newState.equalsIgnoreCase("IN_PROGRESS");
            default            -> false;
        }; // Compares the current state and its allowed state change EX. An open ticket is only allowed to change to IN_PROGRESS
        if (!valid) {
            throw new IllegalStateException("Ticket " + ticketId + " cannot move from " + state + " to " + newState + ".");
        } // Throws an error if it is an invalid state transition
    } 

    // Checks if the target user can be assigned to this ticket
    public void checkReassign(User user) {
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
    }
    
    // private helpers 
    
    // Creates an automatic TicketID when ticket is created
    private String generateTicketId() {
        ticketCounter++;
        return String.format("TK-%03d", ticketCounter);
    } // Creates an automatic TicketID when ticket is created 

    // Receives a newState, user and reason. The method that actually moves a ticket from a state
    private void move(String newState, User by, String reason) {
        String oldState = state; // remember oldState
        state = newState.toUpperCase();  // assign new State
        log(oldState, state, by, reason); // Creates a tickert log if succesful
    } // the method that actually moves a ticket from a state

    // Requires an old value and new value the user who made the change and reason. Creates a new ticketLog whenever something changes within a ticket EX: Priority, Agent, or a State change.
    private void log(String from, String to, User by, String reason) {
        TicketLogEntry log = new TicketLogEntry(from, to, by.getUserId(), by.getFirstName(), reason.trim());
        ticketLogEntries.add(log);
    } // Creates a new ticketLog whenever something changes within a ticket EX: Priority, Agent, or a State change

}