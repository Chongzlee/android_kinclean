package com.example.kinclean;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Page_history extends AppCompatActivity {
    private RecyclerView orderreview;
    private ArrayList<Data_Order> selectedDataOrder;
    private Recycle_orderhistory adapter;

    private Button btnback, btnreceipt;
    private TextView allprice;

    private List<Data_Receipt> dataReceiptList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_page_history);

        selectedDataOrder = Data_OrderHistory.getInstance().getSelectedOrderList();

        orderreview = findViewById(R.id.contractrecycle2);
        btnback = findViewById(R.id.btnback2);
        allprice = findViewById(R.id.allpricehis);
        btnreceipt = findViewById(R.id.btnreceipt);
        ArrayList<Data_Receipt> dataReceipts = getIntent().getParcelableArrayListExtra("receipts");

        orderreview.setLayoutManager(new LinearLayoutManager(this));
        adapter = new Recycle_orderhistory(selectedDataOrder);
        orderreview.setAdapter(adapter);

        dataReceiptList = new ArrayList<>();


        // Retrieve the extra text value from the Intent

        // Calculate and display the total price
        int totalPrice = calculateTotalPrice(selectedDataOrder);
        allprice.setText("           ราคารวม :      " + " ฿ " + totalPrice);
        updateOrder(dataReceipts);

        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_history.this, Page_menu.class);
                intent.putParcelableArrayListExtra("receipts", dataReceipts);
                startActivity(intent);
            }
        });

        btnreceipt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showConfirmationDialog(selectedDataOrder, totalPrice,dataReceipts);
            }
        });
    }

    private int calculateTotalPrice(List<Data_Order> dataOrderList) {
        int totalPrice = 0;
        for (Data_Order dataOrder : dataOrderList) {
            int price = Integer.parseInt(dataOrder.getPrice());
            totalPrice += price * dataOrder.getQuantity();
        }
        return totalPrice;
    }

    private void showConfirmationDialog(List<Data_Order> dataOrderList, int totalPrice,ArrayList<Data_Receipt> receipts) {
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append(" ยืนยันเพื่อทำการชำระเงิน \nเลขบิลที่ : "+ receipts.get(0).getId()+"\n");

        for (Data_Order dataOrder : dataOrderList) {
            messageBuilder.append("- ").append(dataOrder.getName()).append(" (Quantity: ")
                    .append(dataOrder.getQuantity()).append(") - Price: ").append(Integer.parseInt(dataOrder.getPrice())* dataOrder.getQuantity())
                    .append(" ฿\n");
        }

        messageBuilder.append("\nราคารวม: ").append(totalPrice).append(" ฿");

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("ยืนยันการชำระเงิน");
        builder.setMessage(messageBuilder.toString());

        builder.setPositiveButton("ยืนยัน", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(Page_history.this, "Receipt generated successfully!", Toast.LENGTH_SHORT).show();

                // Clear all history orders
                dataOrderList.clear();
                adapter.notifyDataSetChanged();

                // Restart the app by relaunching the main activity
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        // Restart the application after 5 seconds
                        Intent intent = getBaseContext().getPackageManager().getLaunchIntentForPackage(getBaseContext().getPackageName());
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        System.exit(0);
                    }
                }, 2000); // Delay in milliseconds (5 seconds)// Finish the current activity to prevent going back to it using the back button
            }
        });

        builder.setNegativeButton("ยกเลิก", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void updateOrder(ArrayList<Data_Receipt> dataReceipts) {
        // Get the receipt number
        int receiptNo = dataReceipts.get(0).getId();

        // Construct the API endpoint URL
        String endpoint = "http://13.239.98.136:5000/updateTotalAmt/RecptNo=" + receiptNo;

        // Execute the API request in a separate thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    URL url = new URL(endpoint);
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("PATCH");
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
