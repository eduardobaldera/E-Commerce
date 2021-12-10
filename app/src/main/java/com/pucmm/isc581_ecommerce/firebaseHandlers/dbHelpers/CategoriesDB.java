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

public class CategoriesDB {
    private static final DatabaseReference myRef = MainActivity.database.getReference("categories");
    private static boolean ran;
    private static ArrayList<Category> categories = new ArrayList<>();
    private static Category selectedCat;



    public static void manageCategory(String child, Category value) {
        //Creates or updates category ?
        myRef.child(child).setValue(value);
    }

    public static void updateProducts(String child, ArrayList<Product> products) {
        Log.wtf("UPDATING PROD", child + products);
        myRef.child(child).child("products").setValue(products);
    }



    public static void deleteCategory(String child) {
        myRef.child(child).removeValue();
    }



    public static String generateID() {
        return myRef.push().getKey();
    }


    public static void removeProductFromCategory(String child, String productID) {

        ArrayList<Product> products = new ArrayList<>();
        ran = true;

        myRef.child(child).child("products").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue() != null && ran) {

                    for (DataSnapshot productSnapshot : snapshot.getChildren()) {
                        Product value = productSnapshot.getValue(Product.class);
                        products.add(value);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        products.removeIf(product1 -> (product1.getUuid().equals(productID)));
                    }

                    myRef.child(child).child("products").setValue(products);
                    ran = false;
                    return;
                } else {
                    Log.e("TAG", " it's null.");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static ArrayList<Category> getCategories() {
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.wtf("LOG INSIDE CAT", "CATEGORIES UPDATING");
                categories.clear();
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    //SAVES EACH CATEGORY TO THE ARRAY
                    Category value = categorySnapshot.getValue(Category.class);
                    categories.add(value);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DB VALUES CATEGORIES", "Failed to read value.", error.toException());
            }
        });

        return categories;
    }

    public static ArrayList<Product> getProductsFromCategory(String id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            categories.forEach(category -> {
                if(category.getId().equals(id)){
                    selectedCat = category;
                }
            });
        }
        if(selectedCat.getProducts() == null) {
            return new ArrayList<>();
        }
        else {
            return selectedCat.getProducts();
        }
    }
}
