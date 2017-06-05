package com.hhyg.TyClosing.ui;
import java.io.IOException;
import java.util.ArrayList;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.AllShopBaseAdapter;
import com.hhyg.TyClosing.allShop.adapter.OnItemClickListener;
import com.hhyg.TyClosing.allShop.handler.SimpleHandler;
import com.hhyg.TyClosing.allShop.info.SearchInfo;
import com.hhyg.TyClosing.allShop.info.SpecialInfo;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.global.HttpUtil;
import com.hhyg.TyClosing.global.INetWorkCallBack;
import com.hhyg.TyClosing.global.ImageHelper;
import com.hhyg.TyClosing.global.MyApplication;
import com.hhyg.TyClosing.global.NetCallBackHandlerException;
import com.hhyg.TyClosing.global.ProcMsgHelper;
import com.hhyg.TyClosing.mgr.ClosingRefInfoMgr;
import com.hhyg.TyClosing.mgr.UserTrackMgr;
import com.hhyg.TyClosing.ui.base.BaseActivity;
import com.hhyg.TyClosing.ui.view.AutoClearEditText;
import com.hhyg.TyClosing.ui.view.AutoClearEditText.OnCommitBtnListener;
import com.hhyg.TyClosing.ui.view.ProgressBar;
import com.hhyg.TyClosing.ui.view.SimpleProgressBar;
import com.nostra13.universalimageloader.core.ImageLoader;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils.TruncateAt;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
public class SearchActivity extends BaseActivity implements View.OnClickListener{
	private final String HOT_SEARCH_URI = Constants.getIndexUrl()+"?r=hotsearch/hotsearch";
	private final String HISTORY_SEARCH_URI = Constants.getIndexUrl()+"?r=searchwords/historywords";
	protected HttpUtil mHttpUtil = MyApplication.GetInstance();
	protected HotSearchProc mHotSearchProc = new HotSearchProc();
	private HistorySearchProc mHistoryProc = new HistorySearchProc();
	private LinearLayout ViewGroup;
	private LinearLayout historyGroup;
	private ArrayList<String> HotWords;
	private ArrayList<String> HistotyWords = new ArrayList<String>();
	private ArrayList<SpecialInfo> AdInfos;
	private ListView mListView;
	private MyAdapter mAdapter;
	private	ImageButton backbtn;
	private ImageButton deleteHistory;
	private ProgressBar mProgressBar;
	private AutoClearEditText mEditText;
	private ImageButton searchCommmitBtn;
	/*savedInstanceState中存储的热搜词*/
	private final String THE_HOTWORD = "theHotWord";
	/*savedInstanceState中存储的广告信息*/
	private final String THE_ADINFOS = "theADInfos";
	private ExceptionHandler exceptionHandler = new ExceptionHandler();
	private Handler mHandler = new Handler(Looper.getMainLooper());
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		findView();
		mProgressBar.startProgress();
		mHttpUtil.post(HOT_SEARCH_URI, MakeJsonString(),new NetCallBackHandlerException(exceptionHandler, mHotSearchProc));
	}
	
	class ExceptionHandler extends SimpleHandler{
		@Override
		public void handleMessage(Message msg) {
			mProgressBar.stopProgress();
			super.handleMessage(msg);
			switch(msg.what){
				case 4:
					Toast.makeText(getApplication(), (String) msg.obj, Toast.LENGTH_SHORT).show();
					break;
			}
		}
	}
	
	@Override
	protected void onSaveInstanceState(Bundle outState) {
		outState.putStringArrayList(THE_HOTWORD, HotWords);
		outState.putParcelableArrayList(THE_ADINFOS, AdInfos);
		outState.putStringArrayList("history", HistotyWords);
		super.onSaveInstanceState(outState);
	}

	private void findView() {
		deleteHistory = (ImageButton) findViewById(R.id.clearbtn);
		mProgressBar = new SimpleProgressBar((ImageView) findViewById(R.id.infoOperating));
		historyGroup = (LinearLayout) findViewById(R.id.htyviewgroup);
		ViewGroup = (LinearLayout) findViewById(R.id.viewGroup);
		mListView = (ListView) findViewById(R.id.adlv);
		backbtn = (ImageButton) findViewById(R.id.backbtn);
		mEditText = (AutoClearEditText) findViewById(R.id.searchbar);
		searchCommmitBtn = (ImageButton) findViewById(R.id.searchcommmit);
		backbtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		searchCommmitBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String searchWord = mEditText.getText().toString();
				char[] cs =searchWord.toCharArray();
				char blank = " ".charAt(0);
				boolean onlyBlank = true;
				for(int index = 0; index < cs.length;index ++){
					if(cs[index] != blank){
						onlyBlank = false;
					}
				}
				if(onlyBlank){
					mEditText.clearComposingText();
					return;
				}
				jumpToSearchGoodActivity(searchWord);
			}
		});
		mEditText.setOnCommitBtnListener(new OnCommitBtnListener(){
			@Override
			public void OnCanCommit() {
				searchCommmitBtn.setBackgroundResource(R.drawable.searchcommit_selector);
				searchCommmitBtn.setClickable(true);
			}
			@Override
			public void OnCantCommit() {
				searchCommmitBtn.setBackgroundResource(R.drawable.search_pressed);
				searchCommmitBtn.setClickable(false);
			}
			
		});
		deleteHistory.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				deleteHistoty();
			}
		});
	}

	private void deleteHistoty() {
		mHttpUtil.post(Constants.getIndexUrl() +"?r=searchwords/delhistorywords", makeDelJson(), new INetWorkCallBack() {
			@Override
			public void PostProcess(int msgId, String msg) {
				
			}
		});
		HistotyWords.clear();
		showNoHistory();
	}
	private void init(){
		mAdapter = new MyAdapter(this);
		mListView.setAdapter(mAdapter);
		mAdapter.addItem(AdInfos);
		mAdapter.setOnItemClickLister(new OnItemClickListener<SpecialInfo>(){
			@Override
			public void onClick(SpecialInfo item) {
				jumpToSpecialActivity(item.id);
			}
		} );
		showWords(HotWords,ViewGroup);
		if(HistotyWords.size() == 0){
			showNoHistory();
		}else{
			showWords(HistotyWords,historyGroup);
		}
		mProgressBar.stopProgress();
	}
	private void showNoHistory() {
		historyGroup.removeAllViews();
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); 
		TextView warning = new TextView(this);
		layoutParams.setMargins(0, 6, 0, 0);
		warning.setLayoutParams(layoutParams);
		warning.setTextColor(Constants.UNSELECTOR_BLACK_COLOR);
		warning.setText("无搜索历史");
		historyGroup.addView(warning);
	}

	private void showWords(ArrayList<String> words,ViewGroup container){
		container.removeAllViews();
		int count = words.size(); 
		final int maxWidth = (container.getMeasuredWidth() - container.getPaddingLeft() - container.getPaddingRight())/4*3;
		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT); 
     	layoutParams.setMargins(10, 0, 10, 0); 
     	Paint measurePaint = new Paint();
     	int remianWidth = maxWidth;
		LinearLayout  horizLL = new LinearLayout(this);
     	horizLL.setOrientation(LinearLayout.HORIZONTAL);
     	horizLL.setLayoutParams(layoutParams);
     	container.addView(horizLL);
		for(int idx = 0;idx<count;idx++){
			final String word = words.get(idx);
			LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			Button view = new Button(this);
			view.setMaxLines(1);
			view.setEllipsize(TruncateAt.END);
			view.setText(word);
			view.setBackgroundResource(R.drawable.searchhot_word);
			view.setOnClickListener(this);
			view.setTag(word);
			view.setMaxWidth(maxWidth);
			itemParams.setMargins(0, 20, 20, 0);
			view.setLayoutParams(itemParams);
			measurePaint.setTextSize(view.getTextSize());  
			if(remianWidth > (measurePaint.measureText(word) + 20)){
				horizLL.addView(view);
			    remianWidth = (int) (remianWidth-measurePaint.measureText(word) - 20);
			}else{
				horizLL = new LinearLayout(this);
		     	horizLL.setOrientation(LinearLayout.HORIZONTAL);
		     	horizLL.setLayoutParams(layoutParams);
		     	horizLL.addView(view);
				container.addView(horizLL);
				if(measurePaint.measureText(word) > maxWidth){
					remianWidth = maxWidth;
				}else{
					remianWidth = (int) (maxWidth - measurePaint.measureText(word) - 20);
				}
			}
     	}
	}
	class MyAdapter extends AllShopBaseAdapter<SpecialInfo,MyAdapter.ViewHolder>{
		public MyAdapter(Context context) {
			super(context);
		}
		public class ViewHolder{
			public ImageView ImageItem;
		}
		@Override
		protected View getConvertView() {
			View convertView = inflateItemView(R.layout.searchactivity_item);
			return convertView;
		}
		@Override
		protected ViewHolder getViewInstance(View convertView, ViewHolder viewHolder) {
			viewHolder = new ViewHolder();
			viewHolder.ImageItem = (ImageView)convertView.findViewById(R.id.searchactivity_item);
			return viewHolder;
		}
		@Override
		protected void bindDataToItemView(ViewHolder viewHolder, SpecialInfo item) {
			ImageLoader.getInstance().displayImage(item.netUri, viewHolder.ImageItem, ImageHelper.initSpecialPathOption());
		}
	}
	@Override
	public void onClick(View v) {
		jumpToSearchGoodActivity(v);
	}
	private void jumpToSearchGoodActivity(View v){
		String searchWord = (String) v.getTag();
		jumpToSearchGoodActivity(searchWord);
	}
	private void jumpToSpecialActivity(String specialId){
		Intent intent = new Intent(this, SpecialActivity.class);
		intent.putExtra("specialid", specialId);
		startActivity(intent);
	}
	private void jumpToSearchGoodActivity(String searchWord){
		UserTrackMgr.getInstance().onEvent("searchkey",searchWord);
		HistotyWords.add(searchWord);
		showWords(HistotyWords, historyGroup);
		SearchInfo info = SearchInfo.NewInstance(SearchInfo.KEYWORD_SEARCH, searchWord ,searchWord);
		Intent it = new Intent();
		it.setClass(this, GoodListActivity.class);
		it.putExtra("searchInfo", info);
		startActivity(it);
	}
	private String MakeJsonString(){
		JSONObject param = new JSONObject();
		param.put("imei", MyApplication.GetInstance().getAndroidId());
		param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
		param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
		return param.toString();
	}
	private String MakeHistoryJson(){
		JSONObject param = new JSONObject();
		param.put("imei", MyApplication.GetInstance().getAndroidId());
		param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
		param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
		JSONObject data = new JSONObject();
		data.put("num", 10);
		data.put("usersignmd5", ClosingRefInfoMgr.getInstance().getSalerId());
		param.put("data", data);
		return param.toString();
	}
	private String makeDelJson(){
		JSONObject param = new JSONObject();
		param.put("imei", MyApplication.GetInstance().getAndroidId());
		param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
		param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
		JSONObject data = new JSONObject();
		data.put("usersignmd5", ClosingRefInfoMgr.getInstance().getSalerId());
		param.put("data", data);
		return param.toString();
	}
	class HotSearchProc implements ProcMsgHelper {
		@Override
		public void ProcMsg(String msgBody) throws JSONException, IOException {
			JSONObject jsonObj = JSONObject.parseObject(msgBody);
			JSONArray dataArray = jsonObj.getJSONArray("hotword");
			ArrayList<String> hotWords = new ArrayList<String>();
			ArrayList<SpecialInfo> AD = new ArrayList<SpecialInfo>();
			for (int idx = 0; idx < dataArray.size(); idx++) {
				JSONObject json = dataArray.getJSONObject(idx);
				String word = json.getString("word");
				hotWords.add(word);
			}
			JSONArray AddataArray = jsonObj.getJSONArray("ADdata");
			for (int idx = 0; idx < AddataArray.size(); idx++) {
				JSONObject json = AddataArray.getJSONObject(idx);
				SpecialInfo info = new SpecialInfo();
				info.id = json.getString("specialid");
				info.netUri = json.getString("url");
				AD.add(info);
			}
			SearchActivity.this.HotWords = hotWords;
			SearchActivity.this.AdInfos = AD;
			mHttpUtil.post(HISTORY_SEARCH_URI, MakeHistoryJson(),
					new NetCallBackHandlerException(exceptionHandler, mHistoryProc));
		}
	}
	class HistorySearchProc implements ProcMsgHelper{
		@Override
		public void ProcMsg(String msgBody) throws JSONException, IOException {
			JSONObject jsonObj = JSONObject.parseObject(msgBody);
			JSONArray dataArray = jsonObj.getJSONArray("data");
			ArrayList<String> Words = new ArrayList<String>();
			for (int idx = 0; idx < dataArray.size(); idx++) {
				JSONObject json = dataArray.getJSONObject(idx);
				String word = json.getString("word");
				Words.add(word);
			}
			Words.removeAll(HistotyWords);
			HistotyWords.addAll(Words);
			mHandler.post(new Runnable() {
				@Override
				public void run() {
					init();
				}
			});
		}
	}
}
