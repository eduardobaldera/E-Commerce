package com.pucmm.isc581_ecommerce.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.adapters.ProductsRVAdapter;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.CategoriesDB;
import com.pucmm.isc581_ecommerce.models.Product;

import java.util.ArrayList;

public class CategoryViewActivity extends AppCompatActivity {

    private ImageView imageView;
    private String imageUrl;

    private TextView nameText;
    private TextView noProd;

    private ArrayList<Product> products = new ArrayList<>();

    private RecyclerView rv;
    private ProductsRVAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_view);

        imageView = findViewById(R.id.image_view_category);
        nameText = findViewById(R.id.text_name_category);
        noProd = findViewById(R.id.no_products_cat);
        rv = findViewById(R.id.product_category_recycler_view);

        Bundle extras = getIntent().getExtras();
        imageUrl = extras.getString("CategoryURL");
        String name = extras.getString("CategoryName");
        String id = extras.getString("CategoryID");

        Glide.with(this).load(imageUrl).into(imageView);
        nameText.setText(name);

        products = CategoriesDB.getProductsFromCategory(id);

        if (products.isEmpty() || products == null) {
            noProd.setVisibility(View.VISIBLE);
        }else {
            adapter = new ProductsRVAdapter(products, this, true);
            rv.setAdapter(adapter);
        }



    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}