package dao;

import model.Auto;
import model.User;

import java.sql.*;
import java.util.ArrayList;
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
            if (result > 0 && !user.getAutos().isEmpty()) {
                preparedStatement = connection.prepareStatement("insert into autos (model, prod_year, user_id) values (?,?,?)");
                for (Auto auto : user.getAutos()) {
                    preparedStatement.setString(1, auto.getModel());
                    preparedStatement.setInt(2, auto.getProdYear());
                    preparedStatement.setInt(3, user.getId());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean updateUser(User user) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement preparedStatement = connection.prepareStatement("update users set first_name = ?, last_name = ?, age = ? where user_id = ?");
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setInt(3, user.getAge());
            preparedStatement.setInt(4, user.getId());
            int result = preparedStatement.executeUpdate();
            if (result > 0 && !user.getAutos().isEmpty()) {
                preparedStatement = connection.prepareStatement("update autos set model=?, prod_year = ?, user_id = ? where auto_id = ?");
                for (Auto auto : user.getAutos()) {
                    preparedStatement.setString(1, auto.getModel());
                    preparedStatement.setInt(2, auto.getProdYear());
                    preparedStatement.setInt(3, user.getId());
                    preparedStatement.setInt(4, auto.getAutoId());
                    preparedStatement.addBatch();
                }
                preparedStatement.executeBatch();
            }
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean deleteUser(int userId) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from users where user_id = ?");
            preparedStatement.setInt(1, userId);
            int result = preparedStatement.executeUpdate();
            if (result > 0) {
                preparedStatement = connection.prepareStatement("delete from autos where user_id = ?");
                preparedStatement.setInt(1, userId);
                preparedStatement.executeUpdate();
            }
            return result > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public User getUser(int id) {
        User user = null;
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from users where user_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int age = resultSet.getInt("age");
                preparedStatement = connection.prepareStatement("select * from autos where user_id = ?");
                preparedStatement.setInt(1, id);
                resultSet = preparedStatement.executeQuery();
                List<Auto> autos = getAutos(resultSet, id);
                user = new User(id, firstName, lastName, age, autos);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement userStatement = connection.prepareStatement("select * from users");
            PreparedStatement autoStatement = connection.prepareStatement("select * from autos where user_id = ?");
            ResultSet usersSet = userStatement.executeQuery();
            while (usersSet.next()) {
                int user_id = usersSet.getInt(1);
                String firstName = usersSet.getString(2);
                String lastName = usersSet.getString(3);
                int age = usersSet.getInt(4);
                autoStatement.setInt(1, user_id);

                ResultSet autoSet = autoStatement.executeQuery();
                List<Auto> autos = getAutos(autoSet, user_id);
                User user = new User(user_id, firstName, lastName, age, autos);
                users.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    private List<Auto> getAutos(ResultSet resultSet, int user_id) throws SQLException {
        List<Auto> autos = new ArrayList<>();
        while (resultSet.next()) {
            int auto_id = resultSet.getInt(1);
            String model = resultSet.getString(2);
            int prodYear = resultSet.getInt(3);
            Auto auto = new Auto(auto_id, model, prodYear, user_id);
            autos.add(auto);
        }
        return autos;
    }

    public List<Auto> getAutoForUser(int userId) {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement autoStatement = connection.prepareStatement("select * from autos where user_id = ?");
            autoStatement.setInt(1, userId);
            ResultSet autoSet = autoStatement.executeQuery();
            return getAutos(autoSet, userId);

        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}
