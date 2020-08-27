package executors;

import dao.UserDao;
import dao.UserDaoFactory;
import model.Auto;
import utils.Helper;

import java.io.IOException;
import java.util.List;
import java.util.Stack;
import java.util.stream.Collectors;

public class AutoCommandExecutor {
    //store previous states of autos list
    private final Stack<List<Auto>> autoListStack = new Stack<>();
    private final Stack<Auto> updateStack = new Stack<>();
    private final Stack<Auto> insertStack = new Stack<>();
    //store auto_id for deleting
    private final Stack<Integer> deleteStack = new Stack<>();
    private final Stack<Character> operationStack = new Stack<>();
    //if visible is true, save and undo options are available
    private boolean visible = false;

    //intrance method, used to manipulate auto list.
    //while user not input 0, user can add, update, delete list. User can undo last action until action stack not empty.
    public void observe(List<Auto> autos, int user_id) throws IOException {

        while (true) {
            Helper.print("Auto's belongs to user_id " + user_id);
            autos.forEach(Helper::print);
            if (visible) {
                Helper.print("Actions:\n(1) Add new auto\n(2) Update info about auto\n(3) Delete auto\n(4) Save changes\n(5) Undo last change\n(0) Return");
            } else {
                Helper.print("Actions:\n(1) Add new auto\n(2) Update info about auto\n(3) Delete auto\n(0) Return");
            }

            int respond = Helper.getInteger();
            switch (respond) {
                case 1:
                    addAuto(autos, user_id);
                    break;
                case 2:
                    autos = updateAuto(autos);
                    break;
                case 3:
                    deleteAuto(autos);
                    break;
                case 0:
                    return;
            }
            if (visible && respond == 4) {
                save();
            } else if (visible && respond == 5) {
                autos = undo();
            } else {
                Helper.print("Incorrect command. Please, try again");
            }

        }

    }

    //request model and prodYear from user, create new Auto and add to autos list and insert stack
    private void addAuto(List<Auto> autos, int user_id) {

        try {
            Helper.print("Model:");
            String model = Helper.getString();
            Helper.print("Production year:");
            int prodYear = Helper.getInteger();
            Auto auto = new Auto(model, prodYear, user_id);
            insertStack.push(auto);
            operationStack.push('i');
            autoListStack.push(autos);
            autos.add(auto);
            Helper.print("Insert command added to query.");
            if (!visible)
                visible = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //request auto_id from user, then request confirm to change data, change field if user input 'y'
    //if no change found, cancel update and return to observe method
    //if change found, add updated auto to update stack
    private List<Auto> updateAuto(List<Auto> autos) throws IOException {
        Helper.print("Enter auto_id to update info");
        int auto_id = Helper.getInteger();

        Auto toUpdate = getAutoById(autos, auto_id);
        if (toUpdate == null) {
            Helper.print("Auto not found for update. Cancel operation");
        }
        else {
            boolean changed = false;
            if (confirm("Change model? y/n")) {
                Helper.print("Enter new value for model");
                String newModel = Helper.getString();
                toUpdate.setModel(newModel);
                changed = true;
            }
            if (confirm("Change production year? y/n")) {
                Helper.print("Enter new value for production year");
                int prodYear = Helper.getInteger();
                toUpdate.setProdYear(prodYear);
                if (!changed)
                    changed = true;
            }
            if (changed) {
                updateStack.push(toUpdate);
                operationStack.push('u');
                autoListStack.push(autos);
                autos = autos.stream().map(o -> o.getAutoId() == auto_id ? toUpdate : o).collect(Collectors.toList());
                Helper.print("Update command added to query.");
                if (!visible)
                    visible = true;
            } else {
                Helper.print("No change found");
            }
        }
        return autos;
    }

    //request auto_id to delete from user, then request confirm. if user input 'y', add auto_id to delete stack
    private void deleteAuto(List<Auto> autos) throws IOException {
        Helper.print("Enter auto_id to delete from list");
        int auto_id = Helper.getInteger();
        Auto autoToDelete = getAutoById(autos, auto_id);
        if (autoToDelete == null) {
            Helper.print("Auto not found. Cancel operation.");
        }
        else {
            if (confirm("Confirm operation to delete auto y/n")) {
                deleteStack.push(auto_id);
                operationStack.push('d');
                autoListStack.push(autos);
                autos.remove(autoToDelete);
                Helper.print("Delete command added to query.");
                if (!visible)
                    visible = true;
            }
        }
    }

    //get dao object from factory and perform all queries, clear undo stack, set visible boolean to false
    private void save() {
        UserDao dao = UserDaoFactory.getInstance();
        while (!insertStack.isEmpty()) {
            dao.insertAuto(insertStack.pop());
        }
        while (!updateStack.isEmpty()) {
            dao.updateAuto(updateStack.pop());
        }
        while (!deleteStack.isEmpty()) {
            dao.deleteAuto(deleteStack.pop());
        }
        autoListStack.clear();
        visible = false;
    }

    //return autos list to previous state, if change stack will empty, set visible to false
    private List<Auto> undo() {
        char command = operationStack.pop();
        List<Auto> autos = autoListStack.pop();
        switch (command) {
            case 'i':
                insertStack.pop();
                break;
            case 'u':
                updateStack.pop();
                break;
            case 'd':
                deleteStack.pop();
                break;
        }
        if (autoListStack.isEmpty()) {
            visible = false;
        }
        return autos;
    }

    //get auto by id from list
    private Auto getAutoById(List<Auto> autos, int auto_id) {

        List<Auto> updateAutoList = autos.stream().filter(i -> i.getAutoId() == auto_id).collect(Collectors.toList());
        if (updateAutoList.isEmpty()) {
            return null;
        }
        return updateAutoList.get(0);
    }

    //request from user answer. if respond start with 'y' return true, if 'n' - false. else wait
    private boolean confirm(String message) {
        Helper.print(message);
        try {
            while (true) {
                String confirm = Helper.getString().toLowerCase();
                if (confirm.startsWith("y")) {
                    return true;
                } else if (confirm.startsWith("n")) {
                    return false;
                }
            }
        } catch (IOException e) {
            Helper.print("Some exception occured. Try again");
            return confirm(message);
        }
    }

}
