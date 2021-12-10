package com.pucmm.isc581_ecommerce.ui.product;

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
import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.activities.MainActivity;
import com.pucmm.isc581_ecommerce.activities.ManageProductActivity;
import com.pucmm.isc581_ecommerce.adapters.ProductsRVAdapter;
import com.pucmm.isc581_ecommerce.models.Product;

import java.util.ArrayList;

public class ProductFragment extends Fragment {

    private FloatingActionButton fab;
    private DatabaseReference myRef = MainActivity.database.getReference("products");
    private ArrayList<Product> products = new ArrayList<>();
    private RecyclerView rv;
    private ProductsRVAdapter adapter;
    private ProgressBar progressBar;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_product, container, false);

        fab = root.findViewById(R.id.product_add_btn);

        rv = (RecyclerView) root.findViewById(R.id.product_recycler_view);
        progressBar = (ProgressBar) root.findViewById(R.id.product_progressbar);

        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                products.clear();
                progressBar.setVisibility(View.VISIBLE);
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                for (DataSnapshot productSnapshot : dataSnapshot.getChildren()) {
                    //SAVES EACH CATEGORY TO THE ARRAY
                    Product value = productSnapshot.getValue(Product.class);
                    products.add(value);
                }

                adapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w("DB VALUES CATEGORIES", "Failed to read value.", error.toException());
            }
        });

        adapter = new ProductsRVAdapter(products, getContext());
        rv.setAdapter(adapter);


        fab.setOnClickListener(v-> {
            startActivity(new Intent(getActivity(), ManageProductActivity.class));
        });

        return root;
    }
}