package com.baidu.fex.cross.utils;



import android.content.Context;


import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.DisplayImageOptions.Builder;

public class Utils {

	private static ImageLoader imageLoader = null;
	
	public synchronized static ImageLoader getImageLoader(Context context){
		if(imageLoader == null){
			DisplayImageOptions options = new DisplayImageOptions.Builder()
					.cacheInMemory(true)
					.cacheOnDisc(true)
					.build();
			ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
					.defaultDisplayImageOptions(options)
			        .build();
			imageLoader = ImageLoader.getInstance();
			imageLoader.init(config);
		}
		
		return imageLoader;
		
		
		
		
	}
	
}
