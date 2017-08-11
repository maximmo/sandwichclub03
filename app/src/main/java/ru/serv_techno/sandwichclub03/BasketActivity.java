package ru.serv_techno.sandwichclub03;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.serv_techno.sandwichclub03.adapters.BasketAdapter;
import ru.serv_techno.sandwichclub03.models.Basket;
import ru.serv_techno.sandwichclub03.models.MyOrder;
import ru.serv_techno.sandwichclub03.models.OrderProducts;
import ru.serv_techno.sandwichclub03.models.UserProfile;

public class BasketActivity extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener {

    RecyclerView rwBasket;
    CardView BasketProfile;
    Switch switchPay;
    Switch switchDelivery;
    CheckBox BasketOffer;
    Button CreateOrder;
    TextView BasketProfileName;
    public BasketAdapter basketAdapter;
    List<Basket> basketList;
    UserProfile userProfile;
    TextView banknote;
    TextView comment;

    private Gson gson = new GsonBuilder().create();
    private APIv1 intface = ApiFactory.getInstance().getApi();

    ProgressDialog OrderProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basket);

        rwBasket = (RecyclerView)findViewById(R.id.rwBasket);
        basketList = Basket.listAll(Basket.class);
        basketAdapter = new BasketAdapter(this, basketList);
        if(rwBasket!=null) {
            rwBasket.setAdapter(basketAdapter);
            basketAdapter.notifyDataSetChanged();
        }

        //заполним профиль
        BasketProfile = (CardView) findViewById(R.id.BasketProfile);
        BasketProfileName = (TextView)findViewById(R.id.BasketProfileName);
        BasketProfile.setOnClickListener(this);

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

        banknote = (TextView)findViewById(R.id.Banknote);
        comment = (TextView)findViewById(R.id.Comment);

        //установим доступность кнопки заказа
        CreateOrder = (Button)findViewById(R.id.CreateOrder);
        CreateOrder.setOnClickListener(this);
        BasketOffer = (CheckBox)findViewById(R.id.BasketOffer);
        BasketOffer.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SetCreateOrderAvailability();
            }
        });
        SetCreateOrderAvailability();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        RefreshBasketSumm();
        ReloadProfileData();

        if(basketList.size()==0){
            MySnackbar.ShowMySnackbar(rwBasket, "Добавьте что-нибудь в корзину=)", R.color.SnackbarBgRed);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.CreateOrder:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle(R.string.dialog_create_order_title);
                builder.setMessage(R.string.dialog_create_order_message);
                builder.setCancelable(true);
                builder.setIcon(R.mipmap.ic_launcher);
                builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        CreateOrder();
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
                if(isChecked){
                    buttonView.setText("Наличными");
                }else{
                    buttonView.setText("Картой");
                }
                break;
            case R.id.switchDelivery:
                if(isChecked){
                    buttonView.setText("Доставка");
                }else{
                    buttonView.setText("Самовывоз");
                }
                break;
        }
    }

    private void ReloadProfileData(){
        userProfile = UserProfile.getUser();
        if(userProfile==null){
            BasketProfileName.setText("Не заполнен профиль");
        }else{
            BasketProfileName.setText(userProfile.name);
        }
        SetCreateOrderAvailability();
    }

    public void RefreshBasketSumm(){
        Float BasketSumm = Basket.getBasketSumm();
        ActionBar ab = getSupportActionBar();
        if(ab!=null){
            String ActionBarTitle = "Сумма заказа: " + String.valueOf(BasketSumm) + " \u20BD";
            ab.setTitle(ActionBarTitle);
        }
    }

    private void SetCreateOrderAvailability(){
        Boolean UserUnset = userProfile==null;
        Boolean IsZero = basketList.size()==0;

        CreateOrder.setEnabled(BasketOffer.isChecked()&&!UserUnset&&!IsZero);
        if(CreateOrder.isEnabled()&&!UserUnset&&!IsZero){
            CreateOrder.setBackgroundColor(ContextCompat.getColor(BasketActivity.this, R.color.productBtnBgColor));
        }else{
            CreateOrder.setBackgroundColor(Color.GRAY);
        }
    }

    private void CreateOrder(){

        //здесь создадим заказ
        String paymentType = "cash";
        if (switchPay.isChecked()) {
            paymentType = "cash";
        } else {
            paymentType = "card";
        }

        String delivery = "yes";
        if(switchDelivery.isChecked()){
            delivery = "yes";
        }else{
            delivery = "no";
        }

        ArrayList<OrderProducts> NewOrderProducts = new ArrayList<>();

        //здесь нужно поместить данные из корзины в OrderProducts
        for (Basket basketItem:basketList){
            OrderProducts orderProduct = new OrderProducts(0, 0, basketItem.itembasket, basketItem.countProducts);
            try{
                orderProduct.save();
                NewOrderProducts.add(orderProduct);
            }
            catch (Exception e){
                e.printStackTrace();
                MySnackbar.ShowMySnackbar(CreateOrder, "Ошибка при создании заказа: " + e.toString(), R.color.SnackbarBgRed);
                return;
            }
        }

        List<OrderProducts> orderProducts = OrderProducts.getOrderProductsNew();

        MyOrder myOrder = new MyOrder(0, Basket.getBasketSumm(), paymentType, 1, delivery, userProfile, orderProducts, 1, "new", new Date(), banknote.getText().toString(), comment.getText().toString());
        try{
            myOrder.save();
            for(OrderProducts op : NewOrderProducts){
                op.setOrderid(myOrder.getId().intValue());
            }
        }catch (Exception e){
            e.printStackTrace();
            MySnackbar.ShowMySnackbar(CreateOrder, "Ошибка при сохранении заказа: " + e.toString(), R.color.SnackbarBgRed);
            return;
        }

        LinkedHashMap mp = myOrder.MakeRequestBodyOrder();

        if(mp!=null) {
            SendOrder so = new SendOrder();
            so.execute(mp);
        }
    }

    public void showMyNotification(String textMessage){

        int NOTIFY_ID = 101;

        Intent notificationIntent = new Intent(getApplicationContext(), OrdersActivity.class);
        PendingIntent contentIntent = PendingIntent.getActivity(this,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        Notification.Builder builder = new Notification.Builder(this);
        // оставим только самое необходимое
        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_my_notification)
                .setContentTitle("Sandwich Club 03")
                .setContentText(textMessage); // Текст уведомления

        Notification notification = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN) {
            notification = builder.build();
        }

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        notificationManager.notify(NOTIFY_ID, notification);
    }

    public void setDefaultStatus() throws InterruptedException {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.success_order_create_title);
        builder.setMessage(R.string.success_order_create_message);
        builder.setCancelable(true);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //onBackPressed();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    class SendOrder extends AsyncTask<LinkedHashMap, Void, Map> {

        public Map<String, Object> hmap = new HashMap<String, Object>();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            hmap.put("result", false);
            hmap.put("message", "Ошибка при отправке заказа!");
            hmap.put("extid", "0");

            OrderProgressDialog = new ProgressDialog(BasketActivity.this);
            OrderProgressDialog.setTitle("Отправка заказа");
            OrderProgressDialog.setMessage("Выполняется оформление заказа, пожалуйста подождите!");
            OrderProgressDialog.setIndeterminate(true);
            OrderProgressDialog.show();
        }

        @Override
        protected void onPostExecute(Map map) {
            super.onPostExecute(map);

            OrderProgressDialog.setIndeterminate(false);
            OrderProgressDialog.dismiss();

            if(map!=null){
                Object res = map.get("result");
                Object message = map.get("message");
                Object extid = map.get("extid");

                String StringRes = res.toString();

                boolean BoolRes = Boolean.parseBoolean(StringRes);

                if(BoolRes){
                    List<MyOrder> myOrders = MyOrder.getNewMyOrders();

                    MyOrder myOrderLocal = myOrders.get(0);

                    myOrderLocal.setExtid(Integer.valueOf(extid.toString()));

                    List<OrderProducts> orderProducts = OrderProducts.getOrderProductsNew();
                    for(OrderProducts op : orderProducts){
                        op.setExtid(Integer.valueOf(extid.toString()));
                    }
                    Basket.ClearBasket();

                    showMyNotification(message.toString());

                    try {
                        setDefaultStatus();

                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    MySnackbar.ShowMySnackbar(CreateOrder, "Ошибка: " + message.toString(), R.color.SnackbarBgRed);
                }
            }
            OrderProducts.ClearEmptyProducts();
            MyOrder.ClearEmptyOrders();
        }

        @Override
        protected Map doInBackground(LinkedHashMap... params) {

            String extid = "0";
            Call<ResponseBody> call = intface.SendOrder(params[0]);
            ResponseBody responseBody;
            ResponseBody errorBody;

            try {
                retrofit2.Response<ResponseBody> response = call.execute();
                if(response.code()==200||response.code()==201){

                    responseBody = response.body();

                    hmap.put("message", "Заказ принят! Наш менеджер свяжется с Вами! =)");
                    hmap.put("result", true);
                    hmap.put("extid", extid);

                    String MyMessage = responseBody.string();

                    Map<String, String> map = gson.fromJson(MyMessage, Map.class);

                    for (Map.Entry e : map.entrySet()) {
                        if(e.getKey().equals("id")){
                            extid = e.getValue().toString();
                            float fextid = Float.parseFloat(extid);
                            int IntExtId = (int)fextid;
                            extid = String.valueOf(IntExtId);

                            hmap.put("extid", extid);
                            hmap.put("message", "Номер заказа: " + extid);
                        }
                    }

                }
                else {
                    errorBody = response.errorBody();

                    hmap.put("result", false);
                    hmap.put("message", "Ошибка: ");
                    hmap.put("extid", extid);

                    String MyMessage = errorBody.string();

                    Map<String, String> map = gson.fromJson(MyMessage, Map.class);

                    for (Map.Entry e : map.entrySet()) {
                        if(e.getKey().equals("message")){
                            hmap.put("message", e.getValue().toString());
                        }
                    }
                }
                return hmap;

            } catch (IOException e) {
                e.printStackTrace();

                hmap.put("result", false);
                hmap.put("message", e.getMessage());
                hmap.put("extid", "0");

                return hmap;
            }
        }

        @Override
        protected void onCancelled() {
            super.onCancelled();

            MySnackbar.ShowMySnackbar(CreateOrder, "Отправка заказа прервана пользователем!", R.color.SnackbarBgRed);
            OrderProducts.ClearEmptyProducts();
            MyOrder.ClearEmptyOrders();
        }
    }
}
