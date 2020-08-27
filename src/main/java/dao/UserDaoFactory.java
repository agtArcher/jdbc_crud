package dao;

public class UserDaoFactory {
    private static UserDao dao;

    public static UserDao getInstance() {
        if (dao == null) {
            synchronized (UserDaoFactory.class) {
                dao = new UserDaoImpl();
            }
        }
        return dao;
    }
}
