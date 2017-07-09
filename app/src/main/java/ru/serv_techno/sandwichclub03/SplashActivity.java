package ru.serv_techno.sandwichclub03;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.serv_techno.sandwichclub03.models.Catalog;
import ru.serv_techno.sandwichclub03.models.Product;

public class SplashActivity extends AppCompatActivity {

    private Retrofit retrofit;
    private static APIv1 myApi;
    private static final String LOG_TAG = "snoopy_st_log";

    List<Catalog> catalogList;
    List<Product> productList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getSupportActionBar().hide();

        retrofit = new Retrofit.Builder()
                .baseUrl("http://admin.snoopy03.ru")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        //создадим экземпляр класса работы с API
        myApi = retrofit.create(APIv1.class);

        //инициализируем списки каталогов и товаров
        catalogList = new ArrayList<>();
        productList = new ArrayList<>();

        //получим группы товаров
        myApi.getCatalogs(50).enqueue(new Callback<List<Catalog>>() {
            @Override
            public void onResponse(Call<List<Catalog>> call, Response<List<Catalog>> response) {
                if (response != null) {
                    catalogList.addAll(response.body());

                    //сделаем все каталоги неактивными
                    setUnactiveCatalogs();

                    for (int i = 0; i < catalogList.size(); i++) {
                        try {
                            Catalog cat = catalogList.get(i);
                            cat.save();

                            Log.d(LOG_TAG, "Записана группа: " + cat.name);
                        } catch (Exception e) {
                            e.printStackTrace();
                            Log.e(LOG_TAG, "Ошибка при записи группы: " + e.getMessage());
                        }
                    }
                } else {
                    Log.e(LOG_TAG, String.valueOf(R.string.InternetErrorMessage));
                    finish();
                }

            }

            @Override
            public void onFailure(Call<List<Catalog>> call, Throwable t) {
                Toast.makeText(SplashActivity.this, R.string.InternetErrorMessage, Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, String.valueOf(R.string.InternetErrorMessage));
                finish();
            }
        });

        //получим товары
        //пока временно накостыляем, потом обработать заголовки от сервера и прикрутить нормальную пагинацию
        for (int myIncrement = 1; myIncrement < 3; myIncrement++) {
            myApi.getProducts(100, myIncrement).enqueue(new Callback<List<Product>>() {
                @Override
                public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                    if (response != null) {
                        productList.addAll(response.body());

                        //сделаем все товары неактивными
                        setUnactiveProducts();

                        for (int i = 0; i < productList.size(); i++) {
                            try {
                                Product prod = productList.get(i);
                                prod.save();

                                Log.d(LOG_TAG, "Записано блюдо: " + prod.name);
                            } catch (Exception e) {
                                e.printStackTrace();
                                Log.e(LOG_TAG, "Ошибка при записи блюда: " + e.getMessage());
                            }
                        }
                    } else {
                        Toast.makeText(SplashActivity.this, R.string.InternetErrorMessage, Toast.LENGTH_SHORT).show();
                        Log.e(LOG_TAG, String.valueOf(R.string.InternetErrorMessage));
                        finish();
                    }
                }

                @Override
                public void onFailure(Call<List<Product>> call, Throwable t) {
                    Toast.makeText(SplashActivity.this, R.string.InternetErrorMessage, Toast.LENGTH_SHORT).show();
                    Log.e(LOG_TAG, String.valueOf(R.string.InternetErrorMessage));
                    finish();
                }
            });
        }

        //запустим MainActivity
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent myIntent = new Intent(SplashActivity.this, MainActivity.class);
                startActivity(myIntent);
                finish();
            }
        }, 3 * 1000);

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
