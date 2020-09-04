package auto.model;

import model.Auto;

import java.util.List;

class AutoDto {
    //contains backup of autos list or insertList depends from commandType.
    //if commandType is insert or temporary, saves insertList, else - autos list.
    private final List<Auto> listBackup;
    private final Auto changedAuto;
    private final CommandType commandType;

    public AutoDto(List<Auto> listBackup, Auto changedAuto, CommandType commandType) {
        this.listBackup = listBackup;
        this.changedAuto = changedAuto;
        this.commandType = commandType;
    }

    public List<Auto> getListBackup() {
        return listBackup;
    }

    public Auto getChangedAuto() {
        return changedAuto;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
