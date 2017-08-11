package ru.serv_techno.sandwichclub03.models;

import com.orm.SugarRecord;

import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.RequestBody;

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
    public String bank_note;
    public String comment;

    public MyOrder() {
    }

    public MyOrder(int extid, float price, String paymenttype, int numberperson, String delivery, UserProfile userProfile,
                   List<OrderProducts> orderProducts, int ismobile, String status, Date dateCreate, String bank_note, String comment) {
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
        this.bank_note = bank_note;
        this.comment = comment;
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

        rb = RequestBody.create(MediaType.parse("text/plain"), this.bank_note);
        mp.put("bank_note", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.userProfile.name);
        mp.put("client[name]", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.userProfile.phone);
        mp.put("client[phone]", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.userProfile.address.address);
        mp.put("client[address]", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.comment);
        mp.put("client[comment]", rb);

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

    public static void ClearEmptyOrders(){
        for(MyOrder order : getNewMyOrders()){
            order.delete();
        }
    }

}
