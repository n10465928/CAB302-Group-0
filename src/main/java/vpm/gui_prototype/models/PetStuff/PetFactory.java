package vpm.gui_prototype.models.PetStuff;

public class PetFactory {

    public static Pet createPet(String type, String name, int age){
        return buildPet(name, type, age);
    }
    private static Pet buildPet(String name, String type, int age){
        return new PetBuilder()
                .setName(name)
                .setType(type)
                .setAge(age)
                .build();
    }

}
