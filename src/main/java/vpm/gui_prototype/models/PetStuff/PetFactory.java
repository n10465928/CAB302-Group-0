package vpm.gui_prototype.models.PetStuff;

public class PetFactory {

    public static Pet createPet(String type, String name, int age){
        return switch (type) {
            case "dog" -> createDog(name, age);
            case "cat" -> createCat(name, age);
            case "bird" -> createBird(name, age);
            case "fish" -> createFish(name, age);
            default -> null;
        };
    }

    private static Pet createDog(String name, int age){
        return new Dog(name, age);
    }
    private static Pet createCat(String name, int age){
        return new Cat(name, age);}
    private static Pet createFish(String name, int age){
        return new Cat(name, age);
    }
    private static Pet createBird(String name, int age){
        return new Dog(name, age);}

}
