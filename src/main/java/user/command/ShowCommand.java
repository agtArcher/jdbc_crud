package user.command;

import dao.UserDao;
import dao.DaoFactory;
import exception.ObjectNotFoundException;
import model.User;
import utils.Helper;

import java.io.IOException;
import java.sql.SQLException;

class ShowCommand implements Command {
    @Override
    public void execute() {
        try {
            int userId = Helper.getInteger("Please, enter user's id:");
            UserDao dao = DaoFactory.getUserDao();
            User currentUser = dao.getUser(userId);
            Helper.print("User: " + currentUser);
        } catch (ObjectNotFoundException e) {
            Helper.print("User not found. Return...");
        }
        catch (IOException e) {
            Helper.print("An exception occurred while showing user. Please, try again.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
