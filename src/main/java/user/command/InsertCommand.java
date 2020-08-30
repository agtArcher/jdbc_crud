package user.command;

import dao.UserDaoFactory;
import auto.AutoExplorer;
import model.User;
import utils.Helper;

import java.io.IOException;

class InsertCommand implements Command {
    @Override
    public void execute() {
        try {
            String firstName = Helper.getString("First name:");
            String lastName = Helper.getString("Last name:");
            int age = Helper.getInteger("Age:");

            User user = new User(firstName, lastName, age);
            int userId = UserDaoFactory.getInstance().saveUser(user);
            if (userId == 0) {
                Helper.print("An exception occurred while inserting. Please, repeat");
                return;
            }
            if(Helper.confirm("User added successfully. Observe user's autos list? y/n")) {
                new AutoExplorer(user.getAutos(), userId).explore();
            }
        } catch (IOException e) {
            Helper.print("An exception occurred while inserting user. Please, try again.");
        }
    }
}
