package entitylayer;

import java.time.LocalDateTime;

public class TicketLogEntry {

    // It records changes made to a help desk ticket. It stores the previous state, new user, 
    // user who made the change, timestamp and reason. This helps track the activities performed on a ticket.
    
    private final String ticketLogEntryId;
    private final String oldValue;
    private final String newValue;
    private final String changedByUserId;
    private final LocalDateTime timestamp;
    private final String reason;
    private static int ticketLogEntryCounter = 0;

    // Constructs a new TicketLogEntry instance
    public TicketLogEntry(String oldValue, String newValue, String changedByUserId, String reason) {
        this.ticketLogEntryId = generateTicketLogEntryId();
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.changedByUserId = changedByUserId;
        this.timestamp = LocalDateTime.now();
        this.reason = reason;
    }

    public String getTicketLogEntryId() { return ticketLogEntryId; }
    public String getOldValue() { return oldValue; }
    public String getNewValue() { return newValue; }
    public String getChangedByUserId() { return changedByUserId; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getReason() { return reason; }

    // called whenever a new log is created
    private String generateTicketLogEntryId() {
        ticketLogEntryCounter++;
        return String.format("LG-%03d", ticketLogEntryCounter);
    } // called whenever a new log is created

}
