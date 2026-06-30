package api.utils;

import java.util.Random;

/**
 * Generates random, unique test data so tests can be re-run without
 * hitting "email already taken" type validation errors.
 */
public class DataGenerator {

    private static final Random RANDOM = new Random();

    public static String randomName() {
        String[] firstNames = {"Rahul", "Aisha", "Omkar", "Sneha", "Kabir", "Maya", "Arjun", "Priya"};
        String[] lastNames = {"Sharma", "Verma", "Patil", "Khan", "Singh", "Iyer", "Gupta", "Naik"};
        return firstNames[RANDOM.nextInt(firstNames.length)] + " " + lastNames[RANDOM.nextInt(lastNames.length)];
    }

    public static String randomEmail() {
        return "qa.test." + System.currentTimeMillis() + RANDOM.nextInt(1000) + "@example.com";
    }

    public static String randomGender() {
        return RANDOM.nextBoolean() ? "male" : "female";
    }

    public static String randomStatus() {
        return RANDOM.nextBoolean() ? "active" : "inactive";
    }
}
