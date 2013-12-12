package com.baidu.fex.cross.webcomponent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.baidu.fex.cross.R;
import com.baidu.fex.cross.browser.CrossWebView;

public abstract class WebComponentAbs implements WebComponent, OnSizeChangedListener {

	protected CrossWebView crossWebView;

	private View nativeView;

	private ViewGroup container;

	protected int width;

	protected int height;

	protected int left;

	protected int top;

	private RelativeLayout.LayoutParams params;

	public WebComponentAbs(Context context, CrossWebView crossWebView) {
		this.crossWebView = crossWebView;

		container = (RelativeLayout) ((Activity) context)
				.findViewById(R.id.browser_container);

		nativeView = addView(context);
		params = getLayoutParams();
	}

	abstract protected View addView(Context context);

	abstract protected RelativeLayout.LayoutParams getLayoutParams();

	public void updatePosition() {
		params.leftMargin = left - crossWebView.getScrollX() + width
				- nativeView.getWidth();
		params.topMargin = top - crossWebView.getScrollY()
				+ (height - nativeView.getHeight()) / 2;
		nativeView.setLayoutParams(params);
	}

	public void updateAll(Bundle data) {
		left = data.getInt("left");
		top = data.getInt("top");
		width = data.getInt("width");
		height = data.getInt("height");
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {

	}

	public void updateSize(int width, int height) {

	}
	
	public ViewGroup getContainer(){
		return container;
	}
	
	public void dismiss() {
		container.removeView(nativeView);
	}

	public void sizeChanged(int width, int height) {
		new Handler().post(new Runnable() {

			public void run() {
				updatePosition();
			}
		});
		nativeView.setVisibility(View.VISIBLE);
	}
}
