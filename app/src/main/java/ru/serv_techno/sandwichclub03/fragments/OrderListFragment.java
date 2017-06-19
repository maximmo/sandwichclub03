package ru.serv_techno.sandwichclub03.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.serv_techno.sandwichclub03.MyOrder;
import ru.serv_techno.sandwichclub03.R;
import ru.serv_techno.sandwichclub03.adapters.OrderListAdapter;

/**
 * Created by Maxim on 02.06.2017.
 */

public class OrderListFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.order_list_fragment, null);

        RecyclerView RecyclerViewOrderList  = (RecyclerView) rootview.findViewById(R.id.OrderListRecyclerView);
        RecyclerViewOrderList.setLayoutManager(new LinearLayoutManager(getActivity()));

        List<MyOrder> orderList = MyOrder.listAll(MyOrder.class);

        OrderListAdapter orderListAdapter = new OrderListAdapter(getActivity(), orderList);
        RecyclerViewOrderList.setAdapter(orderListAdapter);
        orderListAdapter.notifyDataSetChanged();
        RecyclerViewOrderList.setItemAnimator(new DefaultItemAnimator());

        return rootview;
    }


}
