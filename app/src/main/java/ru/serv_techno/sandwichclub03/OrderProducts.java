package ru.serv_techno.sandwichclub03;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Maxim on 18.08.2016.
 */
public class OrderProducts extends SugarRecord {
    int extid;
    public int productid;
    public int amount;

    public OrderProducts() {
    }

    public OrderProducts(int extid, int productid, int amount) {
        this.extid = extid;
        this.productid = productid;
        this.amount = amount;
    }

    public static List<OrderProducts> getOrderProductsNew() {
        return OrderProducts.find(OrderProducts.class, "extid = ?", "0");
    }

    public static List<OrderProducts> getOrderProductsByExtid(int extid) {
        return OrderProducts.find(OrderProducts.class, "extid = ?", String.valueOf(extid));
    }

    public void setExtid(int _extid){
        this.extid = _extid;
        this.save();
    }
}
