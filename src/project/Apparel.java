
package Project;
import java.util.Scanner;
import java.text.DecimalFormat;

public class Apparel {
static Scanner input=new Scanner(System.in);
static DecimalFormat twodec= new DecimalFormat("0.00");

 public static void main(String[]args){
    double rneck=250,vneck=300,polo=400,baseprice;
    char type;  boolean back=false; 
      
do {
  System.out.println("===================MAIN================="); 
  System.out.println("T-shirt Type ");
  System.out.println("[R] Round neck");
  System.out.println("[V] V-neck");
  System.out.println("[P] Polo");
  System.out.println("[E] Exit and stop");

  System.out.print("T-shirt Type:  ");
  type=input.next().charAt(0);
  type=Character.toUpperCase(type);   
System.out.println("----------------------------------------");
   
 switch (type){ 
    case 'R': {
        baseprice=rneck;
        sizeChoices(baseprice,type);
    break;
              }
    case 'V': {
        baseprice=vneck;
         sizeChoices(baseprice,type);
    break;
               }
    case 'P' : {
        baseprice=polo;
        sizeChoices(baseprice,type);
    break;
               }
    case 'E' : {            
        System.out.println("You chose to leave." );
        back = true;
    break;
               }       
    default:{ 
        System.out.println("Choice invalid!" );
    }
 } } while(!back);//WHILE 
 }//MAIN ENDS HERE
 
    public static void discountComputation(int quantity,double addson, double baseprice,double discountrate)
    {
        double unitprice,total,discountamount,amountpay;
   
           unitprice=baseprice+addson;
           System.out.println("Unit Price:  "+ twodec.format(unitprice));
           System.out.println("----------------------------------------");
           total=quantity*unitprice;
           System.out.println("Total Amount:  "+ twodec.format(total));    
           discountamount=total*discountrate;
           System.out.println("Amount of Discount:  "+ twodec.format(discountamount));
           amountpay=total-discountamount;
           System.out.println("Amount to Pay:  "+ twodec.format(amountpay));   
           System.out.println(); 
    
   }
  
     public static void noDicount(int quantity, double addson, double baseprice)
   {
           double unitprice, total;
            unitprice=baseprice+addson;
            System.out.println("Unit Price:  "+ twodec.format(unitprice));
            System.out.println("----------------------------------------");
            total=quantity*unitprice;
            System.out.println("Total Amount:  "+ twodec.format(total));    
            System.out.println(); 
   }
   //--------------------------------------------------------------------------------------------------------SIZE MODULE  
     
   public static void sizeChoices(double baseprice, char type)
   {
   char size; double small=0,medium=20,large=40,extralarge=60,addson;
   boolean back=false;
   
  do{
  System.out.println("T-shirt Size ");
  System.out.println("[S] Small");
  System.out.println("[M] Medium");
  System.out.println("[L] Large");
  System.out.println("[X] Extra large");
   System.out.println("[B] Back ");
  
  System.out.print("Enter T-shirt Size:  ");
  size=input.next().charAt(0);
  size=Character.toUpperCase(size); 
  System.out.println("----------------------------------------");
  
   switch (size){ 
    case 'S': {
        addson=small;
        overallQuantity(type,baseprice,addson);
    break;
              }          
    case 'M': {
        addson=medium;
        overallQuantity(type,baseprice,addson); 
    break;
               }
    case 'L' : {
        addson=large;
        overallQuantity(type,baseprice,addson);
    break;
               }
    case 'X' : {
        addson=extralarge;
        overallQuantity(type,baseprice,addson);
    break;
          }

    case 'B' :             
        back = true;
    break;
                
    default:{
        System.out.println("Size does not exist, please try again.");
        System.out.println("");
    break;}
      }
    }  while(!back);
   }
   //--------------------------------------------------------------------------------------------------------NQUANTITY MODULE
   
   public static void rneckQuantity(double baseprice,double addson)
   {  
     int quantity; double discountrate=0.05;
 
    System.out.print("Quantity:  ");
    quantity=input.nextInt();  
   
      if (quantity>7){
             
        discountComputation(quantity,addson,baseprice,discountrate);  
                     }  
      else if(quantity<0)
     {
        System.out.println("----------------------------------------");      
        System.out.println("Sorry, negative number/s not accepted");
        System.out.println(); 
     }
      else{
        noDicount(quantity,addson,baseprice);
     }
   }
   
   //------------------------------------------------------------------------------------------------------VQUANTITY MODULE
  
 public static void vneckQuantity(double baseprice,double addson)
 {  
    int quantity; double discountrate=0.08;
    
   System.out.print("Quantity:  ");
   quantity=input.nextInt();  
   
    if (quantity>5){       
      discountComputation(quantity,addson,baseprice,discountrate);  
                   }  
    else if(quantity<0){
      System.out.println("----------------------------------------");      
      System.out.println("Sorry, negative number/s not accepted");
      System.out.println(); 
                       }
     else{
        noDicount(quantity,addson,baseprice);
         }
 }
    //------------------------------------------------------------------------------------------------PQUANTITY MODULE
    
      public static void poloQuantity(double baseprice,double addson)
{  
    int quantity;  double discountrate = 0.12;
  
   System.out.print("Quantity:  ");
   quantity=input.nextInt();  
   
    if (quantity>2){      
      discountComputation(quantity,addson,baseprice,discountrate);  
                     }  
    else if(quantity<0){
      System.out.println("----------------------------------------");      
      System.out.println("Sorry, negative number/s not accepted");
      System.out.println(); 
         }
     else{
        noDicount(quantity,addson,baseprice);
         }
}
      public static void overallQuantity(char type, double baseprice, double addson){
    
            switch (type)  {
          case 'R':
              rneckQuantity(baseprice,addson);
              break;
          case 'V':
              vneckQuantity(baseprice,addson);
              break;
          case 'P' :
              poloQuantity(baseprice,addson);
              break;
                          }
   }
}