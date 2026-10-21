package entitylayer;

import datalayer.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class User {

    private final String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private final LocalDateTime dateRegistered;
    private final Repository repository;
    private final String role;

    public User(String role, String userId, String firstName, String lastName, String email, String password, Repository repository) {
        this.role = role;
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateRegistered = LocalDateTime.now();
        this.repository = repository;
    }

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastName(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    // Getters
    public String getRole() { return role; }
    public String getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() {return firstName + " " + lastName;}
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public LocalDateTime getDateRegistered() { return dateRegistered; }
    protected Repository getRepository() {return repository;}

    // Others
    public boolean checkPassword(String password) { return this.password != null && this.password.equals(password); } 
    public abstract Ticket getTicket(String ticketId);
    public abstract ArrayList<Ticket> getTickets();

    public ArrayList<Ticket> getActiveTickets() {
        ArrayList<Ticket> myTickets = new ArrayList<>();
        for (Ticket ticket : getTickets()) {
            if (ticket.isActive()) {
                myTickets.add(ticket);
            }
        }
        return myTickets;
     } // Returns all tickets submitted by the requestor that are OPEN / INPROGRESS except Deleted ones


}