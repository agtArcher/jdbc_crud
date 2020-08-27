package model;

public class Auto {
    private int autoId;
    private String model;
    private int prodYear;
    private int userId;

    public Auto(String model, int prodYear, int userId) {
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

    public int getUserId() {
        return userId;
    }
}
