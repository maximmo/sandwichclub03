package ru.serv_techno.sandwichclub03;

import com.orm.SugarRecord;

import java.util.List;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Product extends SugarRecord {
    //int ext_id;
    String name;
    String description;
    float price;
    String weight;
    int catalog_1;
    int catalog_2;
    int main_view;
    String text;
    int like;
    String code;
    int active;
    String imageLink;
    String bigImageLink;
    String weightText;

    public Product() {
    }

    public Product(String name, String description, float price, String weight,
                   int catalog1, int catalog2, int mainview, String text, int like, String code,
                   int active, String imagelink, String bigImageLink, String weightText) {
       // this.ext_id = ext_id;
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

    public static List<Product> getProductsMainView() {
        return Product.find(Product.class, "mainview = ?", "1");
    }

    public static List<Product> getProductsCatalog1(String catalog1) {
        return Product.find(Product.class, "catalog_1 = ?", catalog1);
    }

    public static List<Product> getProductsCatalog2(String catalog2) {
        return Product.find(Product.class, "catalog_1 = ?", catalog2);
    }

    public static Product getProductById(int productid) {
        return Product.findById(Product.class, productid);
    }
}
