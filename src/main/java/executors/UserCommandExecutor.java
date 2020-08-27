package executors;

import dao.UserDao;
import dao.UserDaoFactory;
import model.User;
import utils.Helper;

import java.io.IOException;

public class UserCommandExecutor {
    public void observe() throws IOException {
        while (true) {
            int respond = Helper.getInteger("Actions:\n(1) Add new user\n(2) Update user\n(3) Delete user\n(4) Return");
            switch (respond) {
                case 1:
                    insertUser();
                    break;
                case 2:
                    updateUser();
                    break;
                case 3:
                    deleteUser();
                    break;
                case 4:
                    return;
                default:
                    Helper.print("Incorrect command. Please, repeat");
            }
        }
    }

    private void deleteUser() {
    }

    public static boolean insertUser() throws IOException {
        String firstName = Helper.getString("First name:");
        String lastName = Helper.getString("Last name:");
        int age = Helper.getInteger("Age:");

        User user = new User(firstName, lastName, age);
        int userId = UserDaoFactory.getInstance().saveUser(user);
        if (userId == 0) {
            Helper.print("Exception was occured. Please, repeat");
            return false;
        }
        if(Helper.confirm("User added successfully. Add auto for user? y/n")) {
            AutoCommandExecutor executor = new AutoCommandExecutor();
            executor.observe(user.getAutos(), userId);
        }
        return true;
    }

    public static boolean updateUser() throws IOException {
        int user_id = Helper.getInteger("Input user's id for update:");
        UserDao dao = UserDaoFactory.getInstance();
        User currentUser = dao.getUser(user_id);
        boolean hasChanged = false;
        if (currentUser == null) {
            Helper.print("User not found. Operation canceled");
            return false;
        }
        if (Helper.confirm("Modify first name? y/n\nCurrent first name: " + currentUser.getFirstName())) {
            String firstName = Helper.getString("Enter new value for first name:");
            currentUser.setFirstName(firstName);
            hasChanged = true;
        }
        if (Helper.confirm("Modify last name y/n\nCurrent last name: " + currentUser.getLastName())) {
            String lastName = Helper.getString("Enter new value for last name: ");
            currentUser.setLastName(lastName);
            if (!hasChanged)
                hasChanged = true;
        }
        if (Helper.confirm("Modify age y/n\nCurrent age: " + currentUser.getAge())) {
            int age = Helper.getInteger("Enter new value for age: ");
            currentUser.setAge(age);
            if (!hasChanged)
                hasChanged = true;
        }
        if (hasChanged) {
            dao.updateUser(currentUser);
            if (Helper.confirm("User data updated. Update autos list?")) {
                AutoCommandExecutor executor = new AutoCommandExecutor();
                executor.observe(currentUser.getAutos(), currentUser.getId());
            }
        }
        return true;
    }

}
