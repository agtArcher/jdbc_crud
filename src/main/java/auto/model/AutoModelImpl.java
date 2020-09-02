package auto.model;

import dao.AutoDao;
import dao.DaoFactory;
import model.Auto;
import utils.Helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class AutoModelImpl implements AutoModel {
    //contains backup list depends from command type and changed auto inside
    private final Stack<AutoDto> operationVault = new Stack<>();
    //list for unsaved autos to separate update and delete
    private List<Auto> insertAutoList = new ArrayList<>();
    //if visible is true, save and undo options are available
    private boolean visible = false;
    //for initializing temporary id to auto for insert
    private int count = 0;
    private List<Auto> autos;

    public AutoModelImpl(List<Auto> autos) {
        this.autos = autos;
    }

    //request model and prodYear from user, create new Auto and add to temp list for separate u,d actions
    @Override
    public void addAuto(int userId, String model, int prodYear) {
        count--;
        Auto auto = new Auto(count, model, prodYear, userId);
        operationVault.push(new AutoDto(null, new ArrayList<>(insertAutoList), auto, CommandType.INSERT));
        insertAutoList.add(auto);
        Helper.print("Insert command added to query.");
        if (!visible)
            visible = true;
    }

    @Override
    public void updateAuto(Auto auto) {
        if (auto.getAutoId() > 0) {
            operationVault.push(new AutoDto(new ArrayList<>(autos), null, auto, CommandType.UPDATE));
            autos = autos.stream().map(o -> o.getAutoId() == auto.getAutoId() ? auto : o).collect(Collectors.toList());
            Helper.print("Update command added to query.");
            if (!visible) {
                visible = true;
            }
        } else if (auto.getAutoId() < 0) {
            operationVault.push(new AutoDto(null, new ArrayList<>(insertAutoList), null, CommandType.ACTION_TEMPORARY));
            insertAutoList = insertAutoList.stream().map(o -> o.getAutoId() == auto.getAutoId() ? auto : o).collect(Collectors.toList());
            Helper.print("Auto updated.");
            if (!visible) {
                visible = true;
            }
        }
    }

    @Override
    public void deleteAuto(Auto auto) {
        if (auto.getAutoId() > 0) {
            operationVault.push(new AutoDto(new ArrayList<>(autos), null, auto, CommandType.DELETE));
            autos.remove(auto);
            Helper.print("Delete command added to query.");
            if (!visible) {
                visible = true;
            }
        } else if (auto.getAutoId() < 0) {
            operationVault.push(new AutoDto(null, new ArrayList<>(insertAutoList), null, CommandType.ACTION_TEMPORARY));
            insertAutoList.remove(auto);
            Helper.print("Auto deleted.");
            if (!visible) {
                visible = true;
            }
        }
    }

    //get dao object from factory and perform all queries, clear undo stack, set visible boolean to false
    @Override
    public void save(int userId) {
        AutoDao dao = DaoFactory.getAutoDao();
        if (!insertAutoList.isEmpty()) {
            Helper.print("Inserting...");
            boolean[] queryResult = dao.insertAllAuto(insertAutoList);
            for (int i = 0; i < queryResult.length; i++) {
                if (!queryResult[i]) {
                    Helper.print("Insert operation failed, info about object: " + insertAutoList.get(i).toString());
                }
            }
            insertAutoList.clear();
            count = 0;
            operationVault.removeIf(x -> x.getCommandType() == CommandType.INSERT || x.getCommandType() == CommandType.ACTION_TEMPORARY);
        }

        List<AutoDto> toUpdate = operationVault.stream().filter(x -> x.getCommandType() == CommandType.UPDATE).collect(Collectors.toList());
        Collections.reverse(toUpdate);
        if (!toUpdate.isEmpty()) {
            Helper.print("Updating...");
            while (!toUpdate.isEmpty()) {
                Auto updatedAuto = toUpdate.get(0).getChangedAuto();
                if(!dao.updateAuto(updatedAuto)) {
                    Helper.print("Update operation failed, info about object: " + updatedAuto.toString());
                } else {
                    toUpdate.removeIf(x -> x.getChangedAuto().getAutoId() == updatedAuto.getAutoId());
                }
            }
            operationVault.removeIf(x -> x.getCommandType() == CommandType.UPDATE);
        }

        List<AutoDto> toDelete = operationVault.stream().filter(x -> x.getCommandType() == CommandType.DELETE).collect(Collectors.toList());
        if (!toDelete.isEmpty()) {
            Helper.print("Deleting...");
            for (AutoDto autoDto : toDelete) {
                int autoId = autoDto.getChangedAuto().getAutoId();
                if(!dao.deleteAuto(autoId)) {
                    Helper.print("Delete operation failed, object's id: " + autoId);
                }
            }
        }
        operationVault.clear();
        visible = false;
        Helper.print("Saved!");
        autos = DaoFactory.getUserDao().getAutoForUser(userId);
    }

    //return autos list to previous state, if change stack will empty, set visible to false
    @Override
    public void undo() {
        AutoDto undoAutoDto = operationVault.pop();
        switch (undoAutoDto.getCommandType()) {
            case INSERT:
            case ACTION_TEMPORARY:
                insertAutoList = undoAutoDto.getBackupInsertList();
                break;
            case DELETE:
            case UPDATE:
                autos = undoAutoDto.getBackupList();
                break;
        }
    }

    //check for unsaved actions and ask for save actions
    @Override
    public void checkUnsavedOperations(int userId) {
        if (!operationVault.isEmpty()) {
            if (Helper.confirm("Unsaved data found. Save changes? y/n")) {
                save(userId);
            }
        }
    }

    @Override
    public boolean isVisible() {
        return visible;
    }

    @Override
    public List<Auto> getInsertList() {
        return insertAutoList;
    }

    @Override
    //get copy of auto by id from list for correct save list in listStack
    public Auto getAutoById(int autoId) {
        List<Auto> updateAutoList = null;
        if (autoId > 0) {
            updateAutoList = autos.stream().filter(i -> i.getAutoId() == autoId).collect(Collectors.toList());
        } else if (autoId < 0) {
            updateAutoList = insertAutoList.stream().filter(i -> i.getAutoId() == autoId).collect(Collectors.toList());
        }

        if (updateAutoList == null || updateAutoList.isEmpty()) {
            return null;
        }
        Auto origAuto = updateAutoList.get(0);
        return new Auto(origAuto.getAutoId(), origAuto.getModel(), origAuto.getProdYear(), origAuto.getUserId());
    }

    public List<Auto> getAutos() {
        return autos;
    }
}
