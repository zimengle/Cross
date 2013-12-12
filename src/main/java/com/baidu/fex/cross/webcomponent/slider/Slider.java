package com.baidu.fex.cross.webcomponent.slider;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.fex.cross.browser.CrossWebView;
import com.baidu.fex.cross.webcomponent.WebComponentAbs;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;

public class Slider extends WebComponentAbs implements ImageLoadingListener {

	private SliderView sliderView;

	private RelativeLayout.LayoutParams params;

	public Slider(Context context, CrossWebView crossWebView) {
		super(context, crossWebView);
	}

	protected View addView(Context context) {
		sliderView = new SliderView(context);
		sliderView.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT));
		getContainer().addView(sliderView);
		params = (RelativeLayout.LayoutParams) sliderView.getLayoutParams();
		sliderView.setLayoutParams(params);
		sliderView.setOnSizeChangedListener(this);
		sliderView.setVisibility(View.INVISIBLE);
		sliderView.setImageLoadingListener(this);
		return sliderView;
	}

	public void updateAll(Bundle data) {
		super.updateAll(data);
		if (sliderView.getWidth() != 0) {
			updatePosition();
		}else{
			params.height = height;
			params.width = width;
			sliderView.setLayoutParams(params);
		}
		
		String imgUrls = data.getString("imgUrls");
		sliderView.setImages(imgUrls);
	}

	@Override
	protected RelativeLayout.LayoutParams getLayoutParams() {
		return params;
	}
	
	@Override
	public void dismiss() {
		super.dismiss();
		crossWebView.runJSMethod("onSliderNativeDismissed()");
	}

	public void onLoadingCancelled(String arg0, View arg1) {
		
	}

	public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
		crossWebView.runJSMethod("onSliderNativeInstalled()");
	}

	public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
		
	}

	public void onLoadingStarted(String arg0, View arg1) {
		
	}
	
}
