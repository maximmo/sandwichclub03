package ru.serv_techno.sandwichclub03.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;

import ru.serv_techno.sandwichclub03.MyOrder;
import ru.serv_techno.sandwichclub03.OrderProducts;
import ru.serv_techno.sandwichclub03.R;

/**
 * Created by Maxim on 02.06.2017.
 */

public class OrderFragment extends Fragment {

    RecyclerView OrderProductList;
    ImageView OrderImage;
    TextView OrderItemId;
    TextView OrderItemSumm;
    TextView OrderItemStatus;
    TextView OrderItemDate;
    Button RepeatOrder;

    public MyOrder myOrder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootview = inflater.inflate(R.layout.order_fragment, null);

        OrderProductList = (RecyclerView) rootview.findViewById(R.id.OrderProductList);
        OrderProductList.setLayoutManager(new LinearLayoutManager(getActivity()));

        OrderImage = (ImageView) rootview.findViewById(R.id.OrderImage);
        OrderItemId = (TextView) rootview.findViewById(R.id.OrderItemId);
        OrderItemSumm = (TextView) rootview.findViewById(R.id.OrderItemSumm);
        OrderItemStatus = (TextView) rootview.findViewById(R.id.OrderItemStatus);
        OrderItemDate = (TextView) rootview.findViewById(R.id.OrderItemDate);

        if(myOrder!=null){
            List<OrderProducts> orderProductsList = GetOrderProductList();
            InitAdapter(orderProductsList);
            InitCardView();
        }


        return rootview;
    }

//    @Override
//    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
//        super.onViewCreated(view, savedInstanceState);
//
//        if(myOrder!=null){
//            List<OrderProducts> orderProductsList = GetOrderProductList();
//            //InitAdapter(orderProductsList);
//            InitCardView();
//        }
//    }

    private List<OrderProducts> GetOrderProductList(){
        return OrderProducts.getOrderProductsByExtid(myOrder.extid);
    }

    private void InitAdapter(List<OrderProducts> orderProductsList){

    }

    private void InitCardView(){

        OrderItemId.setText(String.valueOf(myOrder.extid));
        OrderItemSumm.setText(String.valueOf(myOrder.price) + " \u20BD");
        OrderItemDate.setText(DateFormat.getDateInstance().format(myOrder.dateCreate));
        String orderStatus = myOrder.status;
        switch (orderStatus){
            case "new":
                //OrderItemCard.setCardBackgroundColor(ContextCompat.getColor(OrderItemCard.getContext(), R.color.NewOrderBg));
                orderStatus = "Новый";
                Picasso.with(getActivity())
                        .load(android.R.drawable.presence_away)
                        .placeholder(android.R.drawable.presence_invisible)
                        .into(OrderImage);
                break;
            case "canceled":
                //holder.OrderItemCard.setCardBackgroundColor(ContextCompat.getColor(holder.OrderItemCard.getContext(), R.color.colorEndSpinner));
                orderStatus = "Отменен";
                Picasso.with(getActivity())
                        .load(android.R.drawable.presence_busy)
                        .placeholder(android.R.drawable.presence_invisible)
                        .into(OrderImage);
                break;
            case "confirmed":
                //holder.OrderItemCard.setCardBackgroundColor(ContextCompat.getColor(holder.OrderItemCard.getContext(), R.color.colorEndSpinner));
                orderStatus = "Подтвержден";
                Picasso.with(getActivity())
                        .load(android.R.drawable.presence_online)
                        .placeholder(android.R.drawable.presence_invisible)
                        .into(OrderImage);
                break;
        }
        OrderItemStatus.setText(orderStatus);

    }
}
