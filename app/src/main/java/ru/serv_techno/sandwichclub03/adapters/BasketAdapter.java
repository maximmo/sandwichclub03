package ru.serv_techno.sandwichclub03.adapters;

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
import java.util.Locale;

import ru.serv_techno.sandwichclub03.models.Basket;
import ru.serv_techno.sandwichclub03.BasketActivity;
import ru.serv_techno.sandwichclub03.MySnackbar;
import ru.serv_techno.sandwichclub03.models.Product;
import ru.serv_techno.sandwichclub03.R;

/**
 * Created by Maxim on 27.04.2017.
 */

public class BasketAdapter extends RecyclerView.Adapter<BasketAdapter.BasketViewHolder> {

    Context ctx;
    List<Basket> basketList;

    int lastPosition = -1;

    //Конструктор адаптера
    public BasketAdapter(Context ctx, List<Basket> basketList) {
        this.ctx = ctx;
        this.basketList = basketList;
    }

    public static class BasketViewHolder extends RecyclerView.ViewHolder {
        CardView BasketItemCardView;
        ImageView cwBasketImg;
        TextView cwBasketProductName;
        TextView cwBasketProductPrice;
        Button PlusBtn;
        Button BasketCountItem;
        Button MinusBtn;

        BasketViewHolder(View itemView) {
            super(itemView);

            BasketItemCardView = (CardView) itemView.findViewById(R.id.BasketItemCardView);
            cwBasketProductName = (TextView) itemView.findViewById(R.id.cwBasketProductName);
            cwBasketProductPrice = (TextView) itemView.findViewById(R.id.cwBasketProductPrice);
            PlusBtn = (Button) itemView.findViewById(R.id.PlusBtn);
            BasketCountItem = (Button) itemView.findViewById(R.id.BasketCountItem);
            MinusBtn = (Button) itemView.findViewById(R.id.MinusBtn);
            cwBasketImg = (ImageView) itemView.findViewById(R.id.cwBasketImg);
        }
    }

    @Override
    public BasketViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_basket, parent, false);
        BasketViewHolder pvh = new BasketViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(BasketViewHolder holder, int position) {
        Basket basket = basketList.get(position);

        Product product = Product.getProductById(basket.itembasket);

        if (product != null) {
            holder.cwBasketProductName.setText(product.name);
            String orderPrice = String.format(Locale.getDefault(), "%.0f", product.price) + " \u20BD";
            holder.cwBasketProductPrice.setText(orderPrice);

            if (product.imageLink != null) {
                Picasso.with(ctx)
                        .load(product.bigImageLink)
                        .placeholder(R.drawable.product_default_bg)
                        .error(R.drawable.product_default_bg)
                        .into(holder.cwBasketImg);
            }

            int countProducts = basket.countProducts;
            holder.BasketCountItem.setText(String.valueOf(countProducts));
            holder.BasketCountItem.setTag(String.valueOf(basket.getId()));
            holder.PlusBtn.setOnClickListener(btnItemPress);
            holder.PlusBtn.setTag(String.valueOf(position));
            holder.MinusBtn.setOnClickListener(btnItemPress);
            holder.MinusBtn.setTag(String.valueOf(position));
        }
    }

    View.OnClickListener btnItemPress = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            int position = Integer.parseInt(v.getTag().toString());
            Basket basket = basketList.get(position);
            if(basket==null){
                return;
            }
            Product product = Product.getProductById(basket.itembasket);
            int countProductsLocal = basket.countProducts;

            if (product != null) {
                switch (v.getId()) {
                    case R.id.PlusBtn:
                        boolean result = Basket.AddProduct(product, 1);
                        if (result) {
                            basket.countProducts++;
                            MySnackbar.ShowMySnackbar(v, "Добавлен товар: " + product.name, R.color.SnackbarBg);
                        } else {
                            MySnackbar.ShowMySnackbar(v, "Не удалось добавить позицию: " + product.name, R.color.SnackbarBgRed);
                        }
                        break;
                    case R.id.MinusBtn:
                        boolean resultMinus = Basket.MinusProduct(product);
                        if (resultMinus) {
                            if(countProductsLocal==1){
                                basketList.remove(basket);
                            }else{
                                basket.countProducts--;
                            }

                            MySnackbar.ShowMySnackbar(v, "Товар удален из корзины: " + product.name, R.color.SnackbarBg);
                        } else {
                            MySnackbar.ShowMySnackbar(v, "Не удалось удалить позицию: " + product.name, R.color.SnackbarBgRed);
                        }
                        break;
                }
            }
            ((BasketActivity)ctx).basketAdapter.notifyDataSetChanged();
            ((BasketActivity)ctx).RefreshBasketSumm();
        }
    };

    @Override
    public int getItemCount() {
        return basketList.size();
    }

    public Basket getItem(int position) {
        return basketList.get(position);
    }
}

