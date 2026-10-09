package dsa;
import java.util.*;
public class AtmBankingSystem {
    public static void main(String[] args){
        Scanner inp=new Scanner(System.in);
        Node node = new Node();
        AccountCreation account=new AccountCreation();
             int accountChoice=0;
         int choice=0; 
  do{      
        System.out.println("=========ATM Banking System==========");
        System.out.println("[1] Create Account");
        System.out.println("[2] Login");
        System.out.println("[3] Exit");
       System.out.print("Enter: ");
       choice=inp.nextInt();
        switch(choice){
            case 0:{
                account.display();
                break;
            }
            case 1:{
                inp.nextLine();
             System.out.print("Enter Name: ");
              node.name=inp.nextLine();
             
             System.out.print("Enter Birthday: ");
              node.birthday=inp.nextLine();
             
             System.out.print("Enter Address: ");
              node.address=inp.nextLine();
             
             System.out.print("Enter Contact Number: ");
              node.contactNumber=inp.next();
             System.out.println("Choose an Account Type: ");
               System.out.println("[1] Savings");
               System.out.println("[2] Checking");
               System.out.print("Enter: ");
              accountChoice = inp.nextInt();
 
         
      account.insertData(node.name,node.birthday,node.address,node.contactNumber, account.accountTypeValidation(accountChoice));
     
              break;
               }
             case 2:{
                break;
            }
             case 3:{
                break;
            }
        }
       
  }while(choice!=3);
    }
}
class AccountCreation{
     Node head;
     
    void insertData(String name, String birthday,String address,String contactNumber, String accountType){ 
        Node node = new Node();
        
        node.name = name; 
        node.birthday=birthday; 
        node.address=address; 
        node.contactNumber=contactNumber; 
        node.accountType=accountType;
        node.next = null;
        
        System.out.println("address: "+node);
        System.out.println("Account created successfully.");
        if(head == null){
            head = node;
        }else{
            Node n = head; 
            while(n.next!=null){
                n = n.next; 
            }
            n.next = node;
        }
}
     void display(){
    Node n = head;

    while(n != null){
        n.displayInfo();
        System.out.println("--------------------");
        n = n.next;
    }
}
     
     String accountTypeValidation( int accountChoice){
         Scanner sc=new Scanner(System.in);
         Node node=new Node();
    
         while (accountChoice != 1 && accountChoice != 2) {
                System.out.println("Invalid input. Please enter 1 or 2.");
                System.out.print("Enter: ");
                accountChoice = sc.nextInt();
            }

            if (accountChoice == 1) {
                node.accountType = "Savings";
            } else {
                node.accountType = "Checking";
            }

            return node.accountType;
     }
}

class Node{
    String name;
    String birthday;
    String address;
    String contactNumber;
    String accountType; //saving or checking
    Node next;
      
   void displayInfo(){
       System.out.println("Name: " +name);
       System.out.println("Birthday: " +birthday);
       System.out.println("Address: " +address);
       System.out.println("contactNumber: " +contactNumber);
        System.out.println("Account Type: "+accountType);
   }
}
