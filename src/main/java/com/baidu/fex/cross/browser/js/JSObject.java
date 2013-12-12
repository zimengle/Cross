package com.baidu.fex.cross.browser.js;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import android.os.Handler;

public class JSObject {

	public static final String NAMESPACE = "AndroidEnvironment";

	private Handler handler;

	private float pageRatio;
	
	private String packageName = JSObject.class.getPackage().getName();

	public JSObject(Handler handler) {
		this.handler = handler;
	}

	public void setPageRatio(float pageRatio){
		this.pageRatio = pageRatio;
	}
	
	public float getPageRatio(){
		return pageRatio;
	}
	
	public InteractableJSObj getComponent(String componentName){
		String componentClassName = packageName+"."+componentName+"JSObject";
		InteractableJSObj instance = null;
		try {
			Class<? extends InteractableJSObj> interactableJSObjClass = Class.forName(componentClassName).asSubclass(InteractableJSObj.class);
			Constructor<? extends InteractableJSObj> constructor = interactableJSObjClass.getConstructor(Handler.class);
			instance = constructor.newInstance(handler);
			instance.setPageRatio(pageRatio);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		
		return instance;
	}
	
}
