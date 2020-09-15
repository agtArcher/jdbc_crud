package dao;

import model.Auto;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public interface AutoDao {
    boolean insertAuto(Auto auto) throws SQLException;
    boolean insertAuto(Connection connection, Auto auto) throws SQLException;
    boolean[] insertAllAuto(Collection<Auto> autos) throws SQLException;
    boolean[] insertAllAuto(Connection connection, Collection<Auto> autos) throws SQLException;
    boolean updateAuto(Auto auto) throws SQLException;
    boolean updateAuto(Connection connection, Auto auto) throws SQLException;
    boolean deleteAuto(int autoId) throws SQLException;
    boolean deleteAuto(Connection connection, int autoId) throws SQLException;
    Connection getConnection() throws SQLException;
}
