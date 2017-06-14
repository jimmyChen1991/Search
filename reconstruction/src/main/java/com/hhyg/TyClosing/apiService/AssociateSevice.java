package com.hhyg.TyClosing.apiService;

import com.hhyg.TyClosing.entities.associate.AssociateRes;

import io.reactivex.Observable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by user on 2017/6/14.
 */

public interface AssociateSevice {
    @POST("index.php?r=essearch/associate")
    @FormUrlEncoded
    Observable<AssociateRes> getAssociate(@Field("parameter") String param);
}
