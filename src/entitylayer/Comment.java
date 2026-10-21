package entitylayer;

import java.time.LocalDateTime;

public class Comment {

    private final String commentId;
    private final User author;
    private final String message;
    private final LocalDateTime timestamp;
    private static int commentCounter = 0;

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

    private String generateCommentId() {
        commentCounter++;
        return String.format("CM-%03d", commentCounter);
    } // called whenever a new comment is created

}
