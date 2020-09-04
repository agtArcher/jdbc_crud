package dao;

import exception.ObjectNotFoundException;
import model.Auto;
import model.User;

import java.sql.SQLException;
import java.util.List;

public interface UserDao {
    int saveUser(User user) throws SQLException;
    boolean updateUser(User user) throws SQLException;
    boolean deleteUser(int userId) throws SQLException;
    User getUser(int userId) throws ObjectNotFoundException, SQLException;
    List<User> getAllUsers() throws SQLException;
    List<Auto> getAutoForUser(int userId) throws SQLException;
}
