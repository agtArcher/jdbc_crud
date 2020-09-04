package user.command;

import dao.UserDao;
import dao.DaoFactory;
import exception.ObjectNotFoundException;
import model.User;
import utils.Helper;

import java.io.IOException;
import java.sql.SQLException;

class DeleteCommand implements Command {
    @Override
    public void execute() {
        try {
            int user_id = Helper.getInteger("Enter user's id for deleting: ");
            UserDao dao = DaoFactory.getUserDao();
            User currentUser = dao.getUser(user_id);
            if (Helper.confirm("Are you sure to delete this user? y/n\nUser: " + currentUser)) {
                if(dao.deleteUser(user_id)) {
                    Helper.print("User deleted.");
                } else {
                    Helper.print("An exception occurred while deleting. Please, try again.");
                }
            }
        } catch (ObjectNotFoundException e) {
            Helper.print("User for delete not found. Cancel operation.");
        }

        catch (IOException e) {
            Helper.print("An exception occurred while deleting user. Please, try again.");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
