package entitylayer;

import java.time.LocalDate;

public class TicketLogEntry {

    private static int  ticketLogCount = 0;
    private String ticketLogEntryId;
    private String previousState;
    private String newState;
    private String changedByUserId;
    private String changedByUserName;
    private LocalDate timestamp;
    private String reason;

    public TicketLogEntry(String previousState, String newState, String changedByUserId, String changedByUserName, String reason) {
        ticketLogCount++;
        this.ticketLogEntryId = String.valueOf(ticketLogCount);
        this.previousState = previousState;
        this.newState = newState;
        this.changedByUserId = changedByUserId;
        this.changedByUserName = changedByUserName;
        this.timestamp = LocalDate.now();
        this.reason = reason;
    }

    public String getTicketLogId() { return ticketLogEntryId; }
    public String getPreviousState() { return previousState; }
    public String getNewState() { return newState; }
    public String getChangedByUserId() { return changedByUserId; }
    public String getChangedByUserName() { return changedByUserName; }
    public LocalDate getTimestamp() { return timestamp; }
    public String getReason() { return reason; }

    public String getFormattedLogEntry() {
        return "TicketLogEntry {" +
                "ticketLogId='" + ticketLogEntryId + '\'' +
                ", previousState='" + previousState + '\'' +
                ", newState='" + newState + '\'' +
                ", changedByUserId='" + changedByUserId + '\'' +
                ", changedByUserName='" + changedByUserName + '\'' +
                ", timestamp=" + timestamp +
                ", reason='" + reason + '\'' +
                '}';
    }
}
