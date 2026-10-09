public class Cat extends Animal {
    void makeSound(){
        super.makeSound();
        System.out.println("The Cat is meowing.");
    }
    void eat(){
        System.out.println("The Cat is eating.");
    }
}
