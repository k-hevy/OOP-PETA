package entitylayer;

import datalayer.Repository;

public class Faculty extends Requestor {

    private String employeeId;
    private String department;
    private String position;
    private String deskExtension;

    public Faculty(String userId, String firstName, String lastName, String email, String password, Repository repository,
                    String employeeId, String department, String position, String deskExtension) {
        super(userId, firstName, lastName, email, password, repository);
        this.employeeId = employeeId;
        this.department = department;
        this.position = position;
        this.deskExtension = deskExtension;
    }

    //Getters
    public String getEmployeeId() {return employeeId;}
    public String getDepartment() {return department;}
    public String getPosition() {return position;}
    public String getDeskExtension() {return deskExtension;}

     // Setetrs
    public void setEmployeeId(String employeeId) { this.employeeId = employeeId;}
    public void setDepartment(String department) {this.department = department;}
    public void setPosition(String position) {this.position = position;}
    public void setDeskExtension(String deskExtension)  {this.deskExtension = deskExtension;}

}
