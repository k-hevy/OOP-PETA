package entitylayer;

import datalayer.Repository;
import java.time.LocalDateTime;
import java.util.ArrayList;

public abstract class User {

    // Abstract parent class of all system users. It contains the common information and methods shared 
    // by different types of users, such as Requestor/SupportAgent.

    private final String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private final LocalDateTime dateRegistered;
    private final Repository repository;
    private final String role;

    // Constructs a User instance
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
    public LocalDateTime getDateRegistered() { return dateRegistered; }
    protected Repository getRepository() {return repository;}

    // Others

    // Receives a password if the provided password matches the user's password returns true
    public boolean checkPassword(String password) { return this.password != null && this.password.equals(password); } 

    // Abstract method to get a ticket by ID
    public abstract Ticket getTicket(String ticketId);

    // Abstract method to get all tickets associated with the user
    public abstract ArrayList<Ticket> getTickets();

    // Returns all tickets submitted by the requestor that are OPEN / INPROGRESS except Deleted ones
    public ArrayList<Ticket> getActiveTickets() {
        ArrayList<Ticket> myTickets = new ArrayList<>();
        for (Ticket ticket : getTickets()) {
            if (ticket.isActive()) {
                myTickets.add(ticket);
            }
        }
        return myTickets;
    } 

}