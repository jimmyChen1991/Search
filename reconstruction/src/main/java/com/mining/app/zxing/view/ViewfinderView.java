/*
 * Copyright (C) 2008 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mining.app.zxing.view;

import java.util.Collection;
import java.util.HashSet;

import com.google.zxing.ResultPoint;
import com.hhyg.TyClosing.R;
import com.hhyg.TyClosing.log.Logger;
import com.mining.app.zxing.camera.CameraManager;
import com.mining.app.zxing.camera.CameraManager.RectSetting;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.view.View;

/**
 * This view is overlaid on top of the camera preview. It adds the viewfinder
 * rectangle and partial transparency outside it, as well as the laser scanner
 * animation and result points.
 * 
 */
public class ViewfinderView extends View {
	private static final String TAG = "log";
	/**
	 * ˢ�½����ʱ��
	 */
	protected static final long ANIMATION_DELAY = 10L;
	protected static final int OPAQUE = 0xFF;

	/**
	 * �ĸ���ɫ�߽Ƕ�Ӧ�ĳ���
	 */
	protected int ScreenRate;
	
	/**
	 * �ĸ���ɫ�߽Ƕ�Ӧ�Ŀ��
	 */
	protected static final int CORNER_WIDTH = 25;
	/**
	 * ɨ����е��м��ߵĿ��
	 */
	protected static final int MIDDLE_LINE_WIDTH = 6;
	
	/**
	 * ɨ����е��м��ߵ���ɨ������ҵļ�϶
	 */
	protected static final int MIDDLE_LINE_PADDING = 5;
	
	/**
	 * �м�������ÿ��ˢ���ƶ��ľ���
	 */
	protected static final int SPEEN_DISTANCE = 5;
	
	/**
	 * �ֻ�����Ļ�ܶ�
	 */
	protected static float density;
	/**
	 * �����С
	 */
	protected static final int TEXT_SIZE = 16;
	/**
	 * �������ɨ�������ľ���
	 */
	protected static final int TEXT_PADDING_TOP = 30;
	
	/**
	 * ���ʶ��������
	 */
	protected Paint paint;
	protected Paint btmPaint;
	/**
	 * �м们���ߵ����λ��
	 */
	protected int slideTop;
	
	/**
	 * �м们���ߵ���׶�λ��
	 */
	protected int slideBottom;
	
	/**
	 * ��ɨ��Ķ�ά��������������û��������ܣ���ʱ������
	 */
	protected Bitmap resultBitmap;
	protected final int maskColor;
	protected final int resultColor;
	
	protected final int resultPointColor;
	protected Collection<ResultPoint> possibleResultPoints;
	protected Collection<ResultPoint> lastPossibleResultPoints;

	protected boolean isFirst;
	
	protected Bitmap mBitmap;
	public ViewfinderView(Context context, AttributeSet attrs) {
		super(context, attrs);
		density = context.getResources().getDisplayMetrics().density;
		Logger.GetInstance().Debug("getdensity__"+density);
		//������ת����dp
		//ScreenRate = (int)(20 * density);
		ScreenRate  = 112;
		paint = new Paint();
		Resources resources = getResources();
		maskColor = resources.getColor(R.color.viewfinder_mask);
		resultColor = resources.getColor(R.color.result_view);

		resultPointColor = resources.getColor(R.color.possible_result_points);
		possibleResultPoints = new HashSet<ResultPoint>(5);
		Bitmap btm = ((BitmapDrawable)getResources().getDrawable(R.drawable.saoyisao)).getBitmap();		
		btmPaint = new Paint();
		btmPaint = new Paint(Paint.ANTI_ALIAS_FLAG);  
		btmPaint.setFilterBitmap(true);  
		btmPaint.setDither(true);
        float len = 1.4f;
        float widthscale = 1f;
        mBitmap = shorter(btm,len,widthscale);
	
		
	}

	@Override
	public void onDraw(Canvas canvas) {
		//�м��ɨ�����Ҫ�޸�ɨ���Ĵ�С��ȥCameraManager�����޸�
		Rect frame = CameraManager.get().getFramingRect();		
		if (frame == null) {
			return;
		}		
		//��ʼ���м��߻��������ϱߺ����±�
		if(!isFirst){
			isFirst = true;
			slideTop = frame.top;
			slideBottom = frame.bottom;
		}
		
		//��ȡ��Ļ�Ŀ�͸�
		int width = canvas.getWidth();
		int height = canvas.getHeight();
		paint.setColor(resultBitmap != null ? resultColor : maskColor);
		paint.setAlpha(180);
		//����ɨ����������Ӱ���֣����ĸ����֣�ɨ�������浽��Ļ���棬ɨ�������浽��Ļ����
		//ɨ��������浽��Ļ��ߣ�ɨ�����ұߵ���Ļ�ұ�		
		canvas.drawRect(0, 0, width, frame.top, paint);
		canvas.drawRect(0, frame.top, frame.left, frame.bottom + 1, paint);
		canvas.drawRect(frame.right + 1, frame.top, width, frame.bottom + 1,
				paint);
		canvas.drawRect(0, frame.bottom + 1, width, height, paint);
		if (resultBitmap != null) {
			// Draw the opaque result bitmap over the scanning rectangle
			paint.setAlpha(OPAQUE);
			canvas.drawBitmap(resultBitmap, frame.left, frame.top, paint);
		} else {

			//��ɨ�����ϵĽǣ��ܹ�8������
			//paint.setARGB(255, 230, 218, 207);
			paint.setColor(Color.WHITE);
//			canvas.drawRect(frame.left + ScreenRate, frame.top, frame.right-ScreenRate, frame.top+3, paint);
//			canvas.drawRect(frame.left, frame.top+ScreenRate, frame.left+3, frame.bottom-ScreenRate, paint);
//			canvas.drawRect(frame.left + ScreenRate, frame.bottom - 3, frame.right -ScreenRate , frame.bottom, paint);
//			canvas.drawRect(frame.right-3, frame.top+ScreenRate, frame.right, frame.bottom-ScreenRate, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + ScreenRate,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.left, frame.top, frame.left + CORNER_WIDTH, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.top, frame.right,
					frame.top + CORNER_WIDTH, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.top, frame.right, frame.top
					+ ScreenRate, paint);
			canvas.drawRect(frame.left, frame.bottom - CORNER_WIDTH, frame.left
					+ ScreenRate, frame.bottom, paint);
			canvas.drawRect(frame.left, frame.bottom - ScreenRate,
					frame.left + CORNER_WIDTH, frame.bottom, paint);
			canvas.drawRect(frame.right - ScreenRate, frame.bottom - CORNER_WIDTH,
					frame.right, frame.bottom, paint);
			canvas.drawRect(frame.right - CORNER_WIDTH, frame.bottom - ScreenRate,
					frame.right, frame.bottom, paint);

			
			//�����м����,ÿ��ˢ�½��棬�м���������ƶ�SPEEN_DISTANCE
			slideTop += SPEEN_DISTANCE;
			if(slideTop >= (frame.bottom-CORNER_WIDTH)){
				slideTop = (frame.top+CORNER_WIDTH);
			}
			//canvas.drawRect(frame.left + MIDDLE_LINE_PADDING, slideTop - MIDDLE_LINE_WIDTH/2, frame.right - MIDDLE_LINE_PADDING,slideTop + MIDDLE_LINE_WIDTH/2, paint);
			
	        canvas.drawBitmap(mBitmap, frame.left, slideTop, btmPaint);
	       
			//��ɨ����������
			paint.setColor(Color.WHITE);
			paint.setTextSize(35);
			paint.setAlpha(0x40);
			paint.setTypeface(Typeface.create("System", Typeface.BOLD));
//			canvas.drawText(getResources().getString(R.string.scan_text1), frame.left, (float) (frame.bottom + (float)TEXT_PADDING_TOP *density), paint);
//			canvas.drawText(getResources().getString(R.string.scan_text2), frame.left, (float) (frame.bottom + (float)TEXT_PADDING_TOP *density), paint);
			Collection<ResultPoint> currentPossible = possibleResultPoints;
			Collection<ResultPoint> currentLast = lastPossibleResultPoints;
//			if (currentPossible.isEmpty()) {
//				lastPossibleResultPoints = null;
//			} else {
//				possibleResultPoints = new HashSet<ResultPoint>(5);
//				lastPossibleResultPoints = currentPossible;
//				paint.setAlpha(OPAQUE);
//				paint.setColor(resultPointColor);
//				for (ResultPoint point : currentPossible) {
//					canvas.drawCircle(frame.left + point.getX(), frame.top
//							+ point.getY(), 6.0f, paint);
//				}
//			}
//			if (currentLast != null) {
//				paint.setAlpha(OPAQUE / 2);
//				paint.setColor(resultPointColor);
//				for (ResultPoint point : currentLast) {
//					canvas.drawCircle(frame.left + point.getX(), frame.top
//							+ point.getY(), 3.0f, paint);
//				}
//			}

			
			//ֻˢ��ɨ�������ݣ������ط���ˢ��
			postInvalidateDelayed(ANIMATION_DELAY, frame.left, frame.top,
					frame.right, frame.bottom);
			
		}
	}
	private  Bitmap shorter(Bitmap bitmap,float len,float width) {
		  Matrix matrix = new Matrix();
		  matrix.postScale(len,width); //���Ϳ�Ŵ���С�ı���
		  Bitmap resizeBmp = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
		  return resizeBmp;
		 }
	public void drawViewfinder() {
		resultBitmap = null;
		invalidate();
	}

	/**
	 * Draw a bitmap with the result points highlighted instead of the live
	 * scanning display.
	 * 
	 * @param barcode
	 *            An image of the decoded barcode.
	 */
	public void drawResultBitmap(Bitmap barcode) {
		resultBitmap = barcode;
		invalidate();
	}

	public void addPossibleResultPoint(ResultPoint point) {
		possibleResultPoints.add(point);
	}

}
