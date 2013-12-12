package com.baidu.fex.cross.webcomponent.slider;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.baidu.fex.cross.R;
import com.baidu.fex.cross.webcomponent.OnSizeChangedListener;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

public class SliderView extends ViewPager {

	private String[] imgUrls = null;

	private ImageView[] imageViewList;

	private Context context;

	private ImageLoader imageLoader = ImageLoader.getInstance();

	private OnSizeChangedListener onSizeChangedListener;

	public SliderView(Context context) {
		super(context);
		this.context = context;
		DisplayImageOptions displayImageOptions = new DisplayImageOptions.Builder()
				.cacheInMemory(true).imageScaleType(ImageScaleType.NONE)
				.build();
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				context).memoryCache(new WeakMemoryCache())
				.defaultDisplayImageOptions(displayImageOptions).build();
		ImageLoader.getInstance().init(config);
	}

	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
	}

	public void setImages(String imgUrlsString) {
		if (imgUrls == null) {
			imgUrls = imgUrlsString.split(";");
			imageViewList = new ImageView[imgUrls.length];
			setAdapter(new SliderViewAdapter());
		}
	}

	class SliderViewAdapter extends PagerAdapter {

		@Override
		public Object instantiateItem(View container, int position) {
			View view = LayoutInflater.from(context).inflate(
					R.layout.webcomponent_slider_image_item, null);
			ImageView imageView = (ImageView) view.findViewById(R.id.image);
			imageViewList[position] = imageView;
			imageLoader.displayImage(imgUrls[position], imageView);
			((ViewPager) container).addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViewList[position]);
		}

		@Override
		public int getCount() {
			return imgUrls.length;
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}
	}
	
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		if(onSizeChangedListener != null){
			onSizeChangedListener.sizeChanged(w, h);
		}
	}
	
	public void setOnSizeChangedListener(OnSizeChangedListener onSizeChangedListener){
		this.onSizeChangedListener = onSizeChangedListener;
	}

}
