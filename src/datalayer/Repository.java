package datalayer;

import entitylayer.Ticket;
import entitylayer.User;
import java.util.ArrayList;

public class Repository {

    private ArrayList<User> users;
    private ArrayList<Ticket> tickets;

    public Repository() {
        users = new ArrayList<>();
        tickets = new ArrayList<>();
    }

    public void addUser(User user) {
        users.add(user);
    }

    public User findUserById(String targetId) {
        for (User user : users) {
            if (user.getUserId().equalsIgnoreCase(targetId)) {
                return user;
            }
        }
        return null; // no user with this id exists
    }

    public ArrayList<User> getAllUsers() {
        return users;
    } 

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public Ticket findTicketById(String targetId) {
         for (Ticket ticket : tickets) {
            if (ticket.getTicketId().equalsIgnoreCase(targetId)) {
                return ticket;
            }
        }
        return null; // no ticket with this id exists
    }

    public ArrayList<Ticket> getAllTickets() {
        return tickets;
    }


    
}
