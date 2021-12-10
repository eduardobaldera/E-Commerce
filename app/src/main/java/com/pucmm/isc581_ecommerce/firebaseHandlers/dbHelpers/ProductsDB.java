package com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers;

import android.os.Build;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pucmm.isc581_ecommerce.activities.MainActivity;
import com.pucmm.isc581_ecommerce.models.Category;
import com.pucmm.isc581_ecommerce.models.Product;

import java.util.ArrayList;

public class ProductsDB {

    private static final DatabaseReference myRef = MainActivity.database.getReference("products");
    private static final ArrayList<Product> products = new ArrayList<>();
    private static Product selectedProd ;

    public static void manageProducts(String child, Product value) {
        //Creates or updates category ?
        myRef.child(child).setValue(value);
    }

    public static void deleteProduct(Product product) {
        CategoriesDB.removeProductFromCategory(product.getCategoryID(), product.getUuid());
        myRef.child(product.getUuid()).removeValue();
    }

    public static ArrayList<Product> getProducts() {

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                products.clear();
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    Product value = productSnapshot.getValue(Product.class);
                    products.add(value);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DB VALUES PRODUCTS", "Failed to read value.", error.toException());
            }
        });

        return products;
    }


    public static Product getProductById(String id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            products.forEach(product -> {
                if(product.getUuid().equals(id)){
                    selectedProd = product;
                }
            });
        }

        return selectedProd;
    }

    public static String generateID() {
        return myRef.push().getKey();
    }

}
