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
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.serv_techno.sandwichclub03.models.Catalog;
import ru.serv_techno.sandwichclub03.models.Product;

public interface APIv1 {

    @Multipart
    @POST("api/v1/order/create")
    Call<ResponseBody> SendOrder(@PartMap Map<String, RequestBody> params);

    @Multipart
    @POST("api/v1/product/update/{id}")
    Call<ResponseBody> UpdateProduct(@PartMap Map<String, RequestBody> params, @Path("id") int id);

    @GET("api/v1/catalog/index")
    Call<List<Catalog>> getCatalogs(@Query("per-page") int count, @Query("page") int page);

    @GET("api/v1/product/index")
    Call<List<Product>> getProducts(@Query("per-page") int count, @Query("page") int page);

    @GET("api/v1/order/index")
    Call<ResponseBody> getOrder(@Query("filter") String filter);
}
