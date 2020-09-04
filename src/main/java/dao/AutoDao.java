package dao;

import model.Auto;

import java.sql.SQLException;
import java.util.Collection;

public interface AutoDao {
    boolean insertAuto(Auto auto) throws SQLException;
    boolean[] insertAllAuto(Collection<Auto> autos) throws SQLException;
    boolean updateAuto(Auto auto) throws SQLException;
    boolean deleteAuto(int autoId) throws SQLException;
}
