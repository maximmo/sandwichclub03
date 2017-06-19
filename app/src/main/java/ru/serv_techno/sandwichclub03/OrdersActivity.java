package ru.serv_techno.sandwichclub03;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import ru.serv_techno.sandwichclub03.fragments.OrderFragment;
import ru.serv_techno.sandwichclub03.fragments.OrderListFragment;

public class OrdersActivity extends FragmentActivity {

    private OrderListFragment orderListFragment;
    private OrderFragment orderFragment;
    private FragmentManager fragmentManager;
    private FragmentTransaction fragmentTransaction;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        orderListFragment = new OrderListFragment();
        orderFragment = new OrderFragment();
        fragmentManager = getSupportFragmentManager();

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.ordersContainer, orderListFragment);
        fragmentTransaction.commit();

    }
}
