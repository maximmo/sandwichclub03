package ru.serv_techno.sandwichclub03;

import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by Maxim on 13.05.2017.
 */

public class MySnackbar {

    public static void ShowMySnackbar(View v, String msg, int color){
        Snackbar snackbar = Snackbar.make(v, msg, Snackbar.LENGTH_SHORT);
        View snackbarView = snackbar.getView();
        snackbarView.setBackgroundResource(color);
        snackbar.show();
    }
}

