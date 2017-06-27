package ru.serv_techno.sandwichclub03.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;

import ru.serv_techno.sandwichclub03.MyOrder;
import ru.serv_techno.sandwichclub03.OrderProducts;
import ru.serv_techno.sandwichclub03.Product;
import ru.serv_techno.sandwichclub03.R;

/**
 * Created by Maxim on 27.06.2017.
 */

public class OrderProductsAdapter extends RecyclerView.Adapter<OrderProductsAdapter.OrderProductsViewHolder> {

    Context ctx;
    List<OrderProducts> orderProductsList;

    public OrderProductsAdapter(Context ctx, List<OrderProducts> orderProductsList){
        this.ctx = ctx;
        this.orderProductsList = orderProductsList;
    }

    public static class OrderProductsViewHolder extends RecyclerView.ViewHolder{

        ImageView cwBasketImg;
        TextView cwBasketProductName;
        TextView cwBasketProductPrice;
        Button MinusBtn;
        Button BasketCountItem;
        Button PlusBtn;

        OrderProductsViewHolder(View view){
            super(view);

            cwBasketImg = (ImageView) view.findViewById(R.id.cwBasketImg);
            cwBasketProductName = (TextView) view.findViewById(R.id.cwBasketProductName);
            cwBasketProductPrice = (TextView) view.findViewById(R.id.cwBasketProductPrice);
            MinusBtn = (Button) view.findViewById(R.id.MinusBtn);
            BasketCountItem = (Button) view.findViewById(R.id.BasketCountItem);
            PlusBtn = (Button) view.findViewById(R.id.PlusBtn);
        }
    }

    @Override
    public OrderProductsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basket, parent, false);
        OrderProductsAdapter.OrderProductsViewHolder orderProductViewHolder = new OrderProductsAdapter.OrderProductsViewHolder(v);
        return orderProductViewHolder;
    }

    @Override
    public void onBindViewHolder(OrderProductsViewHolder holder, int position) {
        OrderProducts orderProduct = getItem(position);
        if (orderProduct != null) {
            Product product = Product.getProductById(orderProduct.productid);
            if (product != null) {
                if (product.imageLink != null) {
                    Picasso.with(ctx)
                            .load(product.imageLink)
                            .placeholder(R.drawable.product_default_bg)
                            .error(R.drawable.product_default_bg)
                            .into(holder.cwBasketImg);
                }

                int countProducts = orderProduct.amount;
                holder.cwBasketProductName.setText(product.name);
                holder.cwBasketProductPrice.setText(String.valueOf(product.price) + " \u20BD");
                holder.BasketCountItem.setText(String.valueOf(countProducts));
                holder.PlusBtn.setVisibility(View.GONE);
                holder.MinusBtn.setVisibility(View.GONE);
            }
        }
    }

    public OrderProducts getItem(int position){
        return orderProductsList.get(position);
    }

    @Override
    public int getItemCount() {
        return orderProductsList.size();
    }


}
