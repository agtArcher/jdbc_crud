package dao;

public class DaoFactory {
    public static UserDao getUserDao() {
        return new UserDaoImpl();
    }

    public static AutoDao getAutoDao() {
        return new AutoDaoImpl();
    }

    private DaoFactory() {

    }
}
