package dao;

import model.Auto;
import model.User;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;

public class UserDaoImpl implements UserDao {
    public boolean saveUser(User user) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into users (first_name, last_name, age) values (?,?,?)");
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setInt(3, user.getAge());
            int result = preparedStatement.executeUpdate();
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public int updateUser(User user) {
        return 0;
    }

    @Override
    public int deleteUser(int userId) {
        return 0;
    }

    public User getUser(int id) {
        return null;
    }

    public List<User> getAllUsers() {
        return null;
    }

    public List<Auto> getAutoForUser(int userId) {
        return null;
    }
}
