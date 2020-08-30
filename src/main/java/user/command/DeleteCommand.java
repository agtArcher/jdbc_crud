package user.command;

import dao.UserDao;
import dao.UserDaoFactory;
import model.User;
import utils.Helper;

import java.io.IOException;

class DeleteCommand implements Command {
    @Override
    public void execute() {
        try {
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
        } catch (IOException e) {
            Helper.print("An exception occurred while deleting user. Please, try again.");
        }
    }
}
