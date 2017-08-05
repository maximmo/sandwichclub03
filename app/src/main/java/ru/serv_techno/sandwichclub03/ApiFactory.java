package ru.serv_techno.sandwichclub03;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//singleton
public class ApiFactory{

    private static ApiFactory instance = null;
    private APIv1 api = null;

    private ApiFactory(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://admin.serv-techno.ru/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        api = retrofit.create(APIv1.class);
    }

    public synchronized static ApiFactory getInstance(){
        if(instance == null){
            instance = new ApiFactory();
        }
        return instance;
    }

    public synchronized APIv1 getApi(){
        return api;
    }

}
