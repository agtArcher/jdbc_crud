package user.command;

import dao.UserDao;
import dao.DaoFactory;
import model.User;
import utils.Helper;

import java.util.List;

class ShowAllCommand implements Command {
    @Override
    public void execute() {
        UserDao dao = DaoFactory.getUserDao();
        List<User> users = dao.getAllUsers();
        users.forEach(Helper::print);
    }
}
