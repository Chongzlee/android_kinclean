package com.example.kinclean;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Recycle_menu extends RecyclerView.Adapter<Recycle_menu.ViewHolder> {

    private ArrayList<Data_Food> contacts = new ArrayList<>();
    private ArrayList<Data_Food> filteredContacts = new ArrayList<>();
    private ArrayList<Data_Order> selectedDataOrder = new ArrayList<>();
    private Context context;

    private int cost;
    private int cnt = 1;

    public Recycle_menu(Context context, ArrayList<Data_Order> selectedDataOrder) {
        this.context = context;
        this.selectedDataOrder = selectedDataOrder;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ui_recycle_menu, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Data_Food contact = filteredContacts.get(position);

        holder.txtname.setText(contact.getFood_name());
        holder.txtprice.setText("฿ " + contact.getFood_price());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = contact.getFood_id();
                String name = contact.getFood_name();
                String price = contact.getFood_price();

                boolean itemExists = false;

                for (Data_Order dataOrder : selectedDataOrder) {
                    if (dataOrder.getId() == id) {
                        int qty = dataOrder.getQuantity();
                        qty++;
                        dataOrder.setQuantity(qty);
                        itemExists = true;
                        break;
                    }
                }

                if (!itemExists) {
                    Data_Order dataOrder = new Data_Order(id, name, price, 1);
                    selectedDataOrder.add(dataOrder);
                }

                int cntprice = Integer.parseInt(price);
                cost += cntprice;
                String showCost = Integer.toString(cost);
                Page_menu menupage = Page_menu.getInstance();
                Button btncost = menupage.findViewById(R.id.btncost);
                btncost.setVisibility(View.VISIBLE);
                menupage.setBtncost(cnt + "      MY CART       " + "฿" + showCost);
                cnt++;

                Intent intent = new Intent(context, Page_order.class);
                intent.putParcelableArrayListExtra("selectedOrderList", selectedDataOrder);
                Intent intent2 = new Intent(context, Page_menu.class);
                intent2.putParcelableArrayListExtra("selectedOrderList", selectedDataOrder);
            }
        });

        Glide.with(context).asBitmap().load(contact.getFood_url()).into(holder.img);
    }

    @Override
    public int getItemCount() {
        return filteredContacts.size();
    }

    public void filterContacts() {
        filteredContacts.clear();
        filteredContacts.addAll(contacts);
        notifyDataSetChanged();
    }

    public ArrayList<Data_Food> getContacts() {
        return contacts;
    }

    public void setContacts(ArrayList<Data_Food> contacts) {
        this.contacts = contacts;
        filterContacts();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtname, txtprice;
        private ImageView img;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtname = itemView.findViewById(R.id.txtname);
            txtprice = itemView.findViewById(R.id.txtprice);
            img = itemView.findViewById(R.id.imgUrl);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
