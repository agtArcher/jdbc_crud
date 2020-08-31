package utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Helper {
    private static final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

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

    public static String getString(String message) throws IOException {
        print(message);
        return getString();
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

    public static int getInteger(String message) throws IOException {
        print(message);
        return getInteger();
    }

    //request from user answer. if respond start with 'y' return true, if 'n' - false. else wait
    public static boolean confirm(String message) {
        print(message);
        try {
            while (true) {
                String confirm = getString().toLowerCase();
                if (confirm.startsWith("y")) {
                    return true;
                } else if (confirm.startsWith("n")) {
                    return false;
                }
            }
        } catch (IOException e) {
            print("Some exception occurred. Try again");
            return confirm(message);
        }
    }
}
