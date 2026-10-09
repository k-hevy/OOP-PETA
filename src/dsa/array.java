package dsa;
import java.util.Scanner;
public class array {
    public static void main(String[] args){
    Scanner sc= new Scanner(System.in);
    int n=sc.nextInt();
    int[] arrays = new int[n];
    
   for( int i=n; i>2; i--){
       arrays[i]=arrays[i-1];
} 
      arrays[2]=35;
}}
