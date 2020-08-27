import dao.UserDao;
import dao.UserDaoFactory;
import dao.UserDaoImpl;
import model.User;

import java.io.BufferedReader;
import java.io.IOException;

public class UserCommandExecutor {
    public static boolean insertUser() throws IOException {
        Helper.print("First name:");
        String firstName = Helper.getString();
        Helper.print("Last name:");
        String lastName = Helper.getString();
        Helper.print("Age:");
        int age = Helper.getInteger();

        User user = new User(firstName, lastName, age);
        UserDao dao = UserDaoFactory.getInstance();
        return dao.saveUser(user);
    }

    public static boolean updateUser() {
        /*Helper.print("Input user's id for update:");
        int user_id = Helper.getInteger();
        Helper.print("First name:");
        String firstName = Helper.getString();
        Helper.print("Last name:");
        String lastName = Helper.getString();
        Helper.print("Age:");
        int age = Helper.getInteger();*/

        return false;
    }

}
