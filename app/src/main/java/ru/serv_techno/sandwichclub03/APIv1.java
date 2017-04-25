package ru.serv_techno.sandwichclub03;

import java.util.List;
import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PartMap;
import retrofit2.http.Query;

/**
 * Created by Maxim on 24.09.2016.
 */
public interface APIv1 {

    @Multipart
    @POST("/api/v1/order/create")
    Call<ResponseBody> SendOrder(@PartMap Map<String, RequestBody> params);

    @GET("/api/vi/catalog/index")
    Call<List<Catalog>> getCatalogs(@Query("per-page") int count);

    @GET("/api/vi/product/index")
    Call<List<Product>> getProducts(@Query("per-page") int count);

}