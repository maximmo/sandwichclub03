package ru.serv_techno.sandwichclub03;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import ru.serv_techno.sandwichclub03.models.Basket;
import ru.serv_techno.sandwichclub03.models.Product;

public class ProductActivity extends AppCompatActivity {

    public Product product;
    int countProduct = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        countProduct = 1;

        product = Product.getProductById((int) getIntent().getLongExtra("ProductID", 0));
        if(product==null){
            finish();
            return;
        }

        TextView productActivityText = (TextView)findViewById(R.id.productActivityText);
        productActivityText.setText(product.text);

        int itemPrice = (int)product.price;
        Button priceProduct = (Button)findViewById(R.id.priceProduct);
        priceProduct.setText(String.valueOf(itemPrice) +" \u20BD");
        priceProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countDialog();
            }
        });

        Button weightProduct = (Button)findViewById(R.id.weightProduct);
        weightProduct.setText(product.weightText);

        ImageView productActivityImage = (ImageView)findViewById(R.id.productActivityImage);

        if (product.imageLink!= null) {
            Picasso.with(this)
                    .load(product.bigImageLink)
                    .placeholder(R.drawable.product_default_bg)
                    .error(R.drawable.product_default_bg)
                    .into(productActivityImage);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarProduct);
        toolbar.setTitle(product.name);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        FloatingActionButton fabBasket = (FloatingActionButton) findViewById(R.id.fabProductBasket);
        fabBasket.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), BasketActivity.class);
                startActivity(intent);
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

    private void countDialog(){
        NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(30);
        numberPicker.setMinValue(1);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {
                countProduct = newVal;
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(this).setView(numberPicker);
        builder.setTitle(R.string.dialog_countProduct_title);
        builder.setCancelable(true);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() { // Кнопка ОК
            @Override
            public void onClick(DialogInterface dialog, int which) {
                boolean resultAdd = Basket.AddProduct(product, countProduct);
                if(resultAdd){
                    MySnackbar.ShowMySnackbar(findViewById(R.id.priceProduct), "Добавлен товар: " + product.name, R.color.SnackbarBg);
                }else{
                    MySnackbar.ShowMySnackbar(findViewById(R.id.priceProduct), "Не удалось добавить позицию: " + product.name, R.color.SnackbarBgRed);
                }
            }
        });
        builder.setNegativeButton(R.string.cancel_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        });
        AlertDialog dialog = builder.create();
        dialog.show();
    }
}
