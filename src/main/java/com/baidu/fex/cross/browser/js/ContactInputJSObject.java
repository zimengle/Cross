package com.baidu.fex.cross.browser.js;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;


public class ContactInputJSObject implements InteractableJSObj{

	private Handler handler;

	private float ratio;
	
	private Bundle bundle = new Bundle();

	public ContactInputJSObject(Handler handler){
		this.handler = handler;
	}
	
	private void setBundle(int left, int top, int width, int height){
		bundle.putInt("width", (int) (width * ratio));
		bundle.putInt("height", (int) (height * ratio));
		bundle.putInt("left", (int) (left * ratio));
		bundle.putInt("top", (int) (top * ratio));
	}
	
	public void install(int left, int top, int width, int height) {
		setBundle(left, top, width, height);
		Message message = handler.obtainMessage();
		message.what = 1;
		message.setData(bundle);
		handler.sendMessage(message);
	}

	public void setPageRatio(float ratio) {
		this.ratio = ratio;
	}

	public void notifyDomChange(int left, int top, int width, int height) {
		setBundle(left, top, width, height);
		Message message = handler.obtainMessage();
		message.what = 2;
		message.setData(bundle);
		handler.sendMessage(message);
	}

	
}
