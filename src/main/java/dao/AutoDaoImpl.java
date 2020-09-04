package dao;

import model.Auto;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;

class AutoDaoImpl implements AutoDao {
    @Override
    public boolean insertAuto(Auto auto) throws SQLException {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into autos (model, prod_year, user_id) values (?,?,?)");
            preparedStatement.setString(1, auto.getModel());
            preparedStatement.setInt(2, auto.getProdYear());
            preparedStatement.setInt(3, auto.getUserId());
            int result = preparedStatement.executeUpdate();
            return result > 0;

        }
    }

    @Override
    public boolean[] insertAllAuto(Collection<Auto> autos) throws SQLException {
        boolean[] queryResults = new boolean[autos.size()];
        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement preparedStatement = connection.prepareStatement("insert into autos (model, prod_year, user_id) values (?,?,?)");
            for (Auto auto : autos) {

                preparedStatement.setString(1, auto.getModel());
                preparedStatement.setInt(2, auto.getProdYear());
                preparedStatement.setInt(3, auto.getUserId());
                preparedStatement.addBatch();
            }
            int[] batchResult = preparedStatement.executeBatch();
            for (int i = 0; i < batchResult.length; i++) {
                queryResults[i] = batchResult[i] > 0;
            }

        }
        return queryResults;
    }

    @Override
    public boolean updateAuto(Auto auto) throws SQLException {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement preparedStatement = connection.prepareStatement("update autos set model = ?, prod_year = ? where auto_id = ?");
            preparedStatement.setString(1, auto.getModel());
            preparedStatement.setInt(2, auto.getProdYear());
            preparedStatement.setInt(3, auto.getAutoId());
            int result = preparedStatement.executeUpdate();
            return result > 0;

        }
    }

    @Override
    public boolean deleteAuto(int autoId) throws SQLException {

        try (Connection connection = DriverManager.getConnection("jdbc:postgresql://localhost:5432/postgres", "postgres", "Archer215")) {
            PreparedStatement preparedStatement = connection.prepareStatement("delete from autos where auto_id = ?");
            preparedStatement.setInt(1, autoId);
            int result = preparedStatement.executeUpdate();
            return result > 0;

        }
    }
}
