package entitylayer;

import java.time.LocalDate;

public class Comment {

    private String commentId;
    private String authorId;
    private String authorName;
    private String message;
    private LocalDate timestamp;

    public Comment(String commentId, String authorId, String authorName, String message) {
        this.commentId = commentId;
        this.authorId = authorId;
        this.authorName = authorName;
        this.message = message;
        this.timestamp = LocalDate.now();
    }

    public String getCommentId() {return commentId; } 
    public String getAuthorId() {return authorId;}
    public String getAuthorName() {return authorName;}
    public String getMessage() {return message;}
    public LocalDate getTimestamp() {return timestamp;}
    public String getFormattedComment() {
         return authorName + " (" + authorId + ") : " +  message + "(" + timestamp + ")";
    }



}
