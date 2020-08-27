package model;

import java.util.Objects;

public class Auto {
    private int autoId;
    private String model;
    private int prodYear;
    private int userId;

    public Auto(String model, int prodYear, int userId) {
        autoId = -1;
        this.model = model;
        this.prodYear = prodYear;
        this.userId = userId;
    }

    public Auto(int autoId, String model, int prodYear, int userId) {
        this.autoId = autoId;
        this.model = model;
        this.prodYear = prodYear;
        this.userId = userId;
    }

    public int getAutoId() {
        return autoId;
    }

    public String getModel() {
        return model;
    }

    public int getProdYear() {
        return prodYear;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setProdYear(int prodYear) {
        this.prodYear = prodYear;
    }

    public int getUserId() {
        return userId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Auto auto = (Auto) o;
        return autoId == auto.autoId &&
                prodYear == auto.prodYear &&
                userId == auto.userId &&
                Objects.equals(model, auto.model);
    }

    @Override
    public int hashCode() {
        return Objects.hash(autoId, model, prodYear, userId);
    }

    @Override
    public String toString() {
        return "autoId=" + autoId +
                ", model='" + model + '\'' +
                ", prodYear=" + prodYear +
                ", userId=" + userId;
    }
}
