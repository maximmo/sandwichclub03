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

    int id;
    float price;
    long createdat;
    int numberperson;
    int delivery;
    String clientname;
    String clientphone;
    String clientaddress;

    public MyOrder() {
    }

    public MyOrder(int id, float price, long createdat, int numberperson, int delivery, String clientname, String clientphone, String clientaddress) {
        this.id = id;
        this.price = price;
        this.createdat = createdat;
        this.numberperson = numberperson;
        this.delivery = delivery;
        this.clientname = clientname;
        this.clientphone = clientphone;
        this.clientaddress = clientaddress;
    }

    public LinkedHashMap MakeRequestBodyOrder() {

        RequestBody rb;
        LinkedHashMap<String, RequestBody> mp = new LinkedHashMap<>();

        rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf((int) OrderProducts.getBoxSumm()));
        mp.put("price", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), "1");
        mp.put("number_person", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), "cash");
        mp.put("payment_type", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), "yes");
        mp.put("delivery", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.clientname);
        mp.put("client[name]", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.clientphone);
        mp.put("client[phone]", rb);

        rb = RequestBody.create(MediaType.parse("text/plain"), this.clientaddress);
        mp.put("client[address]", rb);

        List<OrderProducts> orderProducts = OrderProducts.getOrderProductsNew();
        for (int i = 0; i < orderProducts.size(); i++) {
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


}
