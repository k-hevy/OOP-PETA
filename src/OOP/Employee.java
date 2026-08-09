package OOP;

class Employee {
   private int id;
   private String name;
   private double salary;
   private double hoursWorked;
   private double ratePerHour;
   private static int employeeCount;
  
   //getters for acces and setters to assign
   public int getID(){  
    return id;
  }
   public void setID(int id){
      this.id=id;
  }
   public String getName(){
    return name;
  } 
    public void setName(String name){
      this.name=name;
  }
    public double getSalary(){
    return salary;
  } 
    public void setSalary(double salary){
      this.salary=salary;
  }
      public double getHoursWorked(){
    return hoursWorked;
  } 
     public void setHoursWorked(double hoursWorked){
      this.hoursWorked=hoursWorked;
  }
  public double getRatePerHour(){
    return ratePerHour;
  } 
    public void setRatePerHour(double ratePerHour){
      this.ratePerHour=ratePerHour;
  }
    static int getEmployeeCount(){
    return employeeCount;
    }
   
 public Employee(){
   this.id=137782;
   this.name="Denise";
   this.salary=salary;
   this.hoursWorked=hoursWorked;
   this.ratePerHour=ratePerHour;
   employeeCount++;
}
public Employee(int id, String name){
    this(); //calls the first Employee constructor
    this.id=id;
    this.name=name;
}
public Employee(int id, String name, double salary){
    this(id,name); //calls the sec Employee constructor
    this.salary=salary;
}
public void computeSalary(double hoursWorked, double ratePerHour){
    this.hoursWorked=hoursWorked;
    this.ratePerHour=ratePerHour;
 double grossPay = hoursWorked*ratePerHour;
 double tax = grossPay*0.12;
  double netSalary = grossPay-tax; //salary after tax
  this.salary = netSalary;
}

 public void updateSalary(double amount){ //overloading
  this.salary +=amount; //fixed
 }
public void updateSalary(double amount, boolean isPercentage){ //overloading
    if(isPercentage){
        this.salary+= this.salary*(amount/100); //increase
    }
    else{
       this.salary +=amount;  //fixed
    }
}

   public void displayInfo() {
    System.out.println("Employee's ID: " + id);
    System.out.println("Name: " + name);
    System.out.println("Salary: " + (String.format("%.2f", salary)));
    System.out.println("Hours Worked: " + hoursWorked);
    System.out.println("Rate Per Hour: " + ratePerHour);
    System.out.println("---------------------------");
}
}

