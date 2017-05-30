package ru.serv_techno.sandwichclub03;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
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

    int lastPosition = -1;

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
            Picasso.with(ctx)
                    .load(prod.bigImageLink)
                    .placeholder(R.drawable.empty_image)
                    .error(R.drawable.empty_image)
                    .into(holder.imageView);
        }

        int itemPrice = (int)prod.price;
        holder.addButton.setText(String.valueOf(itemPrice)+" \u20BD");
        holder.addButton.setTag(String.valueOf(prod.getId()));
        holder.addButton.setOnClickListener(btnItemPress);

        if(position >lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.product_rw_anim);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }else{
            Animation animation = AnimationUtils.loadAnimation(ctx, R.anim.product_rw_anim_up);
            holder.itemView.startAnimation(animation);
            lastPosition = position;
        }


    }

    View.OnClickListener btnItemPress = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            int productid = Integer.parseInt(v.getTag().toString());
            Product product = Product.getProductById(productid);
            if(product!=null){
                boolean result = Basket.AddProduct(product);
                if(result){
                    Snackbar snackbar = Snackbar.make(v, "Добавлен товар: " + product.name, Snackbar.LENGTH_SHORT);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundResource(R.color.SnackbarBg);
                    snackbar.show();
                }else{
                    Snackbar snackbar = Snackbar.make(v, "Не удалось добавить позицию: " + product.name, Snackbar.LENGTH_SHORT);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundResource(R.color.SnackbarBgRed);
                    snackbar.show();
                }
            }

        }
    };

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public Product getItem(int position){
        return productList.get(position);
    }
}

