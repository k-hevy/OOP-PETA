package dsa;
import java.util.*;
public class LinearSearch {
  public static void main (String[]args){
   Scanner sc=new Scanner(System.in);
     
    System.out.print("Enter array size: ");
    int size=sc.nextInt();
    
     int[] arr =new int[size];
     System.out.println("Enter array elements: ");
    for(int i=0; i<arr.length; i++){
      System.out.print("Element "+i+": ");
      arr[i]=sc.nextInt();
     }
    
    System.out.println();
     System.out.print("Enter the value to search: ");
      int target=sc.nextInt();
     
       search(arr,target);

  }
  public static void search(int arr[], int target){
      boolean valueF=false;  String indexesf ="";
        for(int i = 0; i < arr.length; i++){
            if(target == arr[i]){
                if(valueF){
               indexesf += ", ";
                }
                indexesf += i;
                valueF = true;
             }}
    if(valueF){
            System.out.println("Value " + target + " found at index: " + indexesf);
        } else {
            System.out.println("Value not found.");
        }
  }}