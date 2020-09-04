package dao;

import exception.ObjectNotFoundException;
import model.Auto;
import model.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

class UserDaoImpl implements UserDao {

    UserDaoImpl() {
    }

    public int saveUser(User user) throws SQLException {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into users (first_name, last_name, age) values (?,?,?)", Statement.RETURN_GENERATED_KEYS);
            preparedStatement.setString(1, user.getFirstName());
            preparedStatement.setString(2, user.getLastName());
            preparedStatement.setInt(3, user.getAge());
            if (preparedStatement.executeUpdate() > 0) {
                ResultSet rs = preparedStatement.getGeneratedKeys();
                int id = 0;
                if (rs.next()) {
                    id = rs.getInt(1);
                    if (!user.getAutos().isEmpty()) {
                        preparedStatement = connection.prepareStatement("insert into autos (model, prod_year, user_id) values (?,?,?)");
                        for (Auto auto : user.getAutos()) {
                            preparedStatement.setString(1, auto.getModel());
                            preparedStatement.setInt(2, auto.getProdYear());
                            preparedStatement.setInt(3, user.getId());
                            preparedStatement.addBatch();
                        }
                        preparedStatement.executeBatch();
                    }
                }
                return id;
            }


        }
        return -1;
    }

    @Override
    public boolean updateUser(User user) throws SQLException {
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

        }
    }

    @Override
    public boolean deleteUser(int userId) throws SQLException {
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

        }
    }

    public User getUser(int id) throws ObjectNotFoundException, SQLException {
        User user;

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement preparedStatement = connection.prepareStatement("select * from users where user_id = ?");
            preparedStatement.setInt(1, id);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                int age = resultSet.getInt("age");

                List<Auto> autos = getAutoForUser(id);
                user = new User(id, firstName, lastName, age, autos);
            } else {
                throw new ObjectNotFoundException();
            }
        }
        return user;
    }



    public List<User> getAllUsers() throws SQLException {
        List<User> users = new ArrayList<>();

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
        }
        return users;
    }

    private List<Auto> getAutos(ResultSet autoSet, int user_id) throws SQLException {
        List<Auto> autos = new ArrayList<>();
        while (autoSet.next()) {
            int auto_id = autoSet.getInt(1);
            String model = autoSet.getString(2);
            int prodYear = autoSet.getInt(3);
            Auto auto = new Auto(auto_id, model, prodYear, user_id);
            autos.add(auto);
        }
        return autos;
    }

    public List<Auto> getAutoForUser(int userId) throws SQLException {
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

        }
    }
}
