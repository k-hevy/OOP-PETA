package entitylayer;

import java.time.LocalDateTime;

public class Comment {

    // Represents a comment added to a ticket, it stores the comment’s ID, author, message, date and time it was created. 
    // This allows the Requestor and Support Agent to communicate properly.

    private final String commentId;
    private final User author;
    private final String message;
    private final LocalDateTime timestamp;
    private static int commentCounter = 0;

    // Constructs a new Comment instance with author and message
    public Comment(User author, String message) {
        this.commentId = generateCommentId();
        this.author = author;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getCommentId() {return commentId; } 
    public User getAuthor() {return author;}
    public String getAuthorName() { return author.getFullName(); }
    public String getMessage() {return message;}
    public LocalDateTime getTimestamp() { return timestamp;}

    // called whenever a new comment is created
    private String generateCommentId() {
        commentCounter++;
        return String.format("CM-%03d", commentCounter);
    } 
}
