package ru.serv_techno.sandwichclub03.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

        TextView OrderItemStatusTextView;
        TextView OrderItemStatus;
        TextView OrderItemDateTextView;
        TextView OrderItemDate;
        TextView OrderItemSummTextView;
        TextView OrderItemSumm;

        OrderListViewHolder(View itemView){
            super(itemView);

            OrderItemStatusTextView = (TextView)itemView.findViewById(R.id.OrderItemStatusTextView);
            OrderItemStatus = (TextView)itemView.findViewById(R.id.OrderItemStatus);
            OrderItemDateTextView = (TextView)itemView.findViewById(R.id.OrderItemDateTextView);
            OrderItemDate = (TextView)itemView.findViewById(R.id.OrderItemDate);
            OrderItemSummTextView = (TextView)itemView.findViewById(R.id.OrderItemSummTextView);
            OrderItemSumm = (TextView)itemView.findViewById(R.id.OrderItemSumm);
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

        holder.OrderItemStatus.setText(myOrder.status);
        holder.OrderItemDate.setText(DateFormat.getDateInstance().format(myOrder.dateCreate));
        holder.OrderItemSumm.setText(String.valueOf(myOrder.price) + " \u20BD");
    }

    public MyOrder getItem(int position){
        return orderList.get(position);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

}
