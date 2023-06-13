package com.example.kinclean;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class Page_menu extends AppCompatActivity {
    private RecyclerView contractreview;
    private RelativeLayout relativeLayout;

    private Button btnsig, btnapp, btnspt, btnorg, btnstk, btncost;
    private TextView txttop;

    public void setBtncost(String text) {
        btncost.setText(text);
    }

    public void settxtorprice(String text){
        txtorprice.setText(text);
    }

    public void setTxtorcount(String text){
        txtorcount.setText(text);
    }
    public void settxtmycart(String text){
        txtormycart.setText(text);
    }

    private static Page_menu instance;

    private Recycle_menu adapter;
    public static Page_menu getInstance() {
        return instance;
    }

    private static final int ORDER_PAGE_REQUEST_CODE = 1;
    private String cost; // Update the variable to store the cost value
    private String seq;
    private int cnt;

    private TextView txtorcount,txtorprice,txtormycart;
    private Button btngo;
    private ArrayList<Data_Order> selectedDataOrder = new ArrayList<>();

    private ArrayList<Data_Food> foods = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_page_menu);

        instance = this;

        contractreview = findViewById(R.id.contractrecycle);

        btnsig = findViewById(R.id.btnsignature);
        btnapp = findViewById(R.id.btnappitizer);
        btnorg = findViewById(R.id.btnorganic);
        btnstk = findViewById(R.id.btnsteak);
        btnspt = findViewById(R.id.btnspagetti);
        txttop = findViewById(R.id.txttop);
        btncost = findViewById(R.id.btncost);
        btngo = findViewById(R.id.buttond);

        adapter = new Recycle_menu(this, selectedDataOrder);
        contractreview.setAdapter(adapter);
        contractreview.setLayoutManager(new GridLayoutManager(this, 3));

        ArrayList<Data_Receipt> dataReceipts = getIntent().getParcelableArrayListExtra("receipts");

        Log.d("menuview", "receipt: " + dataReceipts.toString());

        try {
            getData();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        btngo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_menu.this, Page_history.class);
                intent.putParcelableArrayListExtra("receipts", dataReceipts);
                intent.putParcelableArrayListExtra("selectedOrderList", selectedDataOrder);
                startActivity(intent);
            }
        });

        btncost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Page_menu.this, Page_order.class);
                intent.putParcelableArrayListExtra("selectedOrderList", selectedDataOrder);
                intent.putParcelableArrayListExtra("receipts", dataReceipts);
                startActivity(intent);
            }
        });

        btnsig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Set the original list of foods
                adapter.setContacts(foods);
                btnsig.setBackgroundColor(getResources().getColor(R.color.selected));
                btnapp.setBackgroundColor(getResources().getColor(R.color.base));
                btnorg.setBackgroundColor(getResources().getColor(R.color.base));
                btnstk.setBackgroundColor(getResources().getColor(R.color.base));
                btnspt.setBackgroundColor(getResources().getColor(R.color.base));
                txttop.setText("ALL MENU");
            }
        });

        btnapp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Data_Food> filteredFoods = new ArrayList<>();
                for (Data_Food food : foods) {
                    if (food.getFood_type().equals("1")) {
                        filteredFoods.add(food);
                    }
                }
                adapter.setContacts(filteredFoods);
                btnsig.setBackgroundColor(getResources().getColor(R.color.base));
                btnapp.setBackgroundColor(getResources().getColor(R.color.selected));
                btnorg.setBackgroundColor(getResources().getColor(R.color.base));
                btnstk.setBackgroundColor(getResources().getColor(R.color.base));
                btnspt.setBackgroundColor(getResources().getColor(R.color.base));
                txttop.setText("APPITIZER");
            }
        });

        btnorg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Data_Food> filteredFoods = new ArrayList<>();
                for (Data_Food food : foods) {
                    if (food.getFood_type().equals("2")) {
                        filteredFoods.add(food);
                    }
                }
                adapter.setContacts(filteredFoods);
                btnsig.setBackgroundColor(getResources().getColor(R.color.base));
                btnapp.setBackgroundColor(getResources().getColor(R.color.base));
                btnorg.setBackgroundColor(getResources().getColor(R.color.selected));
                btnstk.setBackgroundColor(getResources().getColor(R.color.base));
                btnspt.setBackgroundColor(getResources().getColor(R.color.base));
                txttop.setText("ORGANIC");
            }
        });

        btnstk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Data_Food> filteredFoods = new ArrayList<>();
                for (Data_Food food : foods) {
                    if (food.getFood_type().equals("3")) {
                        filteredFoods.add(food);
                    }
                }
                adapter.setContacts(filteredFoods);
                btnsig.setBackgroundColor(getResources().getColor(R.color.base));
                btnapp.setBackgroundColor(getResources().getColor(R.color.base));
                btnorg.setBackgroundColor(getResources().getColor(R.color.base));
                btnstk.setBackgroundColor(getResources().getColor(R.color.selected));
                btnspt.setBackgroundColor(getResources().getColor(R.color.base));
                txttop.setText("STEAK");
            }
        });

        btnspt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<Data_Food> filteredFoods = new ArrayList<>();
                for (Data_Food food : foods) {
                    if (food.getFood_type().equals("4")) {
                        filteredFoods.add(food);
                    }
                }
                adapter.setContacts(filteredFoods);
                btnsig.setBackgroundColor(getResources().getColor(R.color.base));
                btnapp.setBackgroundColor(getResources().getColor(R.color.base));
                btnorg.setBackgroundColor(getResources().getColor(R.color.base));
                btnstk.setBackgroundColor(getResources().getColor(R.color.base));
                btnspt.setBackgroundColor(getResources().getColor(R.color.selected));
                txttop.setText("SPAGETTI");
            }
        });
    }

    private void getData() throws MalformedURLException {
        Uri uri = Uri.parse("http://13.239.98.136:5000/ReadFood/all").buildUpon().build();
        URL url = new URL(uri.toString());
        new DOTask().execute(url);
    }

    class DOTask extends AsyncTask<URL,Void,String> {
        private static final String TAG = "DOTask";
        private static final String API_URL = "http://13.239.98.136:5000/ReadFood/all";

        @Override
        protected String doInBackground(URL... urls) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String result = null;

            try {
                URL url = new URL(API_URL);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                InputStream inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream == null) {
                    return null;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line).append("\n");
                }

                if (buffer.length() == 0) {
                    return null;
                }

                result = buffer.toString();
            } catch (IOException e) {
                Log.e(TAG, "Error accessing API", e);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (IOException e) {
                        Log.e(TAG, "Error closing reader", e);
                    }
                }
            }

            return result;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            if (result != null) {
                try {
                    JSONObject foodobj = new JSONObject(result);
                    parseJson(foodobj);
                    adapter.setContacts(foods);
                } catch (JSONException e) {
                    Log.e(TAG, "Error parsing JSON", e);
                }
            } else {
                Log.e(TAG, "Empty response");
            }
        }

        private void parseJson(JSONObject foodobj) throws JSONException {
            JSONArray foodArray = foodobj.getJSONArray("data");
            for (int i = 0; i < foodArray.length(); i++) {
                JSONObject food = foodArray.getJSONObject(i);
                String foodName = food.optString("FoodName");
                String foodPrice = food.optString("FoodPrice");
                String foodType = food.optString("FoodTypeID");
                String foodpic = food.optString("FoodPic");
                foods.add(new Data_Food(i, foodName, foodPrice, foodpic, foodType, true));
            }
        }
    }
}
