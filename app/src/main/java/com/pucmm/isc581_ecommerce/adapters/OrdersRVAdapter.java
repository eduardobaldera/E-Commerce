package com.pucmm.isc581_ecommerce.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.CartDB;
import com.pucmm.isc581_ecommerce.models.Cart;

import java.util.ArrayList;

public class OrdersRVAdapter extends RecyclerView.Adapter<OrdersRVAdapter.MyRecyclerItemViewHolder> {

    private ArrayList<Cart> orders;
    private Context context;

    public OrdersRVAdapter(ArrayList<Cart> orders, Context context) {
        this.orders = orders;
        this.context = context;

    }

    @NonNull
    @Override
    public OrdersRVAdapter.MyRecyclerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.order_card_view, parent, false);

        OrdersRVAdapter.MyRecyclerItemViewHolder holder = new OrdersRVAdapter.MyRecyclerItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersRVAdapter.MyRecyclerItemViewHolder holder, int position) {

        Cart cart = orders.get(position);
        Log.wtf("ORDERS IN ADAPTER", cart.toString() + "/" + cart.getTotal() + "\n" + cart.getProducts() + position);

        holder.priceText.setText(String.valueOf(cart.getTotal()));
        holder.adapter = new OrderProductsRVAdapter(CartDB.getOrders().get(position).getProducts(), context);
        holder.rv.setAdapter(holder.adapter);

        holder.constraintLayout.setOnClickListener(v-> {
            if(holder.rv.getVisibility() == View.VISIBLE) {
                holder.rv.setVisibility(View.GONE);
            }else {
                holder.rv.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public int getItemCount() {
        return orders.size();
    }

    public class MyRecyclerItemViewHolder extends RecyclerView.ViewHolder {
        TextView priceText;
        RecyclerView rv;
        OrderProductsRVAdapter adapter;
        ConstraintLayout constraintLayout;

        public MyRecyclerItemViewHolder(@NonNull View itemView) {
            super(itemView);

            priceText = itemView.findViewById(R.id.order_total_price);
            rv = itemView.findViewById(R.id.rv_order);
            constraintLayout = itemView.findViewById(R.id.order_constraintLayout);
        }
    }
}
