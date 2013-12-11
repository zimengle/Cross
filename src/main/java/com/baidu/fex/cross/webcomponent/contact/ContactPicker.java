package com.baidu.fex.cross.webcomponent.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.fex.cross.R;
import com.baidu.fex.cross.browser.CrossWebView;
import com.baidu.fex.cross.webcomponent.WebComponent;
import com.baidu.fex.cross.webcomponent.contact.ContactPickerView.OnSizeChangedListener;

public class ContactPicker implements WebComponent, OnSizeChangedListener {

	private ContactPickerView contactPickerView;

	private RelativeLayout.LayoutParams params;

	private int width;

	private int height;

	private int left;

	private int top;

	private CrossWebView crossWebView;

	private RelativeLayout wrapper;

	public ContactPicker(Context context, CrossWebView crossWebView) {
		addView(context);
		this.crossWebView = crossWebView;
	}

	private void addView(Context context) {
		contactPickerView = new ContactPickerView(context);
		contactPickerView.setOnSizeChangedListener(this);
		contactPickerView.setVisibility(View.INVISIBLE);
		contactPickerView.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT));
		wrapper = (RelativeLayout) ((Activity) context)
				.findViewById(R.id.browser_container);
		wrapper.addView(contactPickerView);
		params = (RelativeLayout.LayoutParams) contactPickerView
				.getLayoutParams();
		contactPickerView.setLayoutParams(params);
	}

	public void updateSize(int width, int height) {

	}

	public void updatePosition() {
		params.leftMargin = left - crossWebView.getScrollX() + width
				- contactPickerView.getWidth();
		params.topMargin = top - crossWebView.getScrollY()
				+ (height - contactPickerView.getHeight()) / 2;
		contactPickerView.setLayoutParams(params);
	}

	public void updateAll(Bundle data) {
		left = data.getInt("left");
		top = data.getInt("top");
		width = data.getInt("width");
		height = data.getInt("height");
		
		if (contactPickerView.getWidth() != 0) {
			updatePosition();
		}
		
	}

	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == Activity.RESULT_OK) {
			if (requestCode == ContactPickerView.CONTACT_PICKER_REQUEST_CODE) {
				String phoneNum = contactPickerView.getPickResult(data);
				crossWebView.runJSMethod("setContactInputValue(" + phoneNum
						+ ")");
			}
		}
	}

	public void dismiss() {
		wrapper.removeView(contactPickerView);
	}

	public void sizeChanged(int width, int height) {
		new Handler().post(new Runnable() {
			
			public void run() {
				updatePosition();
			}
		});
		contactPickerView.setVisibility(View.VISIBLE);
		System.out.println("sizeChanged");
	}

}
