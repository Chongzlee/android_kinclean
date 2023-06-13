package com.example.kinclean;

import android.os.Parcel;
import android.os.Parcelable;

public class Data_Receipt implements Parcelable {
    private int id;
    private int no_table;
    private String specificDateTime;
    private String amount;
    private String seq;

    public Data_Receipt(int id, int no_table, String specificDateTime, String amount, String seq) {
        this.id = id;
        this.no_table = no_table;
        this.specificDateTime = specificDateTime;
        this.amount = amount;
        this.seq = seq;
    }

    protected Data_Receipt(Parcel in) {
        id = in.readInt();
        no_table = in.readInt();
        specificDateTime = in.readString();
        amount = in.readString();
        seq = in.readString();
    }

    public static final Creator<Data_Receipt> CREATOR = new Creator<Data_Receipt>() {
        @Override
        public Data_Receipt createFromParcel(Parcel in) {
            return new Data_Receipt(in);
        }

        @Override
        public Data_Receipt[] newArray(int size) {
            return new Data_Receipt[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getNo_table() {
        return no_table;
    }

    public void setNo_table(int no_table) {
        this.no_table = no_table;
    }

    public String getSpecificDateTime() {
        return specificDateTime;
    }

    public void setSpecificDateTime(String specificDateTime) {
        this.specificDateTime = specificDateTime;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getSeq() {
        return seq;
    }

    public void setSeq(String seq) {
        this.seq = seq;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(no_table);
        dest.writeString(specificDateTime);
        dest.writeString(amount);
        dest.writeString(seq);
    }

    @Override
    public String toString() {
        return "Receipt{" +
                "id=" + id +
                ", no_table=" + no_table +
                ", specificDateTime='" + specificDateTime + '\'' +
                ", amount='" + amount + '\'' +
                ", seq='" + seq + '\'' +
                '}';
    }
}
