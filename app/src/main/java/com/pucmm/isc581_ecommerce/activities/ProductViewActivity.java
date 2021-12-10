package com.pucmm.isc581_ecommerce.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.ProductsDB;
import com.pucmm.isc581_ecommerce.models.Product;

public class ProductViewActivity extends AppCompatActivity {

    private ImageView imageView;
    private String imageUrl;

    private TextView nameText;
    private TextView priceText;
    private TextView quantityText;
    private int quantity = 0;

    private FloatingActionButton minusOneBtn;
    private FloatingActionButton plusOneBtn;
    private ExtendedFloatingActionButton addCartBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_view);

//        Log.wtf("Log in main", "se cart+ " + MainActivity.cart.getProducts());


        imageView = findViewById(R.id.product_view_image);
        nameText = findViewById(R.id.product_view_name);
        priceText = findViewById(R.id.product_view_price);
        quantityText = findViewById(R.id.quantity_text_view);
        minusOneBtn = findViewById(R.id.quantity_minus_btn);
        plusOneBtn = findViewById(R.id.quantity_plus_btn);
        addCartBtn = findViewById(R.id.add_cart_button);

        Bundle extras = getIntent().getExtras();
        nameText.setText(extras.getString("ProductName"));
        priceText.setText(String.valueOf(extras.getFloat("ProductPrice")));
        Glide.with(this).load(extras.getString("ProductURL")).into(imageView);
        quantityText.setText(String.valueOf(quantity));

        minusOneBtn.setOnClickListener(v-> {
            if(quantity <= 0) {
                quantity = 0;
            }else {
                quantity -= 1;
            }
            quantityText.setText(String.valueOf(quantity));
        });

        plusOneBtn.setOnClickListener(v-> {
            quantity += 1;
            quantityText.setText(String.valueOf(quantity));
        });

        addCartBtn.setOnClickListener(v-> {
            Product product = ProductsDB.getProductById(extras.getString("ProductID"));
            for(int i = 0 ; i < quantity; i++ ){
                MainActivity.cart.pushProduct(product);
            }
            finish();
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}