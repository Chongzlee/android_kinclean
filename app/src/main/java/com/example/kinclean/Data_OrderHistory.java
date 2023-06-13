package com.example.kinclean;

import java.util.ArrayList;

public class Data_OrderHistory {
    private static Data_OrderHistory instance;
    private ArrayList<Data_Order> selectedDataOrderList;

    private Data_OrderHistory() {
        selectedDataOrderList = new ArrayList<>();
    }

    public static Data_OrderHistory getInstance() {
        if (instance == null) {
            instance = new Data_OrderHistory();
        }
        return instance;
    }

    public ArrayList<Data_Order> getSelectedOrderList() {
        return selectedDataOrderList;
    }

    public void setSelectedOrderList(ArrayList<Data_Order> selectedDataOrderList) {
        this.selectedDataOrderList = selectedDataOrderList;
    }
}
