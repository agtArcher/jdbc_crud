package executors;

import dao.UserDao;
import dao.UserDaoFactory;
import model.Auto;
import utils.Helper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class AutoCommandExecutor {
    //store previous states of autos list
    private final Stack<List<Auto>> autoListStack = new Stack<>();
    //list for unsaved autos to separate update and delete
    private List<Auto> insertAutoList = new ArrayList<>();
    private final Stack<Auto> updateStack = new Stack<>();
    //store previous states of insert list
    private final Stack<List<Auto>> insertListStack = new Stack<>();
    //store auto_id for deleting
    private final Stack<Integer> deleteStack = new Stack<>();
    //save 'd' for delete operation, 'u' for update and 't' for actions
    //with insertAutoList
    private final Stack<Character> operationStack = new Stack<>();
    //if visible is true, save and undo options are available
    private boolean visible = false;
    //for initializing temporary id to auto for insert
    private int count = 0;


    //entrance method, used to manipulate auto list.
    //while user not input 0, user can add, update, delete list. User can undo last action until action stack not empty.
    public void observe(List<Auto> autos, int userId) throws IOException {
        int respond;
        while (true) {
            if (autos.isEmpty() && insertAutoList.isEmpty()) {
                if (visible) {
                    Helper.print("Actions:\n(1)Add new auto\n(2) Save changes\n(3) Undo last change\n(0) Return");
                } else {
                    Helper.print("Actions:\n(1)Add new auto\n(0) Return");
                }
                respond = Helper.getInteger();
                switch (respond) {
                    case 1:
                        addAuto(userId);
                        break;
                    case 0:
                        checkUnsavedOperations(userId);
                        return;
                }
                if (visible && respond == 2) {
                    autos = save(userId);
                } else if (visible && respond == 3) {
                    autos = undo(autos);
                } else {
                    Helper.print("Incorrect command. Please, try again");
                }
            }
            else {
                Helper.print("Auto's belongs to user with id = " + userId);
                if (!autos.isEmpty()) {
                    Helper.print("Saved: ");
                    autos.forEach(Helper::print);
                }
                if (!insertAutoList.isEmpty()) {
                    Helper.print("Unsaved: ");
                    insertAutoList.forEach(Helper::print);
                    Helper.print("auto's id is temporary while not saved");
                }
                if (visible) {
                    Helper.print("Actions:\n(1) Add new auto\n(2) Update info about auto\n(3) Delete auto\n(4) Save changes\n(5) Undo last change\n(0) Return");
                } else {
                    Helper.print("Actions:\n(1) Add new auto\n(2) Update info about auto\n(3) Delete auto\n(0) Return");
                }

                respond = Helper.getInteger();
                switch (respond) {
                    case 1:
                        addAuto(userId);
                        break;
                    case 2:
                        autos = updateAuto(autos);
                        break;
                    case 3:
                        deleteAuto(autos);
                        break;
                    case 0:
                        checkUnsavedOperations(userId);
                        return;
                }
                if (visible && respond == 4) {
                    autos = save(userId);
                } else if (visible && respond == 5) {
                    autos = undo(autos);
                } else {
                    Helper.print("Incorrect command. Please, try again");
                }
            }
        }
    }

    //request model and prodYear from user, create new Auto and add to temp list for separate u,d actions
    private void addAuto(int userId) {

        try {
            String model = Helper.getString("Model:");
            int prodYear = Helper.getInteger("Production year:");
            count--;
            Auto auto = new Auto(count, model, prodYear, userId);
            operationStack.push('t');
            insertListStack.push(new ArrayList<>(insertAutoList));
            insertAutoList.add(auto);
            Helper.print("Insert command added to query.");
            if (!visible)
                visible = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //request auto_id from user, get copy of auto from list by id then request confirm to change data, change field if user input 'y'
    //if no change found, cancel update and return to observe method
    //if change found, add updated auto to update stack
    //if auto id < 0, add 't' to operation stack and update auto in insertList
    private List<Auto> updateAuto(List<Auto> autos) {
        try {
            Helper.print("Enter autoId to update info");
            int autoId = Helper.getInteger();
            List<Auto> copyAutos = new ArrayList<>(autos);
            Auto toUpdate = getAutoById(autos, autoId);
            if (toUpdate == null) {
                Helper.print("Auto not found for update. Cancel operation");
            }
            else {
                boolean hadChanged = false;
                //if value had changed, set hadChanged to true
                if (Helper.confirm("Change model? y/n")) {
                    String newModel = Helper.getString("Enter new value for model");
                    if (!toUpdate.getModel().equals(newModel)) {
                        toUpdate.setModel(newModel);
                        hadChanged = true;
                    }
                }
                if (Helper.confirm("Change production year? y/n")) {
                    int prodYear = Helper.getInteger("Enter new value for production year");
                    if (toUpdate.getProdYear() != prodYear) {
                        toUpdate.setProdYear(prodYear);
                        if (!hadChanged)
                            hadChanged = true;
                    }
                }
                if (hadChanged && autoId > 0) {
                    updateStack.push(toUpdate);
                    operationStack.push('u');
                    autoListStack.push(copyAutos);
                    autos = autos.stream().map(o -> o.getAutoId() == autoId ? toUpdate : o).collect(Collectors.toList());
                    Helper.print("Update command added to query.");
                    if (!visible) {
                        visible = true;
                    }
                } else if (hadChanged && autoId < 0) {
                    operationStack.push('t');
                    insertListStack.push(new ArrayList<>(insertAutoList));
                    insertAutoList = insertAutoList.stream().map(o -> o.getAutoId() == autoId ? toUpdate : o).collect(Collectors.toList());
                    Helper.print("Auto updated.");
                    if (!visible) {
                        visible = true;
                    }
                } else {
                    Helper.print("No change found");
                }
            }
        } catch (IOException e) {
            Helper.print("An exception occurred while updating auto. Please, try again.");
        }
        return autos;
    }

    //request auto_id to delete from user, then request confirm. if user input 'y', add auto_id to delete stack
    //save copy of list in appropriate stack
    private void deleteAuto(List<Auto> autos) {
        try {
            int autoId = Helper.getInteger("Enter autoId to delete from list");
            Auto autoToDelete = getAutoById(autos, autoId);
            if (autoToDelete == null) {
                Helper.print("Auto not found. Cancel operation.");
            }
            else {
                if (Helper.confirm("Confirm operation to delete auto y/n")) {
                    if (autoId > 0) {
                        deleteStack.push(autoId);
                        operationStack.push('d');
                        autoListStack.push(new ArrayList<>(autos));
                        autos.remove(autoToDelete);
                        Helper.print("Delete command added to query.");
                        if (!visible) {
                            visible = true;
                        }
                    } else if (autoId < 0) {
                        insertListStack.push(new ArrayList<>(insertAutoList));
                        operationStack.push('t');
                        insertAutoList.remove(autoToDelete);
                        Helper.print("Auto deleted.");
                        if (!visible) {
                            visible = true;
                        }
                    }
                }
            }
        } catch (IOException e) {
            Helper.print("An exception occurred while deleting auto. Please, try again.");
        }
    }

    //get dao object from factory and perform all queries, clear undo stack, set visible boolean to false
    private List<Auto> save(int userId) {
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
        return UserDaoFactory.getInstance().getAutoForUser(userId);
    }

    //return autos list to previous state, if change stack will empty, set visible to false
    private List<Auto> undo(List<Auto> autos) {
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
        return autos;
    }

    //get copy of auto by id from list for correct save list in listStack
    private Auto getAutoById(List<Auto> autos, int autoId) {
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

    //check for unsaved actions and ask for save actions
    private void checkUnsavedOperations(int userId) {
        if (!autoListStack.isEmpty() || !insertAutoList.isEmpty()) {
            if (Helper.confirm("Unsaved data found. Save changes? y/n")) {
                save(userId);
            }
        }
    }

    //private setter to change insertList
    private void setInsertAutoList(List<Auto> insertAutoList) {
        this.insertAutoList = insertAutoList;
    }
}
