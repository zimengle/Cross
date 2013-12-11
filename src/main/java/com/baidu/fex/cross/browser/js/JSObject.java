package com.baidu.fex.cross.browser.js;

import android.os.Handler;

public class JSObject {

	public static final String NAMESPACE = "AndroidEnvironment";

	private Handler handler;

	private float pageRatio;

	public JSObject(Handler handler) {
		this.handler = handler;
	}

	public void setPageRatio(float pageRatio){
		this.pageRatio = pageRatio;
	}
	
	public float getPageRatio(){
		return pageRatio;
	}
	
	public InteractableJSObj getContactInput() {
		InteractableJSObj contactInputJSObject = new ContactInputJSObject(handler);
		contactInputJSObject.setPageRatio(pageRatio);
		return contactInputJSObject;
	}

	
	
}
