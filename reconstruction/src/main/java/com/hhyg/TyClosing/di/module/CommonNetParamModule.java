package com.hhyg.TyClosing.di.module;

import com.hhyg.TyClosing.entities.CommonParam;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

/**
 * Created by user on 2017/6/9.
 */
@Module
public class CommonNetParamModule {
    @Provides
    @Named("imei")
    String provideImei(){
       return MyApplication.GetInstance().getAndroidId();
    }

    @Provides
    @Named("channelId")
    String provideChannle(){
        return ClosingRefInfoMgr.getInstance().getChannelIdStr();
    }

    @Provides
    @Named("shopid")
    String provideShopid(){
        return ClosingRefInfoMgr.getInstance().getShopId();
    }

    @Provides
    @Named("platformId")
    String providePlatformId(){
        return "3";
    }

    @Provides
    CommonParam provideCommonParam(@Named("imei") String imei,@Named("channelId") String channelId, @Named("shopid") String shopid,
                                   @Named("platformId") String platfromId)
    {
        CommonParam param = new CommonParam();
        param.setPlatformId(platfromId);
        param.setImei(imei);
        param.setChannelId(channelId);
        param.setShopId(shopid);
        return param;
    }
}
