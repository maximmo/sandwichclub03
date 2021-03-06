package ru.serv_techno.sandwichclub03.models;

import android.util.Log;

import com.orm.SugarRecord;

import java.util.List;

import ru.serv_techno.sandwichclub03.R;

/**
 * Created by Maxim on 18.08.2016.
 */
public class Catalog extends SugarRecord {

    public String name;
    int parent_id;
    String code;
    int active;
    public int countProducts;

    public Catalog() {
    }

    public Catalog(String name, int parent_id, String code, int active, int countProducts) {
        this.active = active;
        this.code = code;
        this.name = name;
        this.parent_id = parent_id;
        this.countProducts = countProducts;
    }

    public static List<Catalog> getCatalogsMain() {
        return Catalog.find(Catalog.class, "parentid = ? and active = ?", "0", "1");
    }

    public static void setUnactiveCatalogs(List<Catalog> catalogList){
        if(catalogList.size()>0){
            for(int i=0;i<catalogList.size();i++){
                Catalog catalog = catalogList.get(i);
                catalog.active = 0;
                try{
                    catalog.save();
                }catch (Exception e){
                    e.printStackTrace();
                    Log.e(String.valueOf(R.string.app_name), "Ошибка при записи группы: " + e.getMessage());
                }
            }
        }
    }

}
