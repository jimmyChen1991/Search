package com.hhyg.TyClosing.mgr;

import java.util.ArrayList;

import com.hhyg.TyClosing.dao.InitInfoDao;
import com.hhyg.TyClosing.dao.PickUpInfoDao;
import com.hhyg.TyClosing.dao.SalerInfoDao;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.info.PickUpInfo;
import com.hhyg.TyClosing.info.SalerInfo;

import android.content.SharedPreferences;


public class ClosingRefInfoMgr {

    private static ClosingRefInfoMgr mInstance = new ClosingRefInfoMgr();

    private int mChannelId = -1;
    private int mLatestPayTime = -1;
    private String mTyName;
    /**
     * 体验店ID
     */
    private String shopId;
    private SalerInfo mSalerInfo;
    
    private Integer chosenPickupInfoIndex;
    
    public void setAndSaveChosenPickupInfoIndex(int chosenPickupInfoIndex) {
		this.chosenPickupInfoIndex = chosenPickupInfoIndex;
		saveChosenPickupInfoIndexToSharedPreferences();
	}
	private void saveChosenPickupInfoIndexToSharedPreferences() {
		SharedPreferences pref =MyApplication.GetInstance().getSharedPreferences(MyApplication.GetInstance().getPackageName(), MyApplication.MODE_PRIVATE);
    	SharedPreferences.Editor eidt = pref.edit();
    	eidt.putInt("chosenPick", chosenPickupInfoIndex);
    	eidt.commit();
	}
    public int getChosenPickupInfoIndex() {
    	if(chosenPickupInfoIndex == null){
    		getChosenIndexFormSharedPreferences();
    	}
    	checkChosenIndexBound();
		return chosenPickupInfoIndex;
	}
    private void getChosenIndexFormSharedPreferences() {
    	SharedPreferences pref = MyApplication.GetInstance().getSharedPreferences(MyApplication.GetInstance().getPackageName(), MyApplication.MODE_PRIVATE);
    	chosenPickupInfoIndex = pref.getInt("chosenPick",0);
    }
	private void checkChosenIndexBound() {
		if(chosenPickupInfoIndex >= getAllPickUpAddr().size()){
    		chosenPickupInfoIndex = 0;
    	}
	}
    
    public static ClosingRefInfoMgr getInstance() {
        return mInstance;
    }

    private ArrayList<PickUpInfo> mAddrInfoList = new ArrayList<PickUpInfo>();

    public void add(PickUpInfo info) {
        mAddrInfoList.add(info);
    }

    public void clear() {
        mAddrInfoList.clear();
    }

    /* 功能：获取最晚支付时间
     * 返回：限制的最长支付时间  单位分钟
     */
    public int getLatestPayTime() {
        return mLatestPayTime;
    }

    public void setLatestPayTime(int latestPayTime) {
        mLatestPayTime = latestPayTime;
    }

    public String getTyName() {
        return mTyName;
    }

    public void setTyName(String name) {
        mTyName = name;
    }

    public int getChannelId() {
    	if(mChannelId == -1){
    		InitInfoDao dao = new InitInfoDao();
    		mChannelId = Integer.parseInt(dao.getChannleId());
    	}
        return mChannelId;
    }

    public void setChannelId(int channelId) {
        mChannelId = channelId;
    }
    /* 功能：获取可选提货地址列表
     * 返回： 列表  不需要释放内存
     */
    public ArrayList<PickUpInfo> getAllPickUpAddr() {
    	if(mAddrInfoList.size() == 0){
    		PickUpInfoDao dao = new PickUpInfoDao();
    		mAddrInfoList = dao.getAllPickUpInfo();
    	}
        return mAddrInfoList;
    }
    
    public int getCurPickupId(){
    	final int chosenIndex = getChosenPickupInfoIndex();
    	return getAllPickUpAddr().get(chosenIndex).id;
    }
    public void setAllPickUpAddr(ArrayList<PickUpInfo> pickUpInfos){
    	mAddrInfoList = pickUpInfos;
    }
    public String getShopId() {
    	if(shopId == null){
    		InitInfoDao dao = new InitInfoDao();
    		shopId = dao.getShopId();
    	}
        return shopId;
    }

    public void setShopId(String shopId) {
        this.shopId = shopId;
    }

	public int getInitChannelId() {
		 return mChannelId;
	}

	public SalerInfo getSalerInfo() {
		if(mSalerInfo != null){
			return mSalerInfo;
		}
		SalerInfoDao dao = new SalerInfoDao();
		return dao.getSalerInfo();
	}

	public String getSalerName(){
        SalerInfo info = getSalerInfo();
        return info.getSalerName();
    }
    public String getUserName(){
        SalerInfo info = getSalerInfo();
        return info.getUserName();
    }
    public String getSalerId(){
        SalerInfo info = getSalerInfo();
        return info.getSalerId();
    }
    public void setSalerInfo(SalerInfo salerInfo) {
		mSalerInfo = salerInfo;
	}
	public boolean isShopTypeOutside(){
		return getSalerInfo().isShopTypeOutside();
	}
}

	