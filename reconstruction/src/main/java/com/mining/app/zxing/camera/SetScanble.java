package com.mining.app.zxing.camera;

public class SetScanble implements Runnable{
	
	private MyBoolean scanbel;
	
	
	public SetScanble(MyBoolean bool) {
		super();
		this.scanbel = bool;
	}
	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			Thread.sleep(2000);
			scanbel.setmBool(true);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

}
