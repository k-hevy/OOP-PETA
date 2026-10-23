package entitylayer;

import datalayer.Repository;

public class Student extends Requestor {
    
    // It represents a student who uses the system to submit tickets for campus-related problems. 
    // It extends the Requestor class, allowing students to inherit the requestor's feature.

    private String studentId;
    private String program;
    private int yearLevel;
    private String section;

    // Constructs a Student instance with student-specific attributes
    public Student(String userId, String firstName, String lastName, String email, String password, Repository repository, String studentId, String program, int yearLevel, String section) {
            super(userId, firstName, lastName, email, password, repository);
            this.studentId = studentId;
            this.program = program;
            this.yearLevel = yearLevel;
            this.section = section;
        }

    // getters
    public String getStudentId() { return studentId; }
    public String getProgram() { return program; }
    public int getYearLevel() { return yearLevel; }
    public String getSection() { return section;}
    
    // Setters
    public void setStudentId(String studentId) {this.studentId = studentId;}
    public void setProgram(String program) {this.program = program;}
    public void setYearLevel(int yearLevel) {this.yearLevel = yearLevel;}
    public void setSection(String section) {this.section = section;} 

}
