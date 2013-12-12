package com.baidu.fex.cross.browser.js;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class SliderJSObject implements InteractableJSObj{

	private Handler handler;
	
	private float ratio;
	
	private Bundle bundle = new Bundle();

	public SliderJSObject(Handler handler){
		this.handler = handler;
		bundle.putString("type", "Slider");
	}
	
	public void setPageRatio(float ratio) {
		this.ratio = ratio;
	}
	
	private void setBundle(int left, int top, int width, int height){
		bundle.putInt("width", (int) (width * ratio));
		bundle.putInt("height", (int) (height * ratio));
		bundle.putInt("left", (int) (left * ratio));
		bundle.putInt("top", (int) (top * ratio));
	}
	
	public void install(int left, int top, int width, int height, String imgUrls) {
		bundle.putString("imgUrls", imgUrls);
		setBundle(left, top, width, height);
		Message message = handler.obtainMessage();
		message.what = 1;
		message.setData(bundle);
		handler.sendMessage(message);
	}

	public void notifyDomChange(int left, int top, int width, int height) {
		setBundle(left, top, width, height);
		Message message = handler.obtainMessage();
		message.what = 2;
		message.setData(bundle);
		handler.sendMessage(message);
	}
	
	public void notifyRemoved(){
		Message message = handler.obtainMessage();
		message.what = 3;
		message.setData(bundle);
		handler.sendMessage(message);
	}
	
}
