package dsa;
import  java.util.Scanner;

class Stacks{
     Scanner sc= new Scanner(System.in);
     int top=-1;
     int[] arrays=new int[5]; 
   
   void push(int data){
       if(isFull()){
           System.out.println("Stacks is Full.");
       }
       else{
       top++;
       arrays[top]=data;
       }}
   int pop(){
         int data=0;
       if(isEmpty()){
          System.out.println("Stacks is Empty.");
       }
       else{
        data=arrays[top];
        arrays[top]=0;
        top--; 
       }
       return data;
               
               
   }
   int peek(){
       return arrays[top];
   }
   boolean isFull(){
       return top==arrays.length-1;
   }
   boolean isEmpty(){
       return top==-1;
   }
   void displayTopBot(){
       for(int i=arrays.length-1; i>=0; i--){
       System.out.println(arrays[i]);
   }}
   void displayBotTop(){
       for(int i=0; i<=arrays.length-1; i++){
             
       System.out.println(arrays[i]);
   }      
}
   void searchLowest(){
      int low=arrays[0];
       for(int array:arrays){
         if(array<=low){
             low=array;
       }}
      System.out.println("Lowest is "+low); 
   }
}
public class StacksArray {
  public static void main(String[] args){
      Scanner inp=new Scanner(System.in);
      Stacks s=new Stacks();
      
      int choice=0;
      
    do{ 
      System.out.println("[1] push");
      System.out.println("[2] pop");
      System.out.println("[3] peek");
      System.out.println("[4] display top to bottom");
      System.out.println("[5] display bottom to top");
      System.out.println("[6] search lowest");
      System.out.print("Enter choice here: ");
      choice=inp.nextInt();
    switch(choice){
        case 1:{
            System.out.print("Enter number to push: ");
        s.push(inp.nextInt());
        break;
        }
        case 2:{
            System.out.print("Removed: "+ s.pop());
            
             break;
        }
        case 3:{
            System.out.print("Peek: "+ s.peek());
           
            break;
        }
        case 4: {
            s.displayTopBot();
            break;
        }
        case 5:{
            s.displayBotTop();
            break;
        }
        case 6:{
            s.searchLowest();
            break;
        }
    }    
  }while(choice!=7);
  }
  }