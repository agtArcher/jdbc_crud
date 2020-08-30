package user.command;

import dao.UserDao;
import dao.UserDaoFactory;
import model.User;
import utils.Helper;

import java.util.List;

class ShowAllCommand implements Command {
    @Override
    public void execute() {
        UserDao dao = UserDaoFactory.getInstance();
        List<User> users = dao.getAllUsers();
        users.forEach(Helper::print);
    }
}
