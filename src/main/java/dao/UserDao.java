package dao;

import model.Auto;
import model.User;

import java.util.List;

public interface UserDao {
    boolean saveUser(User user);
    int updateUser(User user);
    int deleteUser(int userId);
    User getUser(int userId);
    List<User> getAllUsers();
    List<Auto> getAutoForUser(int userId);
}
