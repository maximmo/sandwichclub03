package ru.serv_techno.sandwichclub03;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.serv_techno.sandwichclub03.models.Catalog;
import ru.serv_techno.sandwichclub03.models.Product;

public class SplashActivity extends AppCompatActivity {

    private static final String LOG_TAG = "snoopy_st_log";

    List<Catalog> catalogList;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        //инициализируем списки каталогов и товаров
        catalogList = new ArrayList<>();
        productList = new ArrayList<>();

        CatalogLoader catalogLoader = new CatalogLoader();
        catalogLoader.execute();
    }

    class CatalogLoader extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int TotalCountCatalog = 0;

            try {
                //отправим первый запрос для анализа заголовков
                Response<List<Catalog>> response = ApiFactory.getInstance().getApi().getCatalogs(50, 1).execute();
                if (response != null) {
                    //здесь проанализируем заголовки
                    String TotalCatalogPages = response.headers().get("X-Pagination-Page-Count");
                    int CatalogPages = Integer.parseInt(TotalCatalogPages);

                    for (int i = 1; i <= CatalogPages; i++) {
                        Response<List<Catalog>> responseIncrement = ApiFactory.getInstance().getApi().getCatalogs(50, i).execute();

                        if (responseIncrement != null) {
                            catalogList.addAll(responseIncrement.body());

                            //сделаем все каталоги неактивными
                            setUnactiveCatalogs();

                            for (int j = 0; j < catalogList.size(); j++) {
                                try {
                                    Catalog cat = catalogList.get(j);
                                    cat.save();

                                    TotalCountCatalog++;

                                    Log.d(LOG_TAG, "Записана группа: " + cat.name);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(LOG_TAG, "Ошибка при записи группы: " + e.getMessage());
                                }
                            }
                        } else {
                            Log.e(LOG_TAG, String.valueOf(R.string.InternetErrorMessage));
                            return null;
                        }
                    }
                    return TotalCountCatalog;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
            return null;
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            if (aVoid == null) {
                finish();
            }

            ProductLoader productLoader = new ProductLoader();
            productLoader.execute();
        }
    }

    class ProductLoader extends AsyncTask<Void, Void, Integer> {

        @Override
        protected Integer doInBackground(Void... params) {
            int TotalCountProduct = 0;

            try {
                Response<List<Product>> response = ApiFactory.getInstance().getApi().getProducts(50, 1).execute();

                if (response != null) {
                    String TotalProductPages = response.headers().get("X-Pagination-Page-Count");
                    int ProductPages = Integer.parseInt(TotalProductPages);

                    for (int myIncrement = 1; myIncrement <= ProductPages; myIncrement++) {
                        Response<List<Product>> responseIncrement = ApiFactory.getInstance().getApi().getProducts(50, myIncrement).execute();
                        if (responseIncrement != null) {
                            productList.addAll(responseIncrement.body());

                            //сделаем все товары неактивными
                            setUnactiveProducts();

                            for (int i = 0; i < productList.size(); i++) {
                                try {
                                    Product prod = productList.get(i);
                                    prod.save();

                                    TotalCountProduct++;
                                    Log.d(LOG_TAG, "Записано блюдо: " + prod.name);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Log.e(LOG_TAG, "Ошибка при записи блюда: " + e.getMessage());
                                }
                            }
                        }
                    }
                    return TotalCountProduct;
                } else {
                    Toast.makeText(SplashActivity.this, R.string.InternetErrorMessage, Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, String.valueOf(R.string.InternetErrorMessage));
                    return null;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }

        @Override
        protected void onPostExecute(Integer aVoid) {
            if (aVoid == null) {
                finish();
            }

            Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(myIntent);
            finish();
        }
    }

    private void setUnactiveCatalogs() {
        List<Catalog> allCatalogs = Catalog.listAll(Catalog.class);
        Catalog.setUnactiveCatalogs(allCatalogs);
    }

    private void setUnactiveProducts() {
        List<Product> allProducts = Product.listAll(Product.class);
        Product.setUnactiveProducts(allProducts);
    }

}
