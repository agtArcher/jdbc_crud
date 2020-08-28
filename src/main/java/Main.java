import executors.CommandExecutor;
import executors.UserCommandExecutor;


public class Main {
    public static void main(String[] args) {
        CommandExecutor executor = new UserCommandExecutor();
        executor.observe();
    }

}
