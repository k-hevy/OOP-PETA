package datalayer;

import entitylayer.Ticket;
import entitylayer.User;
import java.util.ArrayList;

public class Repository {

    // It acts as the central storage area that other class can use to add, search, and retrieve users and tickets

    private final ArrayList<User> users;
    private final ArrayList<Ticket> tickets;
    
    //Constructs a new Repository instance and initializes internal user and ticket lists.
    public Repository() {
        users = new ArrayList<>();
        tickets = new ArrayList<>();
    }

    // Receives a new user and adds a new user to the repository
    public void addUser(User user) {
        users.add(user);
    }

    // Receives an id and searches for the user with that id and returns a user or null if none.
    public User findUserById(String targetId) {
        for (User user : users) {
            if (user.getUserId().equalsIgnoreCase(targetId)) {
                return user;
            }
        }
        return null; // no user with this id exists
    }

    // Returns a copy of all users in the system
    public ArrayList<User> getAllUsers() {
        return new ArrayList<>(users);
    } 

    // Receives a ticket and adds a new ticket to the repository
    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    // Receives an id and searches for the ticket with that id and returns a ticket or null if none.
    public Ticket findTicketById(String targetId) {
         for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equalsIgnoreCase(targetId)) {
                return ticket;
            }
        }
        return null; // no ticket with this id exists
    }

    // Returns a copy of all tickets in the system
    public ArrayList<Ticket> getAllTickets() {
        return new ArrayList<>(tickets);
    }

}
