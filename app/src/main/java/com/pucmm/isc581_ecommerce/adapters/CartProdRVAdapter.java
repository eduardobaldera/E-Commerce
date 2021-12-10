package com.pucmm.isc581_ecommerce.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.activities.CartActivity;
import com.pucmm.isc581_ecommerce.activities.MainActivity;
import com.pucmm.isc581_ecommerce.activities.ManageProductActivity;
import com.pucmm.isc581_ecommerce.activities.ProductViewActivity;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.ProductsDB;
import com.pucmm.isc581_ecommerce.models.Product;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class CartProdRVAdapter extends RecyclerView.Adapter<CartProdRVAdapter.MyRecyclerItemViewHolder>{

    private ArrayList<Product> products;
    private Context context;
    private Integer quantity;

    public CartProdRVAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
    }




    @NonNull
    @Override
    public CartProdRVAdapter.MyRecyclerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.product_cart_card_view, parent, false);

        CartProdRVAdapter.MyRecyclerItemViewHolder holder = new CartProdRVAdapter.MyRecyclerItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartProdRVAdapter.MyRecyclerItemViewHolder holder, int position) {
        Product product = products.get(position);
        Log.wtf("Se prodct", product.getUuid() + "/" );
        quantity = MainActivity.cart.getQuantityFromProd(product);
        Log.wtf("QUANTIYU" , String.valueOf(quantity));
        Glide.with(holder.itemView).load(product.getImageUrl()).into(holder.productImage);

        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));
        holder.quantityText.setText(String.valueOf(quantity));

        holder.plusOneBtn.setOnClickListener(v->{
            MainActivity.cart.pushProduct(product);
//            CartActivity.notifyDatachanged();
            notifyDataSetChanged();
            CartActivity.notifyDatachanged();
        });

        holder.minusOneBtn.setOnClickListener(v->{
            if(quantity <= 1) {
                Toast.makeText(context, "Cannot remove any more of this product.", Toast.LENGTH_SHORT).show();
            }else {
                MainActivity.cart.removeProductQuantity(product);
                //CartActivity.notifyDatachanged();
                notifyDataSetChanged();
                CartActivity.notifyDatachanged();

            }
        });

        holder.deleteBtn.setOnClickListener(v-> {
            MainActivity.cart.deleteProduct(product);
            notifyDataSetChanged();
            products.remove(position);
            notifyItemRemoved(position);
            CartActivity.notifyDatachanged();


        });


    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyRecyclerItemViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        TextView productName;
        TextView productPrice;
        TextView quantityText;
        FloatingActionButton minusOneBtn;
        FloatingActionButton plusOneBtn;
        ExtendedFloatingActionButton deleteBtn;


        public MyRecyclerItemViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_cart_image);
            quantityText = itemView.findViewById(R.id.quantity_text_cart_view);
            productName = itemView.findViewById(R.id.product_cart_name);
            productPrice = itemView.findViewById(R.id.product_cart_price);
            minusOneBtn = itemView.findViewById(R.id.quantity_minus_cart_btn);
            plusOneBtn = itemView.findViewById(R.id.quantity_plus_cart_btn);
            deleteBtn = itemView.findViewById(R.id.delete_item_button);


        }
    }
}
