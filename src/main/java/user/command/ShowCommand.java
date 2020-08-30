package user.command;

import dao.UserDao;
import dao.UserDaoFactory;
import model.User;
import utils.Helper;

import java.io.IOException;

class ShowCommand implements Command {
    @Override
    public void execute() {
        try {
            int userId = Helper.getInteger("Please, enter user's id:");
            UserDao dao = UserDaoFactory.getInstance();
            User currentUser = dao.getUser(userId);
            if (currentUser == null) {
                Helper.print("User not found. Return...");
                return;
            }
            Helper.print("User: " + currentUser);
        } catch (IOException e) {
            Helper.print("An exception occurred while showing user. Please, try again.");
        }
    }
}
