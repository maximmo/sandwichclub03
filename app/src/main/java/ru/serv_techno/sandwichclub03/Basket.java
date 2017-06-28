package ru.serv_techno.sandwichclub03;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Maxim on 27.04.2017.
 */

public class Basket extends SugarRecord {

    int itembasket;
    public int countProducts;
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

    public static boolean AddProduct(Product product, int countProducts) {

        String itemId = product.getId().toString();

        List<Basket> basketProductAll = Basket.listAll(Basket.class);
        if (basketProductAll.size() == 0) {
            return AddNewItem(product, itemId, countProducts);
        } else {
            List<Basket> basketProduct = Basket.find(Basket.class, "itembasket=?", itemId);
            if (basketProduct.size() == 0) {
                return AddNewItem(product, itemId, countProducts);
            } else {
                Basket basket = basketProduct.get(0);
                if (basket != null) {
                    return ItemPlus(product, basket, countProducts);
                }
            }
        }
        return false;
    }

    private static boolean AddNewItem(Product product, String itemId, int countProducts) {
        Basket basket = new Basket(Integer.parseInt(itemId), countProducts, product.price, product.price*countProducts);
        try {
            basket.save();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;

    }

    private static boolean ItemPlus(Product product, Basket basket, int countProducts) {
        basket.countProducts = basket.countProducts + countProducts;
        RefreshItemSumm(basket);
        try {
            basket.save();
        } catch (Exception e) {
            Log.e("Error", e.getMessage());
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private static void RefreshItemSumm(Basket basket) {
        basket.summ = basket.countProducts * basket.price;
    }

    public static boolean MinusProduct(Product product) {

        String itemId = product.getId().toString();

        List<Basket> basketProduct = Basket.find(Basket.class, "itembasket=?", itemId);
        if (basketProduct.size()==1) {
            Basket basket = basketProduct.get(0);
            if (basket.countProducts>1) {
                basket.countProducts--;
                RefreshItemSumm(basket);
                basket.save();
                return true;
            } else if (basket.countProducts == 1) {
                basket.delete();
                return true;
            }
        }

        return false;
}

    public static void ClearBasket() {
        Basket.deleteAll(Basket.class);
    }

    public static float getBasketSumm() {

        float summ = 0;

        List<Basket> basketList = Basket.listAll(Basket.class);

        for (int i = 0; i < basketList.size(); i++) {
            Product p = Product.getProductById(basketList.get(i).itembasket);

            if (p != null) {
                summ = summ + basketList.get(i).countProducts * p.price;
            }
        }

        return summ;
    }

}
