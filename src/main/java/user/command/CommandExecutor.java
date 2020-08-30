package user.command;

import utils.Helper;

public class CommandExecutor {
    public void execute(int command) {
        switch (command) {
            case 1:
                new InsertCommand().execute();
                break;
            case 2:
                new UpdateCommand().execute();
                break;
            case 3:
                new DeleteCommand().execute();
                break;
            case 4:
                new ShowCommand().execute();
                break;
            case 5:
                new ShowAllCommand().execute();
                break;
            default:
                Helper.print("Incorrect command. Please, repeat");
        }
    }
}
