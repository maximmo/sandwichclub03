package ru.serv_techno.sandwichclub03.models;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Maxim on 18.08.2016.
 */
public class OrderProducts extends SugarRecord {
    int extid;
    int orderid;
    public int productid;
    public int amount;

    public OrderProducts() {
    }

    public OrderProducts(int extid, int orderid, int productid, int amount) {
        this.extid = extid;
        this.orderid = orderid;
        this.productid = productid;
        this.amount = amount;
    }

    public static List<OrderProducts> getOrderProductsNew() {
        return OrderProducts.find(OrderProducts.class, "extid = ?", "0");
    }

    public static List<OrderProducts> getOrderProductsByExtid(int extid) {
        return OrderProducts.find(OrderProducts.class, "extid = ?", String.valueOf(extid));
    }

    public static List<OrderProducts> getOrderProductsByOrderid(int orderid) {
        return OrderProducts.find(OrderProducts.class, "orderid = ?", String.valueOf(orderid));
    }

    public void setExtid(int extid){
        this.extid = extid;
        this.save();
    }

    public void setOrderid(int orderid){
        this.orderid = orderid;
        this.save();
    }

    public static void ClearEmptyProducts(){
        for(OrderProducts op: getOrderProductsByExtid(0)){
            op.delete();
        }
    }
}
