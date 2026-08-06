import java.util.*;
public class JavaBasicPractice {
    public static void main(String[] args){
        Scanner inp = new Scanner(System.in);
      System.out.print("Entere numbers: ");
      int number =  inp.nextInt();
     
     int[] numbers = new int[number];
       int num;    
     double sum=0;
      for(int i=0; i<numbers.length; i++){
      System.out.printf("Enter number %d: ",i);
        num = inp.nextInt();
        numbers[i]= num;
         sum +=num;
         ave =sum/2;
      }
   //   System.out.println("\nSum of the number is: "+sum);
    //  System.out.println("Average of the numbers is: "+ave);
    //    enhance(numbers);
        
    for(int i=numbers.length-1; i>=0; i--){
        System.out.print(numbers[i]);
        
    }
    
}
    }
    
    
   /* static void enhance(int[] numbers){
        int highest=numbers[0], lowest=numbers[0];
         for (int numbir : numbers)
        
           if(lowest>numbir){
              lowest=numbir; 
           }
           else if(highest<numbir){
              highest=numbir;
           }
  
       System.out.println("Highest number is: "+highest);
       System.out.println("Lowest number is: "+ lowest);
    }
    
}
