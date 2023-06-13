package com.example.kinclean;


import android.content.Context;
import android.os.CountDownTimer;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class Recycle_orderhistory extends RecyclerView.Adapter<Recycle_orderhistory.ViewHolder> {

    private List<Data_Order> items;

    public Recycle_orderhistory(List<Data_Order> items) {
        this.items = items;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView idTextView;
        public TextView nameTextView;
        public TextView priceTextView;
        public TextView quantityTextView;
        public TextView status;
        public CountDownTimer countDownTimer; // Countdown timer instance

        private Button btnadd, btnremove;

        public ViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.txthisTextView);
            nameTextView = itemView.findViewById(R.id.txthisname);
            quantityTextView = itemView.findViewById(R.id.txthiscount);
            status = itemView.findViewById(R.id.txthisstatus);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View itemView = inflater.inflate(R.layout.ui_recycle_history, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data_Order dataOrder = items.get(position);

        TextView nameTextView = holder.nameTextView;
        TextView statusTextView = holder.status;
        TextView quantityTextView = holder.quantityTextView;

        nameTextView.setText("" + dataOrder.getName());
        quantityTextView.setText("จำนวน   " + dataOrder.getQuantity());

        // Get the price from the dataOrder object
        int price = Integer.parseInt(dataOrder.getPrice());

        statusTextView.setText(" ฿ " + price*dataOrder.getQuantity());
        // Set the countdown timer for 10 seconds

    }



    @Override
    public int getItemCount() {
        return items.size();
    }
}
