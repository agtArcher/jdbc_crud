package dao;

import model.Auto;
import model.User;

import java.util.List;

public interface UserDao {
    boolean saveUser();
    int updateUser();
    int deleteUser();
    User getUser(int userId);
    List<User> getAllUsers();
    List<Auto> getAutoForUser(int userId);
}
