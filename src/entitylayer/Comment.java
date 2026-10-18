package entitylayer;

import java.time.LocalDateTime;

public class Comment {

    private final String commentId;
    private final User user;
    private final String message;
    private final LocalDateTime timestamp;
    private static int commentCounter = 0;

    public Comment(User user, String message) {
        this.commentId = generateCommentId();
        this.user = user;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getCommentId() {return commentId; } 
    public User getAuthor() {return user;}
    public String getAuthorName() { return user.getFullName(); }
    public String getMessage() {return message;}
    public LocalDateTime getTimestamp() { return timestamp;}
    public String getTime() { return getTimestamp().format(java.time.format.DateTimeFormatter.ofPattern("MM-dd HH:mm")); }

    private String generateCommentId() {
        commentCounter++;
        return String.format("CM-%03d", commentCounter);
    } // called whenever a new comment is created

}
