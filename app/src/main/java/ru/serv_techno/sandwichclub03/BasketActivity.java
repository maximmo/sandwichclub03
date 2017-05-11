package ru.serv_techno.sandwichclub03;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BasketActivity extends AppCompatActivity implements View.OnClickListener  {

    RecyclerView rwBasket;
    CardView BasketProfile;
    Switch switchPay;
    Switch switchDelivery;
    CheckBox BasketOffer;
    Button CreateOrder;
    TextView BasketProfileName;
    BasketAdapter basketAdapter;
    List<Basket> basketList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        rwBasket = (RecyclerView)findViewById(R.id.rwBasket);
        //определим RecyclerView для товаров, получим товары для главной страницы, проинициализируем адаптер
        basketList = new ArrayList<>();
        basketList.clear();
        basketList = Basket.listAll(Basket.class);
        basketAdapter = new BasketAdapter(this, basketList);
        if(rwBasket!=null) {
            rwBasket.setAdapter(basketAdapter);
            basketAdapter.notifyDataSetChanged();
        }

        BasketProfile = (CardView) findViewById(R.id.BasketProfile);
        BasketProfileName = (TextView)findViewById(R.id.BasketProfileName);
        switchPay = (Switch)findViewById(R.id.switchPay);
        switchDelivery = (Switch)findViewById(R.id.switchDelivery);
        CreateOrder = (Button)findViewById(R.id.CreateOrder);
        CreateOrder.setOnClickListener(this);
        BasketOffer = (CheckBox)findViewById(R.id.BasketOffer);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.CreateOrder:
                break;
            default:
                break;
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
