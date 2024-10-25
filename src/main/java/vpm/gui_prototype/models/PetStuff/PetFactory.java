package vpm.gui_prototype.models.PetStuff;

/**
 * Factory class for creating Pet instances.
 * Provides a static method to create pets based on type, name, and age.
 */
public class PetFactory {

    /**
     * Creates a new Pet of the specified type, name, and age.
     *
     * @param type the type of pet (e.g., "Dog", "Cat")
     * @param name the name of the pet
     * @param age  the age of the pet
     * @return a new Pet instance with the specified attributes
     * @throws IllegalArgumentException if the pet type is unsupported
     * @throws IllegalStateException    if required fields (name or type) are missing
     */
    public static Pet createPet(String type, String name, int age) {
        return buildPet(name, type, age);
    }

    /**
     * Helper method to build a Pet instance using PetBuilder.
     * Configures the PetBuilder with the provided attributes and invokes the build method.
     *
     * @param name the name of the pet
     * @param type the type of pet (e.g., "Dog", "Cat")
     * @param age  the age of the pet
     * @return a new Pet instance with the specified attributes
     */
    private static Pet buildPet(String name, String type, int age) {
        return new PetBuilder()
                .setName(name)
                .setType(type)
                .setAge(age)
                .build();
    }
}
