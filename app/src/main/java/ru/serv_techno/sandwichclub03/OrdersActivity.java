package ru.serv_techno.sandwichclub03;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import ru.serv_techno.sandwichclub03.fragments.OrderFragment;
import ru.serv_techno.sandwichclub03.fragments.OrderListFragment;
import ru.serv_techno.sandwichclub03.models.MyOrder;

public class OrdersActivity extends AppCompatActivity  implements OrderListFragment.OnOrderSelectedListener{

    private OrderListFragment orderListFragment;
    private OrderFragment orderFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;
    public ActionBar actionBar;

    public interface OnBackPressedListener {
        void onBackPressed();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        OnBackPressedListener backPressedListener = null;
        for (Fragment fragment: fm.getFragments()) {
            if (fragment instanceof  OnBackPressedListener) {
                backPressedListener = (OnBackPressedListener) fragment;
                break;
            }
        }

        if (backPressedListener != null) {
            backPressedListener.onBackPressed();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle("История заказов");
            actionBar.setHomeButtonEnabled(true);
        }

        orderListFragment = new OrderListFragment();
        orderFragment = new OrderFragment();
        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ordersContainer, orderListFragment);
        fragmentTransaction.commit();

    }

    @Override
    public void onOrderSelected(MyOrder myOrder) {
        InitOrderFragment(myOrder);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void InitOrderFragment(MyOrder myOrder){

        MyOrder order = MyOrder.findById(MyOrder.class, myOrder.getId());

        orderFragment = new OrderFragment();
        orderFragment.myOrder = order;
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.ordersContainer, orderFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
        if(actionBar!=null){
            actionBar.setTitle("Детали заказа");
        }
    }
}
