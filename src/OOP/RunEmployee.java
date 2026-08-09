package OOP;

public class RunEmployee {
    public static void main (String[] args){
        Employee per1= new Employee();
        Employee per2= new Employee(102030,"Maria");
        Employee per3= new Employee(301020,"Axel", 14000);
        
        per1.computeSalary(43, 377);
        per2.computeSalary(40, 396);
        per3.computeSalary(41, 389);
        
        per1.updateSalary(1000);
        per2.updateSalary(1100, false);
        per3.updateSalary(8, true);
        
        per1.displayInfo();
        per2.displayInfo();
        per3.displayInfo();
        
        System.out.println("Total Employees: "+ Employee.getEmployeeCount());
    }
}
