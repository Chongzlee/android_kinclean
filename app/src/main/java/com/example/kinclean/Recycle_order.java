package com.example.kinclean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Recycle_order extends RecyclerView.Adapter<Recycle_order.ViewHolder> {
    private List<Data_Order> items;
    private Page_order orderPage;

    public Recycle_order(List<Data_Order> items, Page_order orderPage) {
        this.items = items;
        this.orderPage = orderPage;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView idTextView;
        public TextView nameTextView;
        public TextView priceTextView;
        public TextView quantityTextView;
        public Button btnadd;
        public Button btnremove;

        public ViewHolder(View itemView) {
            super(itemView);
            idTextView = itemView.findViewById(R.id.idTextView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            quantityTextView = itemView.findViewById(R.id.quantityTextView);
            btnadd = itemView.findViewById(R.id.btnadd);
            btnremove = itemView.findViewById(R.id.btnremove);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View itemView = inflater.inflate(R.layout.ui_recycle_order, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Data_Order dataOrder = items.get(position);
        TextView nameTextView = holder.nameTextView;
        TextView priceTextView = holder.priceTextView;
        TextView quantityTextView = holder.quantityTextView;

        holder.btnadd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Increase the quantity by 1
                dataOrder.setQuantity(dataOrder.getQuantity() + 1);
                notifyItemChanged(holder.getAdapterPosition());

                // Update the total price in Order_page
                orderPage.updateTotalPrice();
            }
        });

        holder.btnremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Decrease the quantity by 1
                int quantity = dataOrder.getQuantity();
                if (quantity > 1) {
                    dataOrder.setQuantity(quantity - 1);
                    notifyItemChanged(holder.getAdapterPosition());
                } else {
                    // Remove the item from the list
                    items.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                    notifyItemRangeChanged(holder.getAdapterPosition(), items.size());
                }

                // Update the total price in Order_page
                orderPage.updateTotalPrice();
            }
        });

        nameTextView.setText(String.valueOf(dataOrder.getName()));
        priceTextView.setText(" ฿ " + dataOrder.getPrice());
        quantityTextView.setText(String.valueOf(dataOrder.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
