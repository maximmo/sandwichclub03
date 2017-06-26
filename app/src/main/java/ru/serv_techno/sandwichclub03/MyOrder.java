package ru.serv_techno.sandwichclub03;

import android.util.Log;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.SugarRecord;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Maxim on 18.08.2016.
 */
public class MyOrder extends SugarRecord {

    public int extid;
    public float price;
    String paymenttype;
    int numberperson;
    String delivery;
    int ismobile;
    UserProfile userProfile;
    List<OrderProducts> orderProducts;
    public String status;
    public Date dateCreate;

    public MyOrder() {
    }

    public MyOrder(int extid, float price, String paymenttype, int numberperson, String delivery, UserProfile userProfile, List<OrderProducts> orderProducts, int ismobile, String status, Date dateCreate) {
        this.extid = extid;
        this.price = price;
        this.paymenttype = paymenttype;
        this.numberperson = numberperson;
        this.delivery = delivery;
        this.userProfile = userProfile;
        this.orderProducts = orderProducts;
        this.ismobile = ismobile;
        this.status = status;
        this.dateCreate = dateCreate;
    }

    public LinkedHashMap MakeRequestBodyOrder() {

        RequestBody rb;
        LinkedHashMap<String, RequestBody> mp = new LinkedHashMap<>();

        rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf((int) this.price));
        mp.put("price", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(this.ismobile));
        mp.put("isMobile", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(this.numberperson));
        mp.put("number_person", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.paymenttype);
        mp.put("payment_type", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.delivery);
        mp.put("delivery", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.userProfile.name);
        mp.put("client[name]", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.userProfile.phone);
        mp.put("client[phone]", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.userProfile.address);
        mp.put("client[address]", rb);

        for (int i = 0; i < this.orderProducts.size(); i++) {
            OrderProducts p = orderProducts.get(i);

            Product product = Product.getProductById(p.productid);
            if (product != null) {

                rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(product.getId()));
                mp.put("products[" + String.valueOf(i) + "][product_id]", rb);

                rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(p.amount));
                mp.put("products[" + String.valueOf(i) + "][amount]", rb);

            }
        }

        return mp;

    }

    public void setExtid(int _extid){
        this.extid = _extid;
        this.save();
    }

    public static List<MyOrder> getNewMyOrders() {
        return MyOrder.find(MyOrder.class, "extid = ?", "0");
    }

    public static List<MyOrder> getOrdersByStatus(String status) {
        return MyOrder.find(MyOrder.class, "status = ?", status);
    }

//    public static boolean UpdateNewOrdersStatus(){
//
//        final String LOG_TAG = "snoopy_st_log";
//        final Gson gson = new GsonBuilder().create();
//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("http://admin.serv-techno.ru/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        APIv1 myApi = retrofit.create(APIv1.class);
//
//        List<MyOrder> orderList = MyOrder.getNewStatusOrders();
//        for(final MyOrder order : orderList){
//            String filter = "" + "{'id':" + String.valueOf(order.extid) + "}";
//            myApi.getOrder(filter).enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    if(response!=null){
//                        ResponseBody responseBody = response.body();
//
//                        String MyMessage = null;
//                        String status;
//                        try {
//                            MyMessage = responseBody.string();
//                            JSONArray MyArray = gson.fromJson(MyMessage, JSONArray.class);
//
//
//
//                            //Map<String, String> orderMap = gson.fromJson(MyMessage, Map.class);
//
////                            for (Map.Entry e : orderMap.entrySet()) {
////                                if(e.getKey().equals("status")){
////                                    status = e.getValue().toString();
////                                    order.status = status;
////                                    order.save();
////                                }
////                            }
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            Log.e(LOG_TAG, "Ошибка при обновлении статуса заказа: " + e.getMessage());
//                            //res = false;
//                        }
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    t.printStackTrace();
//                    Log.e(LOG_TAG, String.valueOf(R.string.InternetErrorMessage));
//                }
//            });
//        }
//        return true;
//    }

}
