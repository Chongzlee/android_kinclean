package com.example.kinclean;

import android.os.Parcel;
import android.os.Parcelable;

public class Data_Order implements Parcelable {
    private int id;
    private String name;
    private String price;
    private int quantity;



    public Data_Order(int id, String name, String price, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.quantity = quantity;

    }

    protected Data_Order(Parcel in) {
        id = in.readInt();
        name = in.readString();
        price = in.readString();
        quantity = in.readInt();
        ;
    }

    public static final Creator<Data_Order> CREATOR = new Creator<Data_Order>() {
        @Override
        public Data_Order createFromParcel(Parcel in) {
            return new Data_Order(in);
        }

        @Override
        public Data_Order[] newArray(int size) {
            return new Data_Order[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }



    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
        dest.writeString(price);
        dest.writeInt(quantity);
    }
}

