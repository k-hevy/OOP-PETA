package entitylayer;

import java.time.LocalDateTime;

public class TicketLogEntry {

    private final String ticketLogEntryId;
    private final String previousState;
    private final String newState;
    private final String changedByUserId;
    private final String changedByUserName;
    private final LocalDateTime timestamp;
    private final String reason;
    private static int ticketLogEntryCounter = 0;

    public TicketLogEntry(String previousState, String newState, String changedByUserId, String changedByUserName, String reason) {
        this.ticketLogEntryId = generateTicketLogEntryId();
        this.previousState = previousState;
        this.newState = newState;
        this.changedByUserId = changedByUserId;
        this.changedByUserName = changedByUserName;
        this.timestamp = LocalDateTime.now();
        this.reason = reason;
    }

    public String getTicketLogEntryId() { return ticketLogEntryId; }
    public String getPreviousState() { return previousState; }
    public String getNewState() { return newState; }
    public String getChangedByUserId() { return changedByUserId; }
    public String getChangedByUserName() { return changedByUserName; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public String getReason() { return reason; }

    private String generateTicketLogEntryId() {
        ticketLogEntryCounter++;
        return String.format("LG-%03d", ticketLogEntryCounter);
    } // called whenever a new log is created

}
