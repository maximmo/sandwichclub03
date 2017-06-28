package ru.serv_techno.sandwichclub03.fragments;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
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
import java.text.SimpleDateFormat;
import java.util.List;

import ru.serv_techno.sandwichclub03.Basket;
import ru.serv_techno.sandwichclub03.BasketActivity;
import ru.serv_techno.sandwichclub03.MyOrder;
import ru.serv_techno.sandwichclub03.OrderProducts;
import ru.serv_techno.sandwichclub03.OrdersActivity;
import ru.serv_techno.sandwichclub03.Product;
import ru.serv_techno.sandwichclub03.R;
import ru.serv_techno.sandwichclub03.adapters.OrderProductsAdapter;

/**
 * Created by Maxim on 02.06.2017.
 */

public class OrderFragment extends Fragment implements OrdersActivity.OnBackPressedListener {

    RecyclerView OrderProductList;
    ImageView OrderImage;
    TextView OrderItemId;
    TextView OrderItemSumm;
    TextView OrderItemStatus;
    TextView OrderItemDate;
    Button RepeatOrder;

    public MyOrder myOrder;
    public List<OrderProducts> orderProducts;

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

        RepeatOrder = (Button) rootview.findViewById(R.id.RepeatOrder);
        RepeatOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle(R.string.repeat_order_question_title);
                builder.setMessage(R.string.repeat_order_question);
                builder.setCancelable(true);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        MakeRepeatOrder();
                    }
                });
                builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        if(myOrder!=null){
            orderProducts = GetOrderProductList();
            InitAdapter(orderProducts);
            InitCardView();
        }

        return rootview;
    }

    @Override
    public void onBackPressed() {
        OrdersActivity activity = (OrdersActivity) getActivity();
        activity.actionBar.setTitle("История заказов");
        activity.getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }

    private List<OrderProducts> GetOrderProductList(){
        return OrderProducts.getOrderProductsByOrderid(myOrder.getId().intValue());
    }

    private void InitAdapter(List<OrderProducts> orderProductsList){
        OrderProductsAdapter orderProductsAdapter = new OrderProductsAdapter(getActivity(), orderProductsList);
        OrderProductList.setAdapter(orderProductsAdapter);
        orderProductsAdapter.notifyDataSetChanged();
    }

    private void InitCardView(){

        OrderItemId.setText(String.valueOf(myOrder.extid));
        OrderItemSumm.setText(String.valueOf(myOrder.price) + " \u20BD");

        String orderDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("d MMM yyyy H:mm");
        orderDate = dateFormat.format(myOrder.dateCreate);
        OrderItemDate.setText(orderDate);

        String orderStatus = myOrder.status;
        switch (orderStatus){
            case "new":
                orderStatus = "Новый";
                Picasso.with(getActivity())
                        .load(android.R.drawable.presence_away)
                        .placeholder(android.R.drawable.presence_invisible)
                        .into(OrderImage);
                SetBtnAvaliable(false);
                break;
            case "canceled":
                orderStatus = "Отменен";
                Picasso.with(getActivity())
                        .load(android.R.drawable.presence_busy)
                        .placeholder(android.R.drawable.presence_invisible)
                        .into(OrderImage);
                SetBtnAvaliable(false);
                break;
            case "confirmed":
                orderStatus = "Подтвержден";
                Picasso.with(getActivity())
                        .load(android.R.drawable.presence_online)
                        .placeholder(android.R.drawable.presence_invisible)
                        .into(OrderImage);
                SetBtnAvaliable(true);
                break;
        }
        OrderItemStatus.setText(orderStatus);

    }

    private void SetBtnAvaliable(boolean state){
        RepeatOrder.setEnabled(state);
        if(RepeatOrder.isEnabled()){
            RepeatOrder.setBackgroundColor(ContextCompat.getColor(getActivity(), R.color.productBtnBgColor));
        }else{
            RepeatOrder.setBackgroundColor(Color.GRAY);
        }
    }

    private void MakeRepeatOrder(){
        List<Basket> basketList = Basket.listAll(Basket.class);
        if (basketList.size()!=0) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setTitle("Ошибка");
            builder.setMessage("Корзина не пуста! Повтор заказа невозможен!");
            builder.setCancelable(true);
            builder.setIcon(R.mipmap.ic_launcher);
            builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    return;
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else{
            if(orderProducts!=null){
                for(OrderProducts orderProduct : orderProducts){
                    Product product = Product.findById(Product.class, orderProduct.productid);
                    if(product!=null){
                        Basket.AddProduct(product, orderProduct.amount);
                    }
                }
            }
            Intent intent = new Intent(getActivity(), BasketActivity.class);
            startActivity(intent);
        }
    }
}
