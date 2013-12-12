package com.baidu.fex.cross.webcomponent.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import com.baidu.fex.cross.browser.CrossWebView;
import com.baidu.fex.cross.webcomponent.WebComponentAbs;

public class ContactPicker extends WebComponentAbs {

	private ContactPickerView contactPickerView;

	private RelativeLayout.LayoutParams params;

	public ContactPicker(Context context, CrossWebView crossWebView) {
		super(context, crossWebView);
	}

	protected View addView(Context context) {
		contactPickerView = new ContactPickerView(context);
		contactPickerView.setOnSizeChangedListener(this);
		contactPickerView.setVisibility(View.INVISIBLE);
		contactPickerView.setLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT));
		getContainer().addView(contactPickerView);
		params = (RelativeLayout.LayoutParams) contactPickerView
				.getLayoutParams();
		contactPickerView.setLayoutParams(params);
		
		return contactPickerView;
	}

	public void updateAll(Bundle data) {
		super.updateAll(data);
		
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

	@Override
	protected RelativeLayout.LayoutParams getLayoutParams() {
		return params;
	}

}
