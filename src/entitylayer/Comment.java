package entitylayer;

import java.time.LocalDateTime;

public class Comment {

    private String commentId;
    private String authorId;
    private String message;
    private LocalDateTime timestamp;
    private static int commentCounter = 0;

    public Comment(String authorId, String message) {
        this.commentId = generateCommentId();
        this.authorId = authorId;
        this.message = message;
        this.timestamp = LocalDateTime.now();
    }

    public String getCommentId() {return commentId; } 
    public String getAuthorId() {return authorId;}
    public String getMessage() {return message;}
    public LocalDateTime getTimestamp() {return timestamp;}

    private String generateCommentId() {
        commentCounter++;
        return String.format("CM-%03d", commentCounter);
    } // called whenever a new comment is created

    public String getFormattedComment() {
         return  " (" + authorId + ") : " +  message + "(" + timestamp + ")";
    } // will remove soon when UI, remove nalang when may sa UI sa

}
