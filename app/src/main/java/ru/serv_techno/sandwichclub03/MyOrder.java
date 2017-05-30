package ru.serv_techno.sandwichclub03;

import com.orm.SugarRecord;

import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

/**
 * Created by Maxim on 18.08.2016.
 */
public class MyOrder extends SugarRecord {

    int extid;
    float price;
    String paymenttype;
    int numberperson;
    String delivery;
    int ismobile;
    UserProfile userProfile;
    List<OrderProducts> orderProducts;

    public MyOrder() {
    }

    public MyOrder(int extid, float price, String paymenttype, int numberperson, String delivery, UserProfile userProfile, List<OrderProducts> orderProducts, int ismobile) {
        this.extid = extid;
        this.price = price;
        this.paymenttype = paymenttype;
        this.numberperson = numberperson;
        this.delivery = delivery;
        this.userProfile = userProfile;
        this.orderProducts = orderProducts;
        this.ismobile = ismobile;
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
}
