package user.command;

import dao.UserDao;
import dao.DaoFactory;
import auto.AutoExplorer;
import exception.ObjectNotFoundException;
import model.User;
import utils.Helper;

import java.io.IOException;
import java.sql.SQLException;

class UpdateCommand implements Command {
    @Override
    public void execute() {
        try {
            int userId = Helper.getInteger("Input user's id for update:");
            UserDao dao = DaoFactory.getUserDao();
            User currentUser = dao.getUser(userId);
            boolean hasChanged = false;
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
                    if(Helper.confirm("User data updated. Update autos list? y/n")) {
                        new AutoExplorer(currentUser.getAutos(), userId).explore();
                    }
                } else {
                    Helper.print("An exception was occurred while updating. Please, try again.");
                }
            } else if(Helper.confirm("Update autos list? y/n")) {
                new AutoExplorer(currentUser.getAutos(), userId).explore();
            }
        } catch (ObjectNotFoundException e) {
            Helper.print("User for update not found. Cancel operation");

        }
        catch (IOException e) {
            Helper.print("An exception occurred while updating user. Please, try again");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }
}
