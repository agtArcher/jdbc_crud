package auto.model;

import dao.UserDao;
import dao.UserDaoFactory;
import model.Auto;
import utils.Helper;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class AutoModelImpl implements AutoModel {
    //store previous states of autos list
    private final Stack<List<Auto>> autoListStack = new Stack<>();

    private final Stack<Auto> updateStack = new Stack<>();
    //store previous states of insert list
    private final Stack<List<Auto>> insertListStack = new Stack<>();
    //store auto_id for deleting
    private final Stack<Integer> deleteStack = new Stack<>();
    //save 'd' for delete operation, 'u' for update and 't' for actions
    //with insertAutoList
    private final Stack<Character> operationStack = new Stack<>();
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
        operationStack.push('t');
        insertListStack.push(new ArrayList<>(insertAutoList));
        insertAutoList.add(auto);
        Helper.print("Insert command added to query.");
        if (!visible)
            visible = true;
    }


    @Override
    public void updateAuto(Auto auto) {
        if (auto.getAutoId() > 0) {
            List<Auto> copyAutos = new ArrayList<>(autos);
            updateStack.push(auto);
            operationStack.push('u');
            autoListStack.push(copyAutos);
            autos = autos.stream().map(o -> o.getAutoId() == auto.getAutoId() ? auto : o).collect(Collectors.toList());
            Helper.print("Update command added to query.");
            if (!visible) {
                visible = true;
            }
        } else if (auto.getAutoId() < 0) {
            operationStack.push('t');
            insertListStack.push(new ArrayList<>(insertAutoList));
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
            deleteStack.push(auto.getAutoId());
            operationStack.push('d');
            autoListStack.push(new ArrayList<>(autos));
            autos.remove(auto);
            Helper.print("Delete command added to query.");
            if (!visible) {
                visible = true;
            }
        } else if (auto.getAutoId() < 0) {
            insertListStack.push(new ArrayList<>(insertAutoList));
            operationStack.push('t');
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
        UserDao dao = UserDaoFactory.getInstance();
        if (!insertAutoList.isEmpty()) {
            Helper.print("Inserting...");
            for (Auto insertedAuto : insertAutoList) {
                if(!dao.insertAuto(insertedAuto)) {
                    Helper.print("Insert operation failed, info about object: " + insertedAuto.toString());
                }
            }
            insertAutoList.clear();
            insertListStack.clear();
            count = 0;
        }
        if (!updateStack.isEmpty()) {
            Helper.print("Updating...");
            while (!updateStack.isEmpty()) {
                Auto updatedAuto = updateStack.pop();
                if(!dao.updateAuto(updatedAuto)) {
                    Helper.print("Update operation failed, info about object: " + updatedAuto.toString());
                }
            }
        }
        if (!deleteStack.isEmpty()) {
            Helper.print("Deleting...");
            while (!deleteStack.isEmpty()) {
                int autoId = deleteStack.pop();
                if(!dao.deleteAuto(autoId)) {
                    Helper.print("Delete operation failed, object's id: " + autoId);
                }
            }
        }
        autoListStack.clear();
        visible = false;
        Helper.print("Saved!");
        autos = UserDaoFactory.getInstance().getAutoForUser(userId);
    }

    //return autos list to previous state, if change stack will empty, set visible to false
    @Override
    public void undo() {
        char command = operationStack.pop();
        switch (command) {
            case 't':
                setInsertAutoList(insertListStack.pop());
                break;
            case 'u':
                updateStack.pop();
                autos = autoListStack.pop();
                break;
            case 'd':
                deleteStack.pop();
                autos = autoListStack.pop();
                break;
        }
        if (autoListStack.isEmpty() && insertListStack.isEmpty()) {
            visible = false;
        }
        Helper.print("Undo complete!");
    }

    //check for unsaved actions and ask for save actions
    @Override
    public void checkUnsavedOperations(int userId) {
        if (!autoListStack.isEmpty() || !insertAutoList.isEmpty()) {
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

    private void setInsertAutoList(List<Auto> insertAutoList) {
        this.insertAutoList = insertAutoList;
    }


}
