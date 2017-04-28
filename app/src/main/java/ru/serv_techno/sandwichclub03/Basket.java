package ru.serv_techno.sandwichclub03;

import com.orm.SugarRecord;

/**
 * Created by Maxim on 27.04.2017.
 */

public class Basket extends SugarRecord {

    Product product;
    int countProducts;
    float price;
    float summ;


    public Basket() {
    }

    public Basket(Product product, int countProducts, float price, float summ) {

        this.product = product;
        this.countProducts = countProducts;
        this.price = price;
        this.summ = summ;

    }
}
