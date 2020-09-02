package auto.model;

import model.Auto;

import java.util.List;

class AutoDto {
    private final List<Auto> backupList;
    private final List<Auto> backupInsertList;
    private final Auto changedAuto;
    private final CommandType commandType;

    public AutoDto(List<Auto> backupList, List<Auto> backupInsertList, Auto changedAuto, CommandType commandType) {
        this.backupList = backupList;
        this.backupInsertList = backupInsertList;
        this.changedAuto = changedAuto;
        this.commandType = commandType;
    }

    public List<Auto> getBackupList() {
        return backupList;
    }

    public List<Auto> getBackupInsertList() {
        return backupInsertList;
    }

    public Auto getChangedAuto() {
        return changedAuto;
    }

    public CommandType getCommandType() {
        return commandType;
    }
}
