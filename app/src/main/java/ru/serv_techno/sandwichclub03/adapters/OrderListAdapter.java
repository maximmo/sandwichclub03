package ru.serv_techno.sandwichclub03.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.List;
import ru.serv_techno.sandwichclub03.MyOrder;
import ru.serv_techno.sandwichclub03.R;
import ru.serv_techno.sandwichclub03.fragments.OrderListFragment;

/**
 * Created by Maxim on 19.06.2017.
 */

public class OrderListAdapter extends RecyclerView.Adapter<OrderListAdapter.OrderListViewHolder>  {

    Context ctx;
    List<MyOrder> orderList;

    //Конструктор адаптера
    public OrderListAdapter(Context ctx, List<MyOrder> orderList){
        this.ctx = ctx;
        this.orderList = orderList;
    }

    public static class OrderListViewHolder extends RecyclerView.ViewHolder{

        ImageView OrderImage;
        TextView OrderItemIdTextView;
        TextView OrderItemId;
        TextView OrderItemSummTextView;
        TextView OrderItemSumm;
        TextView OrderItemStatus;
        TextView OrderItemDate;
        CardView OrderItemCard;

        OrderListViewHolder(View itemView){
            super(itemView);

            OrderImage = (ImageView)itemView.findViewById(R.id.OrderImage);
            OrderItemIdTextView = (TextView)itemView.findViewById(R.id.OrderItemIdTextView);
            OrderItemId = (TextView)itemView.findViewById(R.id.OrderItemId);
            OrderItemSummTextView = (TextView)itemView.findViewById(R.id.OrderItemSummTextView);
            OrderItemSumm = (TextView)itemView.findViewById(R.id.OrderItemSumm);
            OrderItemDate = (TextView)itemView.findViewById(R.id.OrderItemDate);
            OrderItemStatus = (TextView)itemView.findViewById(R.id.OrderItemStatus);
            OrderItemCard = (CardView)itemView.findViewById(R.id.OrderItemCard);
        }

    }

    @Override
    public OrderListAdapter.OrderListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item, parent, false);
        OrderListAdapter.OrderListViewHolder orderListViewHolder = new OrderListAdapter.OrderListViewHolder(v);
        return orderListViewHolder;
    }

    @Override
    public void onBindViewHolder(OrderListAdapter.OrderListViewHolder holder, int position) {
        MyOrder myOrder = orderList.get(position);

        holder.OrderItemId.setText(String.valueOf(myOrder.extid));
        holder.OrderItemSumm.setText(String.valueOf(myOrder.price) + " \u20BD");
        holder.OrderItemDate.setText(DateFormat.getDateInstance().format(myOrder.dateCreate));
        String orderStatus = myOrder.status;
        switch (orderStatus){
            case "new":
                //int NewOrderColor = R.color.NewOrderBg;
                holder.OrderItemCard.setCardBackgroundColor(ContextCompat.getColor(holder.OrderItemCard.getContext(), R.color.NewOrderBg));
                orderStatus = "Новый";
                Picasso.with(ctx)
                        .load(android.R.drawable.presence_away)
                        .placeholder(android.R.drawable.presence_invisible)
                        .into(holder.OrderImage);
                break;
            case "canceled":
                orderStatus = "Отменен";
                Picasso.with(ctx)
                        .load(android.R.drawable.presence_busy)
                        .placeholder(android.R.drawable.presence_invisible)
                        .into(holder.OrderImage);
                break;
            case "confirmed":
                orderStatus = "Подтвержден";
                Picasso.with(ctx)
                        .load(android.R.drawable.presence_online)
                        .placeholder(android.R.drawable.presence_invisible)
                        .into(holder.OrderImage);
                break;
        }
        holder.OrderItemStatus.setText(orderStatus);

    }

    public MyOrder getItem(int position){
        return orderList.get(position);
    }

    public List<MyOrder> getOrderList(){
        return orderList;
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

}
