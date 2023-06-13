package com.example.kinclean;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class A_Firstpage extends AppCompatActivity {

    private RelativeLayout relativeLayout;
    private Button btnstart;
    private ArrayList<Data_Receipt> dataReceipts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_page_first);

        String tableNo = "123"; // Replace with the actual table number
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String receiptDate = now.format(formatter);

        // Construct the API URLs
        String apiUrl_createReceipt = "http://13.239.98.136:5000/createReceipt/TableNo=" + tableNo + "/Date=" + receiptDate;
        String apiUrl_readReceipt = "http://13.239.98.136:5000/ReadAReceipt";

        btnstart = findViewById(R.id.btnstart);
        btnstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(A_Firstpage.this, Page_menu.class);
                createReceipt(tableNo, receiptDate, apiUrl_createReceipt);
                getReceipt(tableNo, receiptDate, apiUrl_readReceipt, intent);
            }
        });
    }

    private void createReceipt(String tableNo, String receiptDate, String apiUrl) {
        Log.d("MainActivity", "CreateReceipt URL: " + apiUrl);

        // Create a new thread to perform the HTTP request
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create a URL object from the API URL string
                    URL url = new URL(apiUrl);

                    // Open a connection to the URL
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set the request method to POST
                    connection.setRequestMethod("POST");

                    // Get the response code from the connection
                    int responseCode = connection.getResponseCode();

                    // Check if the request was successful
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Read the response from the API
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Process the response as needed
                        String responseData = response.toString();
                        // TODO: Handle the response data

                    } else {
                        // Handle the error case
                        // TODO: Handle the error appropriately
                    }

                    // Close the connection
                    connection.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                    // TODO: Handle the exception appropriately
                }
            }
        }).start();
    }

    private void getReceipt(String tableNo, String receiptDate, String apiUrl, Intent intent) {
        Log.d("MainActivity", "ReadReceipt URL: " + apiUrl);

        // Create a new thread to perform the HTTP request
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // Create a URL object from the API URL string
                    URL url = new URL(apiUrl);

                    // Open a connection to the URL
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();

                    // Set the request method to GET
                    connection.setRequestMethod("GET");

                    // Get the response code from the connection
                    int responseCode = connection.getResponseCode();

                    // Check if the ReadAReceipt request was successful (200 indicates success)
                    if (responseCode == HttpURLConnection.HTTP_OK) {
                        // Read the response from the ReadAReceipt API
                        BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = reader.readLine()) != null) {
                            response.append(line);
                        }
                        reader.close();

                        // Process the ReadAReceipt response as needed
                        String responseData = response.toString();

                        // Parse the JSON response
                        try {
                            JSONObject jsonObject = new JSONObject(responseData);
                            JSONArray dataArray = jsonObject.getJSONArray("data");
                            JSONObject dataObject = dataArray.getJSONObject(0);
                            int receiptNumber = dataObject.getInt("RecptNo");

                            // Assign the receipt number to the receiptno variable
                            int receiptNo = receiptNumber;
                            String numberAsString = String.valueOf(receiptNo);

                            // Start the new activity after making the ReadAReceipt API request
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    // Create a new Receipt object
                                    ArrayList<Data_Receipt> dataReceipts = new ArrayList<>();
                                    Data_Receipt dataReceipt = new Data_Receipt(receiptNo, 123, receiptDate, "0", "-1");
                                    dataReceipts.add(dataReceipt);

                                    // Start the new activity after making the ReadAReceipt API request
                                    intent.putParcelableArrayListExtra("receipts", dataReceipts);
                                    Log.d("Receipt_page", "Receipt ID: " + dataReceipt.getId());
                                    startActivity(intent);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            // Handle JSON parsing exception for ReadAReceipt response
                            // TODO: Handle the exception appropriately
                        }
                    } else {
                        // Handle the error case for ReadAReceipt request
                        // TODO: Handle the error appropriately
                    }

                    // Close the ReadAReceipt connection
                    connection.disconnect();

                } catch (IOException e) {
                    e.printStackTrace();
                    // Handle the exception for ReadAReceipt request
                    // TODO: Handle the exception appropriately
                }
            }
        }).start();
    }
}

