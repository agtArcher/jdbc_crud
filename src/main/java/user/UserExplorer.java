package user;

import utils.Helper;

import java.io.IOException;

public class UserExplorer {
    public void explore() {
        try {
            user.command.CommandExecutor executor = new user.command.CommandExecutor();
            while (true) {
                int respond = Helper.getInteger("Actions:\n(1) Add new user\n(2) Update user\n(3) Delete user\n(4) Show user by id\n(5) Show all users\n(0) Return");
                if (respond == 0) {
                    return;
                } else {
                    executor.execute(respond);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
