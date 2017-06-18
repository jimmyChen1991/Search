package com.hhyg.TyClosing.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils.TruncateAt;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.allShop.adapter.AllShopBaseAdapter;
import com.hhyg.TyClosing.ui.adapter.search.AssociateAdapter;
import com.hhyg.TyClosing.allShop.adapter.OnItemClickListener;
import com.hhyg.TyClosing.allShop.handler.SimpleHandler;
import com.hhyg.TyClosing.allShop.info.SpecialInfo;
import com.hhyg.TyClosing.apiService.AssociateSevice;
import com.hhyg.TyClosing.config.Constants;
import com.hhyg.TyClosing.di.componet.DaggerAssociateComponent;
import com.hhyg.TyClosing.di.componet.DaggerCommonNetParamComponent;
import com.hhyg.TyClosing.di.module.AssociateModule;
import com.hhyg.TyClosing.di.module.CommonNetParamModule;
import com.hhyg.TyClosing.entities.associate.AssociateParam;
import com.hhyg.TyClosing.entities.associate.AssociateRes;
import com.hhyg.TyClosing.entities.search.SearchGoodsParam;
import com.hhyg.TyClosing.entities.search.SearchType;
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
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.util.ArrayList;

import javax.inject.Inject;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.functions.Predicate;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends BaseActivity implements View.OnClickListener{
	private static final String TAG = "searchActivity";
	private final String HOT_SEARCH_URI = Constants.getIndexUrl()+"?r=hotsearch/hotsearch";
	private final String HISTORY_SEARCH_URI = Constants.getIndexUrl()+"?r=searchwords/historywords";
	protected HttpUtil mHttpUtil = MyApplication.GetInstance();
	protected HotSearchProc mHotSearchProc = new HotSearchProc();
	private HistorySearchProc mHistoryProc = new HistorySearchProc();
	private LinearLayout ViewGroup;
	private LinearLayout historyGroup;
	private ArrayList<HotSearchWord> HotWords;
	private ArrayList<HotSearchWord> HistotyWords = new ArrayList<>();
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
	private RecyclerView associateRec;
	private View wordwrap;
	@Inject
	AssociateSevice associateSevice;
	@Inject
	AssociateParam associateParam;
	@Inject
	Gson gSon;
	@Inject
	AssociateAdapter associateAdapter;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.search);
		findView();
		DaggerAssociateComponent.builder()
				.applicationComponent(MyApplication.GetInstance().getComponent())
				.commonNetParamComponent(DaggerCommonNetParamComponent.builder().commonNetParamModule(new CommonNetParamModule()).build())
				.associateModule(new AssociateModule())
				.build()
				.inject(this);
		associateAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
			@Override
			public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
				AssociateRes.DataBean bean = (AssociateRes.DataBean) adapter.getData().get(position);
				final String keyWord = bean.getName();
				jumpToSearchGoodActivity(keyWord);
			}
		});
		associateRec.setAdapter(associateAdapter);
		RxTextView.textChanges(mEditText)
				.filter(new Predicate<CharSequence>() {
					@Override
					public boolean test(@NonNull CharSequence charSequence) throws Exception {
						return charSequence.length() == 0;
					}
				})
				.subscribe(new Consumer<CharSequence>() {
					@Override
					public void accept(@NonNull CharSequence charSequence) throws Exception {
						wordwrap.setVisibility(View.VISIBLE);
						associateRec.setVisibility(View.GONE);
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(@NonNull Throwable throwable) throws Exception {

					}
				});
		RxTextView.textChanges(mEditText)
				.observeOn(AndroidSchedulers.mainThread())
				.filter(new Predicate<CharSequence>() {
					@Override
					public boolean test(@NonNull CharSequence charSequence) throws Exception {
						return charSequence.length() > 0;
					}
				})
				.doOnNext(new Consumer<CharSequence>() {
					@Override
					public void accept(@NonNull CharSequence charSequence) throws Exception {
						wordwrap.setVisibility(View.GONE);
					}
				})
				.observeOn(Schedulers.io())
				.map(new Function<CharSequence, String>() {
					@Override
					public String apply(@NonNull CharSequence charSequence) throws Exception {
						return charSequence.toString();
					}
				})
				.map(new Function<String, AssociateParam>() {
					@Override
					public AssociateParam apply(@NonNull String s) throws Exception {
						AssociateParam.DataBean bean = new AssociateParam.DataBean();
						bean.setKeyword(mEditText.getText().toString());
						associateParam.setData(bean);
						return associateParam;
					}
				})
				.concatMap(new Function<AssociateParam, ObservableSource<AssociateRes>>() {
					@Override
					public ObservableSource<AssociateRes> apply(@NonNull AssociateParam associateParam) throws Exception {
						return associateSevice.getAssociate(gSon.toJson(associateParam))
								.onErrorResumeNext(Observable.<AssociateRes>empty());
					}
				})
				.filter(new Predicate<AssociateRes>() {
					@Override
					public boolean test(@NonNull AssociateRes associateRes) throws Exception {
						return mEditText.getText().toString().length() > 0;
					}
				})
				.observeOn(AndroidSchedulers.mainThread())
				.subscribe(new Consumer<AssociateRes>() {
					@Override
					public void accept(@NonNull AssociateRes associateRes) throws Exception {
						Log.v(TAG,"success");
						associateRec.setVisibility(View.VISIBLE);
						associateAdapter.setNewData(associateRes.getData());
					}
				}, new Consumer<Throwable>() {
					@Override
					public void accept(@NonNull Throwable throwable) throws Exception {
						Log.v(TAG,"failed");
						Log.v(TAG, throwable.toString());
					}
				}, new Action() {
					@Override
					public void run() throws Exception {

					}
				}, new Consumer<Disposable>() {
					@Override
					public void accept(@NonNull Disposable disposable) throws Exception {

					}
				});
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
		wordwrap = findViewById(R.id.wordwrap);
		associateRec = (RecyclerView) findViewById(R.id.associate_rec);
		associateRec.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
		associateRec.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
		associateRec.setHasFixedSize(true);
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

	private void showWords(ArrayList<HotSearchWord> words,ViewGroup container){
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
			final HotSearchWord word = words.get(idx);
			LinearLayout.LayoutParams itemParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			Button view = new Button(this);
			view.setMaxLines(1);
			view.setEllipsize(TruncateAt.END);
			if(word.isHightlight()){
				view.setTextColor(Constants.SELECTOR_COLOR);
			}else{
				view.setTextColor(Constants.UNSELECTOR_BLACK_COLOR);
			}
			view.setText(word.getWord());
			view.setBackgroundResource(R.drawable.searchhot_word);
			view.setOnClickListener(this);
			view.setTag(words.get(idx));
			view.setMaxWidth(maxWidth);
			itemParams.setMargins(0, 20, 20, 0);
			view.setLayoutParams(itemParams);
			measurePaint.setTextSize(view.getTextSize());
			if(remianWidth > (measurePaint.measureText(word.getWord()) + 50)){
				horizLL.addView(view);
			    remianWidth = (int) (remianWidth-measurePaint.measureText(word.getWord()) - 50);
			}else{
				horizLL = new LinearLayout(this);
		     	horizLL.setOrientation(LinearLayout.HORIZONTAL);
		     	horizLL.setLayoutParams(layoutParams);
		     	horizLL.addView(view);
				container.addView(horizLL);
				if(measurePaint.measureText(word.getWord()) > maxWidth){
					remianWidth = maxWidth;
				}else{
					remianWidth = (int) (maxWidth - measurePaint.measureText(word.getWord()) - 50);
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
		HotSearchWord searchWord = (HotSearchWord) v.getTag();
		if(searchWord.getSpecialId() != 0){
			jumpToSpecialActivity(String.valueOf(searchWord.getSpecialId()));
		}else{
			jumpToSearchGoodActivity(searchWord.getWord());
		}
	}
	private void jumpToSpecialActivity(String specialId){
		Intent intent = new Intent(this, SpecialActivity.class);
		intent.putExtra("specialid", specialId);
		startActivity(intent);
	}
	private void jumpToSearchGoodActivity(String searchWord){
		UserTrackMgr.getInstance().onEvent("searchkey",searchWord);
		HotSearchWord word = new HotSearchWord();
		word.setWord(searchWord);
		HistotyWords.add(word);
		showWords(HistotyWords, historyGroup);
		SearchGoodsParam.DataBean param =  new SearchGoodsParam.DataBean();
		param.setKeyword(searchWord);
		Intent it = new Intent(SearchActivity.this,SearchGoodActivity.class);
		it.putExtra(getString(R.string.search_token),param);
		it.putExtra(getString(R.string.search_content),searchWord);
		it.putExtra(getString(R.string.search_type), SearchType.KEY_WORD.ordinal());
		startActivity(it);
	}
	private String MakeJsonString(){
		JSONObject param = new JSONObject();
		param.put("imei", MyApplication.GetInstance().getAndroidId());
		param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
		param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
		param.put("platformId",3);
		return param.toString();
	}
	private String MakeHistoryJson(){
		JSONObject param = new JSONObject();
		param.put("imei", MyApplication.GetInstance().getAndroidId());
		param.put("shopid", ClosingRefInfoMgr.getInstance().getShopId());
		param.put("channel", ClosingRefInfoMgr.getInstance().getChannelId());
		param.put("platformId",3);
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
			ArrayList<HotSearchWord> hotWords = new ArrayList<>();
			ArrayList<SpecialInfo> AD = new ArrayList<SpecialInfo>();
			for (int idx = 0; idx < dataArray.size(); idx++) {
				JSONObject json = dataArray.getJSONObject(idx);
				HotSearchWord wordWrap = new HotSearchWord();
				wordWrap.setHightlight(json.getBooleanValue("highlight"));
				wordWrap.setSpecialId(json.getIntValue("specialid"));
				wordWrap.setWord(json.getString("word"));
				hotWords.add(wordWrap);
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
			ArrayList<HotSearchWord> Words = new ArrayList<HotSearchWord>();
			for (int idx = 0; idx < dataArray.size(); idx++) {
				JSONObject json = dataArray.getJSONObject(idx);
				HotSearchWord wordWrap = new HotSearchWord();
				wordWrap.setWord(json.getString("word"));
				Words.add(wordWrap);
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

	static class HotSearchWord{
		private String word;
		private boolean hightlight;
		private int specialId;

		public String getWord() {
			return word;
		}

		public void setWord(String word) {
			this.word = word;
		}

		public boolean isHightlight() {
			return hightlight;
		}

		public void setHightlight(boolean hightlight) {
			this.hightlight = hightlight;
		}

		public int getSpecialId() {
			return specialId;
		}

		public void setSpecialId(int specialId) {
			this.specialId = specialId;
		}
	}
}
