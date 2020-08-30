package services;

import java.io.IOException;

public interface UserModel {
    void insertUser() throws IOException;
    void updateUser() throws IOException;
    void deleteUser() throws IOException;
    void showUser() throws IOException;
    void showAllUsers();

}
