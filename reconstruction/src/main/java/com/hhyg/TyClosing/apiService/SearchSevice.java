package com.hhyg.TyClosing.apiService;

import com.hhyg.TyClosing.entities.search.SearchFilterRes;
import com.hhyg.TyClosing.entities.search.SearchGoods;
import com.hhyg.TyClosing.entities.shopcart.ShopcartListRes;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by user on 2017/6/9.
 */

public interface SearchSevice {
    @POST("index.php?r=essearch/searchgoods")
    @FormUrlEncoded
    Observable<SearchGoods> searchGoodsApi(@Field("parameter") String param);

    @POST("index.php?r=essearch/searchfilter")
    @FormUrlEncoded
    Observable<SearchFilterRes> searchFilterApi(@Field("parameter") String param);

    @POST("index.php?r=cart/padcartlist")
    @FormUrlEncoded
    Observable<ShopcartListRes> shopcartList(@Field("parameter") String param);
}