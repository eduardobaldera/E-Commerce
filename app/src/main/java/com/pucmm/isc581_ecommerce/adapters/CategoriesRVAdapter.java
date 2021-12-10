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
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.activities.CategoryViewActivity;
import com.pucmm.isc581_ecommerce.activities.ManageCategoryActivity;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.CategoriesDB;
import com.pucmm.isc581_ecommerce.models.Category;

import java.util.ArrayList;

public class CategoriesRVAdapter extends RecyclerView.Adapter<CategoriesRVAdapter.MyRecyclerItemViewHolder> {

    private ArrayList<Category> categories;
    private Context context;

    public CategoriesRVAdapter(ArrayList<Category> categories, Context context) {
        this.categories = categories;
        this.context = context;

        Log.wtf("ADAPTER STMG", String.valueOf(categories));
    }

    @NonNull
    @Override
    public MyRecyclerItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).
                inflate(R.layout.categories_card_view, parent, false);

        MyRecyclerItemViewHolder holder = new MyRecyclerItemViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyRecyclerItemViewHolder holder, int position) {
        Category category = categories.get(position);

        //ASIGN IMAGE FROM RECIEVED URL FROM ARTICLE
        Glide.with(holder.itemView).load(category.getImageUrl()).into(holder.categoryImage);

        //ASSIGN TITLE FROM RECIEVED ARTICLE
        holder.categoryName.setText(category.getName());

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
                            Intent intent = new Intent(context, ManageCategoryActivity.class);
                            intent.putExtra("CategoryName", category.getName());
                            intent.putExtra("CategoryURL", category.getImageUrl());
                            intent.putExtra("CategoryID", category.getId());
                            context.startActivity(intent);
                            break;
                        case 1:
                            AlertDialog.Builder confirm = new AlertDialog.Builder(context);
                            confirm.setMessage("Delete Category?")
                                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            if(category.getProducts() == null) {
                                                CategoriesDB.deleteCategory(category.getId());
                                            }
                                            else if(category.getProducts().isEmpty()){
                                                CategoriesDB.deleteCategory(category.getId());
                                            }else {
                                                Toast.makeText(context, "Category cannot be deleted. It has products", Toast.LENGTH_SHORT).show();
                                            }
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
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(context, CategoryViewActivity.class);
                intent.putExtra("CategoryName", category.getName());
                intent.putExtra("CategoryURL", category.getImageUrl());
                intent.putExtra("CategoryID", category.getId());

                context.startActivity(intent);

            }
        });
    }


    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyRecyclerItemViewHolder extends RecyclerView.ViewHolder {

        //DECLARE FIELDS
        ImageView categoryImage;
        ImageView settingsImage;
        TextView categoryName;
        ConstraintLayout parentLayout;


        public MyRecyclerItemViewHolder(View itemView) {
            super(itemView);
            //ASSIGN ID'S
            categoryImage = (ImageView) itemView.findViewById(R.id.category_image);
            settingsImage = (ImageView) itemView.findViewById(R.id.category_settings);
            categoryName = (TextView) itemView.findViewById(R.id.category_name);
            parentLayout = (ConstraintLayout) itemView.findViewById(R.id.parent_layout);
        }
    }
}
