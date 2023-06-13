package com.example.kinclean;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Page_order extends AppCompatActivity {
    private RecyclerView orderReviewRecyclerView;
    private Recycle_order adapter;
    private ArrayList<Data_Order> selectedDataOrderList;

    private Button backButton, goOnButton;
    private TextView totalPriceTextView;

    private int seq;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_page_order);

        backButton = findViewById(R.id.btnback);
        goOnButton = findViewById(R.id.btngoon);
        totalPriceTextView = findViewById(R.id.allprice);
        orderReviewRecyclerView = findViewById(R.id.recyclerView);
        orderReviewRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<Data_Receipt> dataReceipts = getIntent().getParcelableArrayListExtra("receipts");
        Log.d("Order_page", "receipt: " + dataReceipts.toString());

        goOnButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Add the selected orders to the previous orders list
                ArrayList<Data_Order> previousDataOrders = Data_OrderHistory.getInstance().getSelectedOrderList();
                previousDataOrders.addAll(selectedDataOrderList);
                Data_OrderHistory.getInstance().setSelectedOrderList(previousDataOrders);

                // Perform the API request to create the order list
                createOrderList(dataReceipts);

                // Change the text of the TextView in the current activity


                // Start the Page_history activity
                Intent intent = new Intent(Page_order.this, Page_history.class);
                intent.putParcelableArrayListExtra("receipts", dataReceipts);
                startActivity(intent);
            }
        });


        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Clear the selectedOrderList
                selectedDataOrderList.clear();

                // Start the Menuview activity
                Intent intent = new Intent(Page_order.this, Page_menu.class);
                intent.putParcelableArrayListExtra("receipts", dataReceipts);
                Log.d("Order_page", "receipt: " + dataReceipts.toString());
                startActivity(intent);
            }
        });

        // Retrieve the selectedOrderList ArrayList from the ContactReview activity
        Intent intent = getIntent();
        selectedDataOrderList = intent.getParcelableArrayListExtra("selectedOrderList");

        // Update the recyclerView with the selectedOrderList
        adapter = new Recycle_order(selectedDataOrderList, this);
        orderReviewRecyclerView.setAdapter(adapter);

        // Calculate and display the total price
        int totalPrice = calculateTotalPrice(selectedDataOrderList);
        totalPriceTextView.setText("           ราคารวม :      " + " ฿ " + totalPrice);
    }

    private int calculateTotalPrice(List<Data_Order> dataOrderList) {
        int totalPrice = 0;
        for (Data_Order dataOrder : dataOrderList) {
            int price = Integer.parseInt(dataOrder.getPrice());
            totalPrice += price * dataOrder.getQuantity();
        }
        return totalPrice;
    }

    private void createOrderList(ArrayList<Data_Receipt> dataReceipts) {
        // Retrieve the first receipt from the receipts list
        Data_Receipt firstDataReceipt = dataReceipts.get(0);
        int receiptNo = firstDataReceipt.getId();
        int seq = Integer.parseInt(firstDataReceipt.getSeq());

        for (Data_Order dataOrder : selectedDataOrderList) {
            // Increment the sequence number for each order
            seq++;

            // Update the sequence number in the first receipt
            firstDataReceipt.setSeq(String.valueOf(seq));

            // Construct the API endpoint URL
            StringBuilder endpointBuilder = new StringBuilder("http://13.239.98.136:5000/createOrderList/RecptNo=");
            endpointBuilder.append(receiptNo)
                    .append("/Seq=")
                    .append(seq);

            // Append FoodName and Qty for the current order
            String foodName = dataOrder.getName();
            int qty = dataOrder.getQuantity();
            endpointBuilder.append("/FoodName=")
                    .append(foodName)
                    .append("/Qty=")
                    .append(qty);

            final String endpoint = endpointBuilder.toString();

            // Execute the API request in a separate thread
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = new URL(endpoint);
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                        connection.setDoOutput(true);

                        int responseCode = connection.getResponseCode();

                        if (responseCode == HttpURLConnection.HTTP_OK) {
                            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                            String line;
                            StringBuilder response = new StringBuilder();
                            while ((line = reader.readLine()) != null) {
                                response.append(line);
                            }
                            reader.close();

                            Log.d("Order_page", "API Response: " + response.toString());
                        } else {
                            Log.e("Order_page", "API Request Failed. Response Code: " + responseCode);
                        }

                        connection.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();

        }
    }

    public void updateTotalPrice() {
        int totalPrice = calculateTotalPrice(selectedDataOrderList);
        totalPriceTextView.setText("    Total Price:             " + totalPrice + " ฿");
    }
}
