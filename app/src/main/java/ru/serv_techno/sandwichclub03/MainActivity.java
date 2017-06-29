package ru.serv_techno.sandwichclub03;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.view.MotionEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    List<Product> productList;
    List<Catalog> catalogList;
    RecyclerView rvCatalogs;
    RecyclerView rvProducts;
    ProductAdapter productAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, BasketActivity.class);
                startActivity(intent);
            }
        });

        final DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
            this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        final NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        if (navigationView!=null){
            navigationView.setNavigationItemSelectedListener(this);
        }

        //определим RecyclerView для каталогов и передадим в адаптер групп
        catalogList = new ArrayList<>();
        catalogList = Catalog.getCatalogsMain();
        boolean[] selected = new boolean[catalogList.size()];
        for(int i=0;i<catalogList.size();i++){
            selected[i]=false;
        }
        final CatalogAdapter catalogAdapter = new CatalogAdapter(this, catalogList, selected);
        rvCatalogs = (RecyclerView)findViewById(R.id.rwCatalogs);
        if(rvCatalogs!=null) {
            rvCatalogs.setAdapter(catalogAdapter);
            catalogAdapter.notifyDataSetChanged();
            rvCatalogs.addOnItemTouchListener(new RecyclerClickListener(this) {
                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                    return;
                }

                @Override
                public void onItemClick(RecyclerView rvCatalogs, View itemView, int position) {
                    Catalog cat = catalogAdapter.getItem(position);
                    Long catId = cat.getId();
                    //здесь переключим текущую группу
                    productList = new ArrayList<>();
                    productList.clear();
                    productList = Product.getProductsCatalog1(String.valueOf(catId));
                    MainActivity.this.productAdapter = new ProductAdapter(MainActivity.this, productList);
                    rvProducts = (RecyclerView)findViewById(R.id.rwProducts);
                    rvProducts.setItemAnimator(new DefaultItemAnimator());
                    if(rvProducts!=null){
                        rvProducts.setAdapter(productAdapter);
                        productAdapter.notifyDataSetChanged();
                        getSupportActionBar().setTitle(cat.name);
                        drawer.closeDrawer(navigationView);
                    }

                    for(int i=0;i<catalogAdapter.catalogList.size();i++){
                        if(i==position){
                            catalogAdapter.selected[i]=true;
                        }else{
                            catalogAdapter.selected[i]=false;
                        }
                    }
                    catalogAdapter.notifyDataSetChanged();
                }
            });
        }

        //определим RecyclerView для товаров, получим товары для главной страницы, проинициализируем адаптер
        productList = new ArrayList<>();
        productList.clear();
        productList = Product.getProductsMainView();
        MainActivity.this.productAdapter = new ProductAdapter(this, productList);
        rvProducts = (RecyclerView)findViewById(R.id.rwProducts);
        rvProducts.setItemAnimator(new DefaultItemAnimator());
        if(rvProducts!=null){
            rvProducts.setAdapter(productAdapter);
            productAdapter.notifyDataSetChanged();
            rvProducts.addOnItemTouchListener(new RecyclerClickListener(this) {
                @Override
                public void onItemClick(RecyclerView recyclerView, View itemView, int position) {
                    Product product = MainActivity.this.productAdapter.getItem(position);
                    if(product!=null){
                        switch (itemView.getId()){
                            case R.id.cwAddBasket:
                                //не работает
                                Toast.makeText(MainActivity.this, "Товар добавлен в корзину", Toast.LENGTH_SHORT).show();
                                break;
                            default:
                                Intent intent = new Intent(MainActivity.this, ProductActivity.class);
                                intent.putExtra("ProductID", product.getId());
                                startActivity(intent);
                                break;
                        }
                    }
                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {
                    return;
                }
            });
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id){
            case R.id.nav_call:
                Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:83012406806"));
                startActivity(intent);
                break;
            case R.id.nav_map:
                Intent intentMap = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intentMap);
                break;
            case R.id.nav_profile:
                Intent intentProfile = new Intent(MainActivity.this, UserProfileActivity.class);
                startActivity(intentProfile);
                break;
            case R.id.nav_basket:
                Intent intentBasket = new Intent(MainActivity.this, BasketActivity.class);
                startActivity(intentBasket);
                break;
            case R.id.nav_vk:
                Uri addressVK = Uri.parse("https://vk.com/snoopys_club");
                Intent openlinkIntent = new Intent(Intent.ACTION_VIEW, addressVK);
                startActivity(openlinkIntent);
                break;
            case R.id.nav_dev:
                Uri addressDev = Uri.parse("http://serv-techno.ru/");
                Intent devIntent = new Intent(Intent.ACTION_VIEW, addressDev);
                startActivity(devIntent);
                break;
            case R.id.nav_orders:
                Intent ordersIntent = new Intent(MainActivity.this, OrdersActivity.class);
                startActivity(ordersIntent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
//        int id = item.getItemId();
//
//        if (id == R.id.nav_map) {
//
//        } else if (id == R.id.nav_call) {
//
//        } else if (id == R.id.nav_profile) {
//
//        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
