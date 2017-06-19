package ru.serv_techno.sandwichclub03.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.serv_techno.sandwichclub03.R;

/**
 * Created by Maxim on 02.06.2017.
 */

public class OrderFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.order_fragment, null);
    }
}
