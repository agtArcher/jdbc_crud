import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Helper {
    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private static boolean isInteger(String possibleNumber) {
        try {
            Integer.parseInt(possibleNumber);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void print(Object message) {
        System.out.println(message);
    }

    public static String getString() throws IOException {
        return reader.readLine();
    }

    public static int getInteger() throws IOException {
        while (true) {
            String possibleInt = reader.readLine();
            if (isInteger(possibleInt)) {
                return Integer.parseInt(possibleInt);
            }
            Helper.print("Incorrect data. Please, input a number.");
        }

    }
}
