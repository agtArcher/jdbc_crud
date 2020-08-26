package dao;

import model.User;

import java.util.List;

public interface UserDao {
    boolean saveUser();
    int updateUser();
    int deleteUser();
    User getUser(int id);
    List<User> getAllUsers();
}
