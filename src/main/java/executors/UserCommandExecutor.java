package executors;

import dao.UserDao;
import dao.UserDaoFactory;
import dao.UserDaoImpl;
import model.User;
import utils.Helper;


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
        /*utils.Helper.print("Input user's id for update:");
        int user_id = utils.Helper.getInteger();
        utils.Helper.print("First name:");
        String firstName = utils.Helper.getString();
        utils.Helper.print("Last name:");
        String lastName = utils.Helper.getString();
        utils.Helper.print("Age:");
        int age = utils.Helper.getInteger();*/

        return false;
    }

}
