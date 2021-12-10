package com.pucmm.isc581_ecommerce.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.gson.Gson;
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.adapters.CartProdRVAdapter;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.CartDB;
import com.pucmm.isc581_ecommerce.models.Product;

import java.util.ArrayList;

public class CartActivity extends AppCompatActivity {

    private static TextView price;
    private ExtendedFloatingActionButton fab;
    private  RecyclerView rv;
    private  CartProdRVAdapter adapter;
    private  ArrayList<Product> products;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        price = findViewById(R.id.cart_total_price);
        fab = findViewById(R.id.complete_cart_btn);
        rv = findViewById(R.id.cart_rv);


        MainActivity.cart.calculateTotal();
        price.setText(String.valueOf(MainActivity.cart.getTotal()));
        products = MainActivity.cart.getProducts();
        Log.wtf("PLODUTS", products.toString());
        adapter = new CartProdRVAdapter(products, this);
        rv.setAdapter(adapter);

        fab.setOnClickListener(v -> {
            Gson gson = new Gson();
            Log.wtf("CART TO CREATE",gson.toJson(MainActivity.cart));
            CartDB.createOrder(MainActivity.cart);
            MainActivity.cart.cleanCart();
            finish();
        });

    }

    public static void notifyDatachanged() {
        MainActivity.cart.calculateTotal();
        price.setText(String.valueOf(MainActivity.cart.getTotal()));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
}