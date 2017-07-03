package ru.serv_techno.sandwichclub03.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ru.serv_techno.sandwichclub03.models.Catalog;
import ru.serv_techno.sandwichclub03.R;

/**
 * Created by Maxim on 27.04.2017.
 */

public class CatalogAdapter extends RecyclerView.Adapter<CatalogAdapter.CatalogViewHolder>  {

    Context ctx;
    public List<Catalog> catalogList;
    public boolean[] selected;

    //Конструктор адаптера
    public CatalogAdapter(Context ctx, List<Catalog> catalogList, boolean[] selected){
        this.ctx = ctx;
        this.catalogList = catalogList;
        this.selected = selected;
    }

    public static class CatalogViewHolder extends RecyclerView.ViewHolder{

        TextView rwItemCatalog;
        TextView rwItemCatalogCount;

        CatalogViewHolder(View itemView){
            super(itemView);

            rwItemCatalog = (TextView)itemView.findViewById(R.id.rwItemCatalog);
            rwItemCatalogCount = (TextView)itemView.findViewById(R.id.rwItemCatalogCount);
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

        if(selected[position])
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.SnackbarBgRed));
        else
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.colorPrimary));
    }

    public Catalog getItem(int position){
        return catalogList.get(position);
    }

    @Override
    public int getItemCount() {
        return catalogList.size();
    }

}
