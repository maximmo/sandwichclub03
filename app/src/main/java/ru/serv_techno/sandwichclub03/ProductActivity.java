package ru.serv_techno.sandwichclub03;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ProductActivity extends AppCompatActivity {

    public Product product;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        product = Product.getProductById((int) getIntent().getLongExtra("ProductID", 0));
        if(product==null){
            finish();
        }

        TextView productActivityText = (TextView)findViewById(R.id.productActivityText);
        productActivityText.setText(product.text);

        int itemPrice = (int)product.price;
        Button priceProduct = (Button)findViewById(R.id.priceProduct);
        priceProduct.setText(String.valueOf(itemPrice) +" \u20BD");

        Button weightProduct = (Button)findViewById(R.id.weightProduct);
        weightProduct.setText(product.weightText);

        ImageView productActivityImage = (ImageView)findViewById(R.id.productActivityImage);

        if (product.imageLink!= null) {
            Picasso.with(this)
                    .load(product.bigImageLink)
                    .placeholder(R.drawable.burger)
                    .error(R.drawable.burger)
                    .into(productActivityImage);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProduct);
        toolbar.setTitle(product.name);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fabProduct);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean result = Basket.AddProduct(product);
                if(result){
                    Snackbar snackbar = Snackbar.make(view, "Добавлен товар: " + product.name, Snackbar.LENGTH_SHORT);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundResource(R.color.SnackbarBg);
                    snackbar.show();
                }else{
                    Snackbar snackbar = Snackbar.make(view, "Не удалось добавить позицию: " + product.name, Snackbar.LENGTH_SHORT);
                    View snackbarView = snackbar.getView();
                    snackbarView.setBackgroundResource(R.color.SnackbarBgRed);
                    snackbar.show();
                }

            }
        });


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
}
