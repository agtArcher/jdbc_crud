package services;

import model.Auto;

import java.util.List;

public interface AutoModel {
    void addAuto(int userId, String model, int prodYear);
    void updateAuto(Auto auto);
    void deleteAuto(Auto auto);
    void save(int userId);
    void undo();
    void checkUnsavedOperations(int userId);
    boolean isVisible();
    List<Auto> getInsertList();
    Auto getAutoById(int autoId);
    List<Auto> getAutos();
}
