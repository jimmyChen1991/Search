package com.hhyg.TyClosing.mgr;

import java.util.Comparator;

import com.hhyg.TyClosing.info.ActiveInfo;

public class ActiveInfoComparator implements Comparator<ActiveInfo>{

	@Override
	public int compare(ActiveInfo lhs, ActiveInfo rhs) {
		int res = 0;
		if(lhs.getPriority() < rhs.getPriority()){
			res = -1;
		}else if(lhs.getPriority() == rhs.getPriority()){
			res = 0;
		}else{
			res = 1;
		}
		return res;
	}

}
