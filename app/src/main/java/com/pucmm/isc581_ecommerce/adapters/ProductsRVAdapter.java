package com.pucmm.isc581_ecommerce.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.activities.ManageCategoryActivity;
import com.pucmm.isc581_ecommerce.activities.ManageProductActivity;
import com.pucmm.isc581_ecommerce.activities.ProductViewActivity;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.CategoriesDB;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.ProductsDB;
import com.pucmm.isc581_ecommerce.models.Category;
import com.pucmm.isc581_ecommerce.models.Product;

import java.util.ArrayList;

public class ProductsRVAdapter extends RecyclerView.Adapter<ProductsRVAdapter.MyRecyclerItemViewHolder>{

    private ArrayList<Product> products;
    private Context context;
    private boolean fromCat;

    public ProductsRVAdapter(ArrayList<Product> products, Context context) {
        this.products = products;
        this.context = context;
        this.fromCat = false;
    }

    public ProductsRVAdapter(ArrayList<Product> products, Context context, boolean fromCat) {
        this.products = products;
        this.context = context;
        this.fromCat = fromCat;
    }



    @NonNull
    @Override
    public ProductsRVAdapter.MyRecyclerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.product_card_view, parent, false);

        ProductsRVAdapter.MyRecyclerItemViewHolder holder = new ProductsRVAdapter.MyRecyclerItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductsRVAdapter.MyRecyclerItemViewHolder holder, int position) {
        Product product = products.get(position);

        //ASIGN IMAGE FROM RECIEVED URL FROM ARTICLE
        Glide.with(holder.itemView).load(product.getImageUrl()).into(holder.productImage);

        //ASSIGN TITLE FROM RECIEVED ARTICLE
        holder.productName.setText(product.getName());
        holder.productPrice.setText(String.valueOf(product.getPrice()));

        if(fromCat) {
            holder.settingsImage.setVisibility(View.GONE);
        }
        holder.settingsImage.setOnClickListener(v-> {
            // setup the alert builder

            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setTitle("Choose an option");
            String[] options = {"Update", "Delete"};
            builder.setItems(options, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0:
                            Intent intent = new Intent(context, ManageProductActivity.class);
                            intent.putExtra("ProductName", product.getName());
                            intent.putExtra("ProductPrice", product.getPrice());
                            intent.putExtra("ProductURL", product.getImageUrl());
                            intent.putExtra("ProductID", product.getUuid());
                            context.startActivity(intent);
                            break;
                        case 1:
                            AlertDialog.Builder confirm = new AlertDialog.Builder(context);
                            confirm.setMessage("Delete Product?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            ProductsDB.deleteProduct(product);
                                        }
                                    })
                                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                        }
                                    });
                            AlertDialog confirmer = confirm.create();
                            confirmer.show();
                            break;
                    }
                }
            });

// create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        });


        //SETS ON CLICK LISTENER ON CARDVIEW LAYOUT TO SEND TO ARTICLEREAD ACTIVITY
        if(!fromCat){
            holder.parentLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent = new Intent(context, ProductViewActivity.class);
                    intent.putExtra("ProductName", product.getName());
                    intent.putExtra("ProductPrice", product.getPrice());
                    intent.putExtra("ProductURL", product.getImageUrl());
                    intent.putExtra("ProductID", product.getUuid());
                    context.startActivity(intent);

                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    public class MyRecyclerItemViewHolder extends RecyclerView.ViewHolder {

        ImageView productImage;
        ImageView settingsImage;
        TextView productName;
        TextView productPrice;
        ConstraintLayout parentLayout;

        public MyRecyclerItemViewHolder(@NonNull View itemView) {
            super(itemView);

            productImage = itemView.findViewById(R.id.product_image);
            settingsImage = itemView.findViewById(R.id.product_settings);
            productName = itemView.findViewById(R.id.product_name);
            productPrice = itemView.findViewById(R.id.product_price);
            parentLayout = itemView.findViewById(R.id.parent_layout_product);

        }
    }
}
