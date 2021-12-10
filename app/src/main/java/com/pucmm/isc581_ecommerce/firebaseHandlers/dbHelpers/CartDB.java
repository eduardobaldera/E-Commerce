package com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.pucmm.isc581_ecommerce.activities.MainActivity;
import com.pucmm.isc581_ecommerce.models.Cart;
import com.pucmm.isc581_ecommerce.models.Order;
import com.pucmm.isc581_ecommerce.models.Product;

import java.util.ArrayList;

public class CartDB {
    private static final DatabaseReference myRef = MainActivity.database.getReference("cart");
    private static ArrayList<Order> orders = new ArrayList<>();
    private static ArrayList<Cart> carts = new ArrayList<>();



    public static void createOrder(Cart cart) {
        //Creates or updates category ?
        String id = genKey();
        cart.setId(id);
        Gson gson = new Gson();
        Log.wtf("CART TO CREATE",gson.toJson(cart));
        myRef.child(id).setValue(cart);
    }

    public static String genKey() {
        return myRef.push().getKey();
    }


    public static ArrayList<Product> getProductsFromCart(String cartID) {
        ArrayList<Product> products = new ArrayList<>();
        myRef.child(cartID).child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Product product = orderSnapshot.getValue(Product.class);
                    Gson gson = new Gson();
                    Log.wtf("VALUES ADDING\n\n", "\n" + gson.toJson(product));
                    products.add(product);

                }

                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return products;
    }

    public static ArrayList<Order> getOrders() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                orders.clear();
                carts.clear();
                for (DataSnapshot orderSnapshot : snapshot.getChildren()) {
                    Order value = orderSnapshot.getValue(Order.class);
                    Cart cart = orderSnapshot.getValue(Cart.class);
                    Log.wtf("ADDING ORDERS ", String.valueOf(value));

                    orders.add(value);
                    carts.add(cart);
                    Gson gson = new Gson();
                    Log.wtf("VALUES:" , gson.toJson(value) + "\n" + gson.toJson(cart));
                    Log.wtf("VALUES SIZE :" ,  "\n\n" + orders.size() + carts.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Log.wtf("ORDERS ON THE WAY", orders.toString());
        return orders;
    }
    public static ArrayList<Cart> getCarts() {

        Log.wtf("ORDERS ON THE WAY", carts.toString());
        return carts;
    }

}
