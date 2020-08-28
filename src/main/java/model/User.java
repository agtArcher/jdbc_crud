package model;

import java.util.ArrayList;
import java.util.List;

public class User {
    private int id;
    private String firstName;
    private String lastName;
    private int age;
    private List<Auto> autos;

    public User(String firstName, String lastName, int age) {
        id = -1;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        autos = new ArrayList<>();
    }

    public User(int id, String firstName, String lastName, int age, List<Auto> autos) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.autos = autos;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public int getAge() {
        return age;
    }

    public List<Auto> getAutos() {
        return autos;
    }

    public void setAutos(List<Auto> autos) {
        this.autos = autos;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("User: ").append("id=").append(id).append(", firstName='").append(firstName).append('\'').append(", lastName='");
        builder.append(lastName).append('\'').append(", age=").append(age).append("\n").append("Autos:\n");
        for (Auto auto : autos) {
            builder.append(auto).append("\n");
        }
        builder.delete(builder.lastIndexOf("\n"), builder.length());
        return builder.toString();
    }
}
