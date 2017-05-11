package ru.serv_techno.sandwichclub03;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Maxim on 27.04.2017.
 */

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>  {

    Context ctx;
    List<Catalog> catalogList;

    //Конструктор адаптера
    CatalogAdapter(Context ctx, List<Catalog> catalogList){
        this.ctx = ctx;
        this.catalogList = catalogList;
    }

    public static class CatalogViewHolder extends RecyclerView.ViewHolder{

        TextView rwItemCatalog;
        TextView rwItemCatalogCount;
        //View rwItemCatalogShape;

        CatalogViewHolder(View itemView){
            super(itemView);

            rwItemCatalog = (TextView)itemView.findViewById(R.id.rwItemCatalog);
            rwItemCatalogCount = (TextView)itemView.findViewById(R.id.rwItemCatalogCount);
            //rwItemCatalogShape = (View)itemView.findViewById(R.id.rwItemCatalogShape);
        }

    }

    @Override
    public CatalogViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_catalog, parent, false);
        CatalogViewHolder catalogViewHolder = new CatalogViewHolder(v);
        return catalogViewHolder;
    }

    @Override
    public void onBindViewHolder(CatalogViewHolder holder, int position) {
        Catalog catalog = catalogList.get(position);

        holder.rwItemCatalog.setText(catalog.name);
        holder.rwItemCatalogCount.setText(String.valueOf(catalog.countProducts));

    }

    public Catalog getItem(int position){
        return catalogList.get(position);
    }

    @Override
    public int getItemCount() {
        return catalogList.size();
    }

}
