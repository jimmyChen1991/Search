package com.hhyg.TyClosing.mgr;

public class RestrictionMgr {
	
	public final double FREE_DUTY_THREAD = 8000;
	
	private static RestrictionMgr mInstance = new RestrictionMgr();

	public static RestrictionMgr getInstance() { 
		return mInstance;	
	}

}