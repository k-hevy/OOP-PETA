public class Action {
  public static void main(String[] args){
      Animal an1=new Animal();
      Dog d1=new Dog();
      Cat cat1=new Cat();
      
      an1.makeSound();
      an1.eat();
      System.out.println();
      d1.makeSound();
      d1.eat();
        System.out.println();
      cat1.makeSound();
      cat1.eat();
        System.out.println();
    
     d1.setGender("Boy");
     System.out.println("Dogs gender is "+d1.getGender());
    
      cat1.setGender("Girl");
      System.out.println("Cats gender is "+cat1.getGender());
      
      an1.setGender("Gayly");
      System.out.println("The animals gender issssss "+ an1.getGender());
  }  
}
