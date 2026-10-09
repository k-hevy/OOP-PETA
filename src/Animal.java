 class Animal {
    private String gender;
    
    void makeSound(){
    System.out.println("The animal is making a sound.");
    }
    void eat(){
        System.out.println("The animal is eating.");
    }
   String getGender(){
       return gender;
   }
   void setGender(String gender){
        this.gender=gender;
    }
}
