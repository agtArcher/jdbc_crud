package auto.controller;

import auto.AutoExplorer;
import model.Auto;
import auto.model.AutoModel;

import java.util.List;

public class AutoController {
    private final AutoModel autoModel;
    private final AutoExplorer executor;

    public AutoController(AutoModel autoModel, AutoExplorer executor) {
        this.autoModel = autoModel;
        this.executor = executor;
    }

    public void addAuto(int userId, String model, int prodYear) {
        autoModel.addAuto(userId, model, prodYear);
    }

    public void updateAuto(Auto auto) {
        autoModel.updateAuto(auto);
        executor.updateList(autoModel.getAutos());
    }

    public Auto getAutoById(int autoId) {
        return autoModel.getAutoById(autoId);
    }

    public void deleteAuto(Auto auto) {
        autoModel.deleteAuto(auto);
        executor.updateList(autoModel.getAutos());
    }

    public void save(int userId) {
        autoModel.save(userId);
        executor.updateList(autoModel.getAutos());
    }

    public void undo() {
        autoModel.undo();
        executor.updateList(autoModel.getAutos());
    }

    public void checkUnsavedOperations(int userId) {
        autoModel.checkUnsavedOperations(userId);
    }

    public boolean isVisible() {
        return autoModel.isVisible();
    }

    public List<Auto> getInsertList() {
        return autoModel.getInsertList();
    }
}
