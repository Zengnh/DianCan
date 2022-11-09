package com.king.android.api;

import com.king.android.model.Address;
import com.king.android.model.Category;
import com.king.android.model.Charge;
import com.king.android.model.City;
import com.king.android.model.Floor;
import com.king.android.model.MoneyLog;
import com.king.android.model.MoneyTota;
import com.king.android.model.Order;
import com.king.android.model.PayData;
import com.king.android.model.ProductInfo;
import com.king.android.model.Score;
import com.king.android.model.ShopInfo;
import com.king.android.model.Shops;
import com.king.android.model.Upload;
import com.king.android.model.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public
interface ApiService {

    @POST("/api/user/login")
    @FormUrlEncoded
    Observable<Result<User>> login(@FieldMap Map<String,Object> map);

    @POST("/api/index/category")
    Observable<Result<List<Category>>> category();

    @GET("/api/index/shops")
    Observable<Result<List<Shops>>> getShops(@Query("lat") String lat, @Query("lng") String lng, @Query("category_id") String category_id);

    @GET("/api/shop/info")
    Observable<Result<ShopInfo>> getShopInfo(@Query("shop_id") String shop_id, @Query("lat") String lat, @Query("lng") String lng);

    @GET("/api/user/myOrderList")
    Observable<Result<List<Order>>> myOrderList(@Query("access_token") String access_token);

    @GET("/api/user/getAddressList")
    Observable<Result<List<Address>>> getAddressList(@Query("access_token") String access_token);

    @GET("/api/user/commentOrder")
    Observable<Result<Charge>> commentOrder(@Query("access_token") String access_token, @Query("order_id")  String order_id, @Query("user_comment")  String user_comment, @Query("star_num")  String star_num);

    @GET("/api/user/charge")
    Observable<Result<Charge>> charge(@Query("access_token") String access_token, @Query("money")  String money);

    @GET("/api/user/cancelOrder")
    Observable<Result<PayData>> cancelOrder(@Query("access_token") String access_token, @Query("order_id")  String order_id);

    @GET("/api/user/payOrder")
    Observable<Result<PayData>> payOrder(@Query("access_token") String access_token, @Query("order_id")  long order_id);

    @GET("/api/user/payCharge")
    Observable<Result<PayData>> payCharge(@Query("access_token") String access_token, @Query("order_no")  String order_no);

    @GET("/api/user/editCartNum")
    Observable<Result> editCartNum(@Query("access_token") String access_token,@Query("id")  String id,@Query("num")  String num);

    @GET("/api/user/delCart")
    Observable<Result> delCart(@Query("access_token") String access_token,@Query("id")  String id);

    @GET("/api/user/delAddress")
    Observable<Result> delAddress(@Query("access_token") String access_token,@Query("address_id")  String address_id);

    @GET("/api/user/transfer")
    Observable<Result> transfer(@Query("access_token") String access_token,@Query("mobile")  String mobile,@Query("score")  String score);

    @POST("/api/user/getShopList")
    @FormUrlEncoded
    Observable<Result> getShopList(@FieldMap Map<String,Object> map);

    @POST("/api/user/sendCode")
    @FormUrlEncoded
    Observable<Result> sendCode(@Field("mobile") String mobile);

    @POST("/api/user/register")
    @FormUrlEncoded
    Observable<Result> register(@FieldMap() Map<String,Object> map);

    @POST("/api/user/retrieveMember")
    @FormUrlEncoded
    Observable<Result> retrieveMember(@FieldMap() Map<String,Object> map);

    @POST("/api/user/addAddress")
    @FormUrlEncoded
    Observable<Result> addAddress(@FieldMap() Map<String,Object> map);

    @POST("/api/user/editAddress")
    @FormUrlEncoded
    Observable<Result> editAddress(@FieldMap() Map<String,Object> map);

    @POST("/api/user/addCart")
    @FormUrlEncoded
    Observable<Result> addCart(@FieldMap() Map<String,Object> map);

    @POST("/api/user/addOrder")
    @FormUrlEncoded
    Observable<Result<Charge>> addOrder(@FieldMap() Map<String,Object> map);

    @POST("/api/user/updateMemberInfo")
    @FormUrlEncoded
    Observable<Result> updateMemberInfo(@FieldMap Map<String,Object> map);

    @POST("/api/user/addCartOrder")
    @FormUrlEncoded
    Observable<Result<Charge>> addCartOrder(@FieldMap Map<String,Object> map);

    @GET("/api/user/myCart")
    Observable<Result> myCart(@Query("access_token") String access_token);

    @GET("api/user/moneyTotal")
    Observable<Result<MoneyTota>> moneyTota(@Query("access_token") String access_token);

    @GET("api/user/moneyTotal")
    Observable<Result<MoneyTota>> queryMessage(@Query("access_token") String access_token);

    @GET("/api/user/getCityAll")
    Observable<Result<List<City>>> getCityAll();

    @GET("/api/user/moneyLog")
    Observable<Result<MoneyLog>> moneyLog(@Query("access_token") String access_token, @Query("page") int page, @Query("size") int size);

    @GET("/api/user/scoreLog")
    Observable<Result<Score>> scoreLog(@Query("access_token") String access_token, @Query("page") int page, @Query("size") int size);

    @POST("/api/index/uploadImg")
    Observable<Result<Upload>> upload(@Body RequestBody body);

    @GET("/api/shop/productInfo")
    Observable<Result<ProductInfo>> getProductInfo(@Query("product_id") String product_id);

    @GET("/api/shop/seat")
    Observable<Result<List<Floor>>> getFloor(@Query("access_token") String access_token, @Query("shop_id") String shop_id);
}
