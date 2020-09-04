package auto;

import auto.controller.AutoController;
import exception.ObjectNotFoundException;
import model.Auto;
import auto.model.AutoModelImpl;
import utils.Helper;

import java.io.IOException;
import java.util.List;

public class AutoExplorer {
    private final AutoController controller;
    private List<Auto> autos;
    private final int userId;

    public AutoExplorer(List<Auto> autos, int userId) {
        this.autos = autos;
        this.userId = userId;
        controller = new AutoController(new AutoModelImpl(autos), this);
    }

    //entrance method, used to manipulate auto list.
    //while user not input 0, user can add, update, delete list. User can undo last action until action stack not empty.
    public void explore() throws IOException {
        int respond;
        while (true) {
            boolean visible = controller.isVisible();
            if (autos.isEmpty() && controller.getInsertList().isEmpty()) {
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
                        controller.checkUnsavedOperations(userId);
                        return;
                    case 2:
                        if (visible) {
                            controller.save(userId);
                            break;
                        }
                    case 3:
                        if (visible) {
                            controller.undo();
                            break;
                        }
                    default:
                        Helper.print("Incorrect command. Please, try again");
                }
            }
            else {
                Helper.print("Auto's belongs to user with id = " + userId);
                if (!autos.isEmpty()) {
                    Helper.print("Saved: ");
                    autos.forEach(Helper::print);
                }
                if (!controller.getInsertList().isEmpty()) {
                    Helper.print("Unsaved: ");
                    controller.getInsertList().forEach(Helper::print);
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
                        updateAuto();
                        break;
                    case 3:
                        deleteAuto();
                        break;
                    case 0:
                        controller.checkUnsavedOperations(userId);
                        return;
                    case 4:
                        if (visible) {
                            controller.save(userId);
                            break;
                        }
                    case 5:
                        if (visible) {
                            controller.undo();
                            break;
                        }
                    default:
                        Helper.print("Incorrect command. Please, try again");
                }
            }
        }
    }

    public void updateList(List<Auto> autos) {
        this.autos = autos;
    }

    private void addAuto(int userId) {
        try {
            String model = Helper.getString("Model:");
            int prodYear = Helper.getInteger("Production year:");
            controller.addAuto(userId, model, prodYear);
        } catch (IOException e) {
            Helper.print("An exception occurred while adding auto");
        }
    }

    private void updateAuto() {
        try {
            Helper.print("Enter autoId to update info");
            int autoId = Helper.getInteger();

            Auto toUpdate = controller.getAutoById(autoId);
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
            if (hadChanged) {
                controller.updateAuto(toUpdate);
            } else {
                Helper.print("No change found");
            }
        } catch (IOException e) {
            Helper.print("An exception occurred while updating auto. Please, try again.");
        } catch (ObjectNotFoundException e) {
            Helper.print("Auto for update not found. Cancel operation");
        }
    }

    private void deleteAuto() {
        try {
            int autoId = Helper.getInteger("Enter autoId to delete from list");
            Auto autoToDelete = controller.getAutoById(autoId);
            if (Helper.confirm("Confirm operation to delete auto y/n")) {
                controller.deleteAuto(autoToDelete);
            }
        } catch (IOException e) {
            Helper.print("An exception occurred while deleting auto. Please, try again.");
        } catch (ObjectNotFoundException e) {
            Helper.print("Auto for delete not found. Cancel operation.");
        }
    }


}
