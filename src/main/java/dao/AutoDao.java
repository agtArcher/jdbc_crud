package dao;

import model.Auto;

import java.util.Collection;

public interface AutoDao {
    boolean insertAuto(Auto auto);
    boolean[] insertAllAuto(Collection<Auto> autos);
    boolean updateAuto(Auto auto);
    boolean deleteAuto(int autoId);
}
