package com.pucmm.isc581_ecommerce.ui.category;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.pucmm.isc581_ecommerce.activities.MainActivity;
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.activities.ManageCategoryActivity;
import com.pucmm.isc581_ecommerce.adapters.CategoriesRVAdapter;
import com.pucmm.isc581_ecommerce.models.Category;

import java.util.ArrayList;

public class CategoryFragment extends Fragment {

    private DatabaseReference myRef = MainActivity.database.getReference("categories");
    private ArrayList<Category> categories = new ArrayList<>();
    private RecyclerView rv;
    private CategoriesRVAdapter adapter;
    private ProgressBar progressBar;


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_category, container, false);
        FloatingActionButton fab = root.findViewById(R.id.category_add_gallery);


        rv = (RecyclerView) root.findViewById(R.id.categories_recycler_view);
        progressBar = (ProgressBar) root.findViewById(R.id.categories_progressbar);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                categories.clear();
                progressBar.setVisibility(View.VISIBLE);
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot categorySnapshot : dataSnapshot.getChildren()) {
                    //SAVES EACH CATEGORY TO THE ARRAY
                    Category value = categorySnapshot.getValue(Category.class);
                    categories.add(0,value);
                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DB VALUES CATEGORIES", "Failed to read value.", error.toException());
            }
        });

        adapter = new CategoriesRVAdapter(categories, getContext());
        rv.setAdapter(adapter);



        fab.setOnClickListener(v-> {
            startActivity(new Intent(getActivity(), ManageCategoryActivity.class));
        });
        return root;
    }
}