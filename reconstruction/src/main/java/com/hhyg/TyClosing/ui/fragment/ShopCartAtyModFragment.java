package com.hhyg.TyClosing.ui.fragment;

import java.util.ArrayList;

import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.info.ActiveInfo;
import com.hhyg.TyClosing.info.ShopCartItem;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ShopCartAtyModFragment extends DialogFragment{
	private String activeId;
	private ShopCartItem cartItem;
	private AtyModListener modLister;
	public ShopCartAtyModFragment() {
		// TODO Auto-generated constructor stub
	}
	@SuppressLint("ValidFragment")
	public ShopCartAtyModFragment(String activeId,ShopCartItem cartItem) {
		// TODO Auto-generated constructor stub
		this.activeId = activeId;
		this.cartItem = cartItem;
	}
	public void setModLister(AtyModListener modLister) {
		this.modLister = modLister;
	}
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		 final Dialog dialog = new AlertDialog.Builder(getActivity()).create();
		 LayoutInflater inflater = LayoutInflater.from(getActivity());
		 RelativeLayout layout = (RelativeLayout) inflater.inflate(R.layout.shopcart_itematymod_dialog, null);
		 ListView lv = (ListView) layout.findViewById(R.id.atymod_lv);
		 final ActivityModAdapter adapter = new ActivityModAdapter(activeId,cartItem.getaInfos());
		 lv.setAdapter(adapter);
		 lv.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				adapter.setaId(cartItem.getaInfos().get(position).getActiveId());
				adapter.notifyDataSetChanged();
			}
		});
		 dialog.show();
		 dialog.getWindow().setContentView(layout);
		 dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
		 dialog.getWindow().setLayout(900, 800);
		 Button okBtn = (Button) layout.findViewById(R.id.mod_ok);
		 Button cancelBtn = (Button) layout.findViewById(R.id.mod_cancel);
		 okBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
				modLister.onAtyMod(cartItem.getBarCode(),adapter.getaId());
			}
		});
		 cancelBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				dismiss();
			}
		});
         dialog.setCancelable(true);
         return dialog;
	}
	
	class ActivityModAdapter extends BaseAdapter{
		
		private ArrayList<ActiveInfo> mDataset;
		private String aId;
		public void setaId(String aId) {
			this.aId = aId;
		}
		public String getaId() {
			return aId;
		}
		public ActivityModAdapter(String id,ArrayList<ActiveInfo> param) {
			mDataset = param;
			aId = id;
		}
		
		class ViewHolder{
			public TextView itemName;
			public ImageView chosenImg;
			public ImageView privilege_icon;
		}
		
		@Override
		public int getCount() {
			return mDataset.size();
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder viewHolder = null;
			if(convertView == null){
				viewHolder = new ViewHolder();
				LayoutInflater inflater = LayoutInflater.from(getActivity());
				convertView = inflater.inflate(R.layout.shopcartitem_atymoddialog_item, null);
				viewHolder.chosenImg = (ImageView) convertView.findViewById(R.id.chosenimg);
				viewHolder.itemName = (TextView) convertView.findViewById(R.id.itemname);
				viewHolder.privilege_icon = (ImageView) convertView.findViewById(R.id.privilege_icon);
				convertView.setTag(viewHolder);;
			}else{
				viewHolder = (ViewHolder) convertView.getTag();
			}
			final ActiveInfo aInfo = mDataset.get(position);
			viewHolder.itemName.setText(aInfo.getShort_desc());
			if(aInfo != null){
				if(aInfo.getPrivilegeType() == ActiveInfo.PrivilegeType.PRIVILEGE_TYPE){
					viewHolder.privilege_icon.setVisibility(View.VISIBLE);
				}else{
					viewHolder.privilege_icon.setVisibility(View.GONE);
				}
				if(aInfo.getActiveId().equals(aId)){
					viewHolder.chosenImg.setVisibility(View.VISIBLE);
				}else{
					viewHolder.chosenImg.setVisibility(View.INVISIBLE);
				}
			}
			return convertView;
		}
	}
	
	public interface AtyModListener{
		void onAtyMod(String barcode,String activeId);
	}
}
