package com.hhyg.TyClosing.global;

import java.io.File;

import com.hhyg.TyClosing.config.Constants;
import com.nostra13.universalimageloader.cache.disc.impl.BarcodeFileGetter;
import com.nostra13.universalimageloader.cache.disc.impl.BrandFileGetter;
import com.nostra13.universalimageloader.cache.disc.impl.SpecialFileGetter;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;

public class ImageHelper {
	public static void LoaderInit(Context context){
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)  
		        .memoryCacheExtraOptions(400, 400) // default = device screen dimensions  
		        .threadPoolSize(5) // default  
		        .threadPriority(Thread.NORM_PRIORITY - 1) // default  
		        .tasksProcessingOrder(QueueProcessingType.FIFO) // default  
		        .denyCacheImageMultipleSizesInMemory()  
		        .diskCache(new UnlimitedDiskCache(new File(Constants.BASE_LOCAL_PATH_PIC))) // default  
		        .diskCacheSize(50 * 1024 * 1024)  
		        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default  
		        .imageDownloader(new BaseImageDownloader(context)) // default  
		        .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default   
		        .build();
		ImageLoader.getInstance().init(config);
	}

	public static DisplayImageOptions initBrandPathOption(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()   
		        .resetViewBeforeLoading(false)  // default  
		        .delayBeforeLoading(1000)
		        .diskCachePath(new BrandFileGetter())
		        .cacheInMemory(true) // default  
		        .cacheOnDisk(true) // default  
		        .considerExifParams(false) // default  
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT) 
		        .bitmapConfig(Bitmap.Config.RGB_565) // default  
		        .displayer(new SimpleBitmapDisplayer()) // default  
		        .handler(new Handler()) // default  
		        .build();
		return options;
	
	}
	
	public static DisplayImageOptions initCatPathOption(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()   
		        .resetViewBeforeLoading(false)  // default  
		        .delayBeforeLoading(1000)  
		        .cacheInMemory(true) // default  
		        .cacheOnDisk(true) // default  
		        .considerExifParams(false) // default  
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT) // default  
		        .bitmapConfig(Bitmap.Config.RGB_565) 
		        .displayer(new SimpleBitmapDisplayer()) // default  
		        .handler(new Handler()) // default  
		        .build();
		return options;
	}
	public static DisplayImageOptions initTitlePathOption(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()   
		        .resetViewBeforeLoading(false)  // default  
		        .delayBeforeLoading(1000)  
		        .cacheInMemory(true) // default  
		        .cacheOnDisk(true) // default  
		        .considerExifParams(false) // default  
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT) // default  
		        .bitmapConfig(Bitmap.Config.RGB_565)  
		        .displayer(new SimpleBitmapDisplayer()) // default  
		        .handler(new Handler()) // default  
		        .build();
		return options;
	}
	public static DisplayImageOptions initSpecialPathOption(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()   
		        .resetViewBeforeLoading(false)  // default  
		        .delayBeforeLoading(1000)
		        .diskCachePath(new SpecialFileGetter())
		        .cacheInMemory(true) // default  
		        .cacheOnDisk(true) // default  
		        .considerExifParams(false) // default  
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT) 
		        .bitmapConfig(Bitmap.Config.RGB_565) // default  
		        .displayer(new SimpleBitmapDisplayer()) // default  
		        .handler(new Handler()) // default  
		        .build();
		return options;
	}
	public static DisplayImageOptions initBarcodePathOption(){
		DisplayImageOptions options = new DisplayImageOptions.Builder()   
		        .resetViewBeforeLoading(false)  // default  
		        .delayBeforeLoading(1000)
		        .diskCachePath(new BarcodeFileGetter())
		        .cacheInMemory(true) // default  
		        .cacheOnDisk(true) // default  
		        .considerExifParams(false) // default  
		        .imageScaleType(ImageScaleType.IN_SAMPLE_INT) 
		        .bitmapConfig(Bitmap.Config.RGB_565) // default  
		        .displayer(new SimpleBitmapDisplayer()) // default  
		        .handler(new Handler()) // default  
		        .build();
		return options;
	}
}
