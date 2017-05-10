package ru.serv_techno.sandwichclub03;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Maxim on 27.04.2017.
 */

public class Basket extends SugarRecord {

    int itembasket;
    int countProducts;
    float price;
    float summ;


    public Basket() {
    }

    public Basket(int itembasket, int countProducts, float price, float summ) {

        this.itembasket = itembasket;
        this.countProducts = countProducts;
        this.price = price;
        this.summ = summ;

    }

    public static boolean AddProduct(Product product){

        String itemId = product.getId().toString();

        List<Basket> basketProductAll = Basket.listAll(Basket.class);
        if(basketProductAll.size()==0){
            return AddNewItem(product, itemId);
        }
        else{
            List<Basket> basketProduct = Basket.find(Basket.class, "itembasket=?", itemId);
            if(basketProduct.size()==0){
                return AddNewItem(product, itemId);
            }
            else{
                Basket basket = basketProduct.get(0);
                if(basket!=null){
                    return ItemPlus(product, basket);
                }
            }
        }
        return false;
    }

    private static boolean AddNewItem(Product product, String itemId){
        Basket basket = new Basket(Integer.parseInt(itemId), 1, product.price, product.price);
        try {
            basket.save();
        }
        catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;

    }

    private static boolean ItemPlus(Product product, Basket basket){
        basket.countProducts ++;
        basket.summ = basket.summ + product.price;
        try {
            basket.save();
        }
        catch (Exception e){
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static void ClearBasket(){
        Basket.deleteAll(Basket.class);
    }
}
