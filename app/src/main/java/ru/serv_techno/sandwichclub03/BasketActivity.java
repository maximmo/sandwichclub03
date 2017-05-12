package ru.serv_techno.sandwichclub03;

import android.content.Intent;
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
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BasketActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    RecyclerView rwBasket;
    CardView BasketProfile;
    Switch switchPay;
    Switch switchDelivery;
    CheckBox BasketOffer;
    Button CreateOrder;
    TextView BasketProfileName;
    BasketAdapter basketAdapter;
    List<Basket> basketList;
    UserProfile userProfile;

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

        //заполним профиль
        BasketProfile = (CardView) findViewById(R.id.BasketProfile);
        BasketProfileName = (TextView)findViewById(R.id.BasketProfileName);
        ReloadProfileData();
        BasketProfile.setOnClickListener(this);

        CreateOrder = (Button)findViewById(R.id.CreateOrder);
        CreateOrder.setOnClickListener(this);
        BasketOffer = (CheckBox)findViewById(R.id.BasketOffer);

        //проставим switchPay и switchDelivery
        switchPay = (Switch)findViewById(R.id.switchPay);
        if(switchPay!=null){
            switchPay.setChecked(true);
            switchPay.setText("Наличные");
            switchPay.setOnCheckedChangeListener(this);
        }
        switchDelivery = (Switch)findViewById(R.id.switchDelivery);
        if(switchDelivery!=null){
            switchDelivery.setChecked(true);
            switchDelivery.setText("Доставка");
            switchDelivery.setOnCheckedChangeListener(this);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        RefreshBasketSumm();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.CreateOrder:
                break;
            case R.id.BasketProfile:
                Intent intent = new Intent(BasketActivity.this, UserProfileActivity.class);
                startActivityForResult(intent, 1);
                break;
            default:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        ReloadProfileData();
    }

    private void ReloadProfileData(){
        userProfile = UserProfile.getUser();
        if(userProfile==null){
            BasketProfileName.setText("Не заполнен профиль");
        }else{
            BasketProfileName.setText(userProfile.name);
        }
    }

    private void RefreshBasketSumm(){
        Float BasketSumm = Basket.getBasketSumm();
        getSupportActionBar().setTitle("Сумма заказа: " + String.valueOf(BasketSumm) + " \u20BD");
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

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int elementID = buttonView.getId();
        switch (elementID){
            case R.id.switchPay:
                if(isChecked==true){
                    buttonView.setText("Наличными");
                }else{
                    buttonView.setText("Картой");
                }
                break;
            case R.id.switchDelivery:
                if(isChecked==true){
                    buttonView.setText("Доставка");
                }else{
                    buttonView.setText("Самовывоз");
                }
                break;
        }
    }
}
