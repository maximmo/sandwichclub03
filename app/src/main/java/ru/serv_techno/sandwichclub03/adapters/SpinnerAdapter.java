package ru.serv_techno.sandwichclub03.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.serv_techno.sandwichclub03.R;
import ru.serv_techno.sandwichclub03.models.Address;

public class SpinnerAdapter extends BaseAdapter{
    Context ctx;
    public List<Address> addressList;
    LayoutInflater inflater;

    public SpinnerAdapter(Context ctx, List<Address> addressList){
        this.ctx = ctx;
        this.addressList = addressList;
        inflater = (LayoutInflater.from(ctx));
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Address getItem(int position) {
        return addressList.get(position);
    }

    public int getPositionByObject(Address address){
        return addressList.indexOf(address);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        convertView = inflater.inflate(R.layout.item_spinner, null);
        TextView ItemName = (TextView) convertView.findViewById(R.id.ItemName);
        TextView ItemDesc = (TextView) convertView.findViewById(R.id.ItemDesc);
        ItemName.setText(addressList.get(position).address);
        ItemDesc.setText(addressList.get(position).desc);

        return convertView;
    }
}
