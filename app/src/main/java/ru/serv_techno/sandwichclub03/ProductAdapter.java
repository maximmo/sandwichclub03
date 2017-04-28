package ru.serv_techno.sandwichclub03;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Maxim on 27.04.2017.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    Context ctx;
    List<Product> productList;

    //Конструктор адаптера
    ProductAdapter(Context ctx, List<Product> productList){
        this.ctx = ctx;
        this.productList = productList;
    }

    public static class ProductViewHolder extends RecyclerView.ViewHolder{
        CardView cardView;
        TextView productName;
        TextView productWeight;
        Button addButton;
        ImageView imageView;

        ProductViewHolder(View itemView){
            super(itemView);

            cardView = (CardView)itemView.findViewById(R.id.itemlist_card_view);
            productName = (TextView)itemView.findViewById(R.id.cwProductName);
            productWeight = (TextView)itemView.findViewById(R.id.cwProductWeight);
            addButton = (Button)itemView.findViewById(R.id.cwAddBasket);
            imageView = (ImageView)itemView.findViewById(R.id.cwProductImg);
        }
    }

    @Override
    public ProductViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_product, parent, false);
        ProductViewHolder pvh = new ProductViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ProductViewHolder holder, int position) {
        Product prod = productList.get(position);

        holder.productName.setText(prod.name);
        holder.productWeight.setText(prod.weightText);
        if (prod.imageLink!= null) {
            Picasso.with(ctx.getApplicationContext())
                    .load(prod.bigImageLink)
                    .placeholder(R.drawable.logo)
                    .error(R.drawable.logo)
                    .into(holder.imageView);
        }
        //String.valueOf(prod.price)+" \u20BD"
        int itemPrice = (int)prod.price;
        holder.addButton.setText(String.valueOf(itemPrice)+" \u20BD");
        holder.addButton.setTag(position);
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public Product getItem(int position){
        return productList.get(position);
    }
}

