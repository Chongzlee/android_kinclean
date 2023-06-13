package com.example.kinclean;

public class Data_Food {



    private int food_id;
    private String food_name;
    private String food_price;
    private String food_url;
    private  String food_type;
    private boolean signature;

    public Data_Food(int food_id, String food_name, String food_price, String food_url) {
        this.food_id = food_id;
        this.food_name = food_name;
        this.food_price = food_price;
        this.food_url = food_url;
    }

    public Data_Food(int food_id, String food_name, String food_price, String food_url, String food_type, boolean signature) {
        this.food_id = food_id;
        this.food_name = food_name;
        this.food_price = food_price;
        this.food_url = food_url;
        this.food_type = food_type;
        this.signature = signature;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public String getFood_price() {
        return food_price;
    }

    public void setFood_price(String food_price) {
        this.food_price = food_price;
    }

    public String getFood_url() {
        return food_url;
    }

    public void setFood_url(String food_url) {
        this.food_url = food_url;
    }

    public String getFood_type() {
        return food_type;
    }

    public void setFood_type(String food_type) {
        this.food_type = food_type;
    }

    public boolean isSignature() {
        return signature;
    }

    public void setSignature(boolean signature) {
        this.signature = signature;
    }

    @Override
    public String toString() {
        return "Foods{" +
                "food_id=" + food_id +
                ", food_name='" + food_name + '\'' +
                ", food_price=" + food_price +
                ", food_url='" + food_url + '\'' +
                '}';
    }
}
