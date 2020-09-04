package user.command;

import dao.UserDao;
import dao.DaoFactory;
import model.User;
import utils.Helper;

import java.sql.SQLException;
import java.util.List;

class ShowAllCommand implements Command {
    @Override
    public void execute() {
        UserDao dao = DaoFactory.getUserDao();
        List<User> users;
        try {
            users = dao.getAllUsers();
            users.forEach(Helper::print);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
}
