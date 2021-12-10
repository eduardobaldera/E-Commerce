package com.pucmm.isc581_ecommerce.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.models.Product;

import java.util.ArrayList;

public class OrderProductsRVAdapter extends RecyclerView.Adapter<OrderProductsRVAdapter.MyRecyclerItemViewHolder> {

    private ArrayList<Product> products;
    private Context context;
    public OrderProductsRVAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
        //Log.wtf("PRODUCTS IN ADAPTER",products.toString());

    }

    @NonNull
    @Override
    public OrderProductsRVAdapter.MyRecyclerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.product_order_card_view, parent, false);

        OrderProductsRVAdapter.MyRecyclerItemViewHolder holder = new OrderProductsRVAdapter.MyRecyclerItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull OrderProductsRVAdapter.MyRecyclerItemViewHolder holder, int position) {

        Product product = products.get(position);
        holder.nameText.setText(product.getName());

        Glide.with(holder.itemView).load(product.getImageUrl()).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return products != null ? products.size() : 0;
    }

    public class MyRecyclerItemViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView nameText;
        public MyRecyclerItemViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.product_image_order);
            nameText = itemView.findViewById(R.id.product_name_order);
        }
    }
}
