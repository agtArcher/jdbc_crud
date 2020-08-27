package dao;

import model.Auto;
import model.User;

import java.util.List;

public interface UserDao {
    int saveUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int userId);
    User getUser(int userId);
    boolean insertAuto(Auto auto);
    boolean updateAuto(Auto auto);
    boolean deleteAuto(int autoId);
    List<User> getAllUsers();
    List<Auto> getAutoForUser(int userId);
}
