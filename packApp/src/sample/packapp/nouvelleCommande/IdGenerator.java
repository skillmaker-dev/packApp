package sample.packapp.nouvelleCommande;

import java.util.Random;

public class IdGenerator {

    public static String generateId() {

        // create a string of numbers
        String numbers = "0123456789";

        // create random string builder
        StringBuilder sb = new StringBuilder();

        // create an object of Random class
        Random random = new Random();

        // specify length of random string
        int length = 6;

        for(int i = 0; i < length; i++) {

            // generate random index number
            int index = random.nextInt(numbers.length());

            // get character specified by index
            // from the string
            char randomChar = numbers.charAt(index);

            // append the character to string builder
            sb.append(randomChar);
        }

        String randomString = sb.toString();
        return randomString;

    }

}
