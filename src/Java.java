//
////class Node for creating a node
//
//import java.util.Scanner;
//
//class Node{
//    int data;
//    Node next;
//}
////class Linkedlist insert delete traverse/display
//class Linkedlist{
//    Node head;//null
//    
//    //insert data
//    void insertData(int data){ //data=10
//        Node node = new Node();
//        
//        //node = 24
//        node.data = data; 
//        node.next = null;
//        
//        System.out.println("address: "+node);
//        if(head == null){
//            head = node;
//        }else{
//            Node n = head; //12
//            while(n.next!=null){
//                n = n.next; //21
//            }
//            n.next = node;
//        }
//    }
//    void insertAtStart(int data){
//        Node node = new Node(); //1
//        
//        node.data = data;
//        node.next = head;
//        head =node;
//    }
//    
//    void display(){
//        Node n = head; //12
//        while(n!=null){
//            System.out.println(n.data);
//            n = n.next; //24
//        }
//    }
//    void insertAtIndex(int index, int data){
//        Node node = new Node();
//        
//        node.data =data;
//        node.next = null;
//        
//        if(index==0){
//            insertAtStart(data);
//        }else{
//            //index = 6
//            //node = 200
//            Node n = head; //1
//            for(int x = 0; x<index-1; x++){//x=5
//                n = n.next; //21
//            }
//            node.next = n.next;
//            n.next = node;
//
//        }
//    }
//    
//    void delete(int index){
//        if(index == 0){
//            head = head.next;
//        }else{
//            Node n = head; //1
//            Node n1 = null;
//            for(int x=0; x<index-1; x++){//x=3
//                n = n.next; //18
//            }
//            n1 = n.next; //100
//            n.next = n1.next;//21
//             
//        }
//    }
//}
//public class java {
//    public static void main(String[] args) {
//        Linkedlist ll = new Linkedlist();
//        Scanner sc = new Scanner(System.in);
//        int choice=0;
//        do{
//            System.out.println("[1] - insert data");
//            System.out.println("[2] - insert at start");
//            System.out.println("[3] - insert at index");
//            System.out.println("[4] - delete");
//            System.out.println("[5] - display");
//            System.out.print("choice: ");
//            choice = sc.nextInt();
//            if(choice==1){
//                System.out.print("Enter data: ");
//                int data = sc.nextInt();
//                ll.insertData(data);
//            }else if(choice==2){
//                System.out.print("Enter data: ");
//                int data = sc.nextInt();
//                ll.insertAtStart(data);
//            }else if(choice==3){
//                System.out.print("Enter index: ");
//                int index = sc.nextInt();
//                System.out.print("Enter data: ");
//                int data = sc.nextInt();
//                ll.insertAtIndex(index, data);
//            }else if(choice == 4){
//                System.out.print("Enter index you want to delete: ");
//                int index = sc.nextInt();
//                ll.delete(index);
//            }else if(choice==5){
//                ll.display();
//            }
//        }while(choice!=6);
//    }
//}
