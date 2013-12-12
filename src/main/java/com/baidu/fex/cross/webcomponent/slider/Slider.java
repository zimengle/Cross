package com.baidu.fex.cross.webcomponent.slider;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.fex.cross.browser.CrossWebView;
import com.baidu.fex.cross.webcomponent.WebComponentAbs;

public class Slider extends WebComponentAbs {

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

}
