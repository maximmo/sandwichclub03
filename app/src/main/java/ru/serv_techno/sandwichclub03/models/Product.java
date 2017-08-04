package ru.serv_techno.sandwichclub03.models;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

import ru.serv_techno.sandwichclub03.R;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Product extends SugarRecord {
    public String name;
    String description;
    public float price;
    String weight;
    int catalog_1;
    int catalog_2;
    int main_view;
    public String text;
    public int like;
    String code;
    int active;
    public String imageLink;
    public String bigImageLink;
    public String weightText;

    public Product() {
    }

    public Product(String name, String description, float price, String weight,
                   int catalog1, int catalog2, int mainview, String text, int like, String code,
                   int active, String imagelink, String bigImageLink, String weightText) {

        this.name = name;
        this.description = description;
        this.price = price;
        this.weight = weight;
        this.catalog_1 = catalog1;
        this.catalog_2 = catalog2;
        this.main_view = mainview;
        this.text = text;
        this.like = like;
        this.code = code;
        this.active = active;
        this.imageLink = imagelink;
        this.bigImageLink = bigImageLink;
        this.weightText = weightText;
    }

    public int LikesPlus(int count){
        this.like = this.like + count;
        this.save();

        return this.like;
    }

    public static List<Product> getProductsMainView() {
        return Product.find(Product.class, "mainview = ? and active = ?", "1", "1");
    }

    public static List<Product> getProductsCatalog1(String catalog1) {
        return Product.find(Product.class, "catalog1 = ? and active = ?", catalog1, "1");
    }

    public static List<Product> getProductsCatalog2(String catalog2) {
        return Product.find(Product.class, "catalog1 = ?", catalog2);
    }

    public static Product getProductById(int productid) {
        return Product.findById(Product.class, productid);
    }

    public static void setUnactiveProducts(List<Product> productList) {

        if (productList.size() > 0) {
            for (int i = 0; i < productList.size(); i++) {
                Product product = productList.get(i);
                product.active = 0;
                try {
                    product.save();
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(String.valueOf(R.string.app_name), "Ошибка при записи товара: " + e.getMessage());
                }
            }
        }
    }
}
