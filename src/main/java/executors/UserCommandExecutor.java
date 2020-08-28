package executors;

import dao.UserDao;
import dao.UserDaoFactory;
import model.Auto;
import model.User;
import utils.Helper;

import java.io.IOException;
import java.util.List;

public class UserCommandExecutor implements CommandExecutor {
    public void observe() {
        try {
            while (true) {
                int respond = Helper.getInteger("Actions:\n(1) Add new user\n(2) Update user\n(3) Delete user\n(4) Show user by id\n(5) Show all users\n(0) Return");
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
                        showUser();
                        break;
                    case 5:
                        showAllUsers();
                        break;
                    case 0:
                        return;
                    default:
                        Helper.print("Incorrect command. Please, repeat");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteUser() throws IOException {
        int user_id = Helper.getInteger("Enter user's id for deleting: ");
        UserDao dao = UserDaoFactory.getInstance();
        User currentUser = dao.getUser(user_id);
        if (currentUser == null) {
            Helper.print("User not found. Cancel operation.");
            return;
        }
        if (Helper.confirm("Are you sure to delete this user? y/n\nUser: " + currentUser)) {
            if(dao.deleteUser(user_id)) {
                Helper.print("User deleted.");
            } else {
                Helper.print("An exception occurred while deleting. Please, try again.");
            }
        }

    }

    private void insertUser() throws IOException {
        String firstName = Helper.getString("First name:");
        String lastName = Helper.getString("Last name:");
        int age = Helper.getInteger("Age:");

        User user = new User(firstName, lastName, age);
        int userId = UserDaoFactory.getInstance().saveUser(user);
        if (userId == 0) {
            Helper.print("An exception occurred while inserting. Please, repeat");
            return;
        }
        checkAuto(user.getAutos(), userId, "User added successfully. Observe user's autos list? y/n");
    }

    private void updateUser() throws IOException {
        int user_id = Helper.getInteger("Input user's id for update:");
        UserDao dao = UserDaoFactory.getInstance();
        User currentUser = dao.getUser(user_id);
        boolean hasChanged = false;
        if (currentUser == null) {
            Helper.print("User not found. Cancel operation");
            return;
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
            if (dao.updateUser(currentUser)) {
                checkAuto(currentUser.getAutos(), user_id, "User data updated. Update autos list? y/n");
            } else {
                Helper.print("An exception was occurred while updating. Please, try again.");
            }
        } else {
            checkAuto(currentUser.getAutos(), user_id, "Update autos list? y/n");
        }
    }

    private void showUser() throws IOException {
        int userId = Helper.getInteger("Please, enter user's id:");
        UserDao dao = UserDaoFactory.getInstance();
        User currentUser = dao.getUser(userId);
        if (currentUser == null) {
            Helper.print("User not found. Return...");
            return;
        }
        Helper.print("User: " + currentUser);
    }

    private void showAllUsers() {
        UserDao dao = UserDaoFactory.getInstance();
        List<User> users = dao.getAllUsers();
        users.forEach(Helper::print);
    }

    private void checkAuto(List<Auto> autos, int userId, String message) throws IOException {
        if(Helper.confirm(message)) {
            AutoCommandExecutor executor = new AutoCommandExecutor();
            executor.observe(autos, userId);
        }
    }

}
