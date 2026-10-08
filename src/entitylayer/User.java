package entitylayer;

import datalayer.Repository;
import java.time.LocalDate;

public abstract class User {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private LocalDate dateRegistered;
    private Repository repository;

    public User(String userId, String firstName, String lastName, String email, String password, Repository repository) {
        this.userId = userId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.dateRegistered = LocalDate.now();
        this.repository = repository;
    }

    // Setters
    public void setFirstName(String firstName) { this.firstName = firstName; }
    public void setLastname(String lastName) { this.lastName = lastName; }
    public void setEmail(String email) { this.email = email; }
    public void setPassword(String password) { this.password = password; }

    // Getters
    public String getUserId() { return userId; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
    public String getFullName() {return firstName + " " + lastName;}
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public LocalDate getdateRegsistered() { return dateRegistered; }
    public Repository getRepository() {return repository;}

    // Others
    public boolean checkPassword() {return false;} // for login na to later
    public abstract String getRole();

    
}
