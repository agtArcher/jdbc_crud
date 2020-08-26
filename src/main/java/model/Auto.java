package model;

public class Auto {
    private int autoId;
    private String model;
    private String prodYear;
    private int userId;

    public Auto(String model, String prodYear, int userId) {
        this.model = model;
        this.prodYear = prodYear;
        this.userId = userId;
    }

    public String getModel() {
        return model;
    }

    public String getProdYear() {
        return prodYear;
    }

    public int getUserId() {
        return userId;
    }
}
