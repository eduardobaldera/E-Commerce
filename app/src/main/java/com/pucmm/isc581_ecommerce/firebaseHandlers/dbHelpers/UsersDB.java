package com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers;

import android.os.Build;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pucmm.isc581_ecommerce.activities.MainActivity;
import com.pucmm.isc581_ecommerce.models.Category;
import com.pucmm.isc581_ecommerce.models.Product;
import com.pucmm.isc581_ecommerce.models.User;

import java.util.ArrayList;

public class UsersDB {
    private static final DatabaseReference myRef = MainActivity.database.getReference("users");
    private static User currentUser;
    private static ArrayList<User> users= new ArrayList<>();


    public static void createUser(String child, User user) {
        //Creates or updates category ?
        myRef.child(child).setValue(user);
    }

    public static void getUser() {
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        currentUser = null;
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                users.clear();
                for (DataSnapshot userSnapshot : snapshot.getChildren()) {
                    User value = userSnapshot.getValue(User.class);
                    users.add(value);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    public static User getUserByUid(String email) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            users.forEach(user -> {
                if(user.getEmail().equals(email)) {
                    currentUser = user;
                }
            });
        }

        return currentUser;
    }
}
