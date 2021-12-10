package com.pucmm.isc581_ecommerce.ui.home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.pucmm.isc581_ecommerce.R;
import com.pucmm.isc581_ecommerce.adapters.OrdersRVAdapter;
import com.pucmm.isc581_ecommerce.firebaseHandlers.dbHelpers.CartDB;
import com.pucmm.isc581_ecommerce.models.Cart;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private RecyclerView rv;
    private OrdersRVAdapter adapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_home, container, false);

        rv = root.findViewById(R.id.cart_orders_rv);

        ArrayList<Cart> orders = CartDB.getCarts();
        Log.wtf("ORDERS IN HF",orders.toString());
        adapter = new OrdersRVAdapter(orders,getContext());

        rv.setAdapter(adapter);
        return root;
    }
}