package com.baidu.fex.cross.browser;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.webkit.WebView;

import com.baidu.fex.cross.browser.js.JSObject;
import com.baidu.fex.cross.webcomponent.WebComponent;
import com.baidu.fex.cross.webcomponent.contact.ContactPicker;
import com.baidu.fex.cross.webcomponent.slider.Slider;

@SuppressLint("SetJavaScriptEnabled")
public class CrossWebView extends WebView implements WebComponent{

	private Context context;
	
	private List<WebViewCallback>  webViewCallbacks = new ArrayList<WebViewCallback>();
	
	private ArrayList<WebComponent> webComponents = new ArrayList<WebComponent>();

	protected boolean loaded;

	public CrossWebView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context);
	}

	public CrossWebView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public CrossWebView(Context context) {
		super(context);
		init(context);
	}

	public void addWebviewCallback(WebViewCallback webViewCallback){
		webViewCallbacks.add(webViewCallback);
	}
	
	public void removeWebviewCallback(WebViewCallback webViewCallback){
		webViewCallbacks.remove(webViewCallback);
	}
	
	protected void fireOnPageStart(WebView view, String url, Bitmap favicon) {
		for(WebViewCallback callback:webViewCallbacks){
			callback.onPageStarted(view, url, favicon);
		}
	}
	
	protected void fireOnPageFinished(WebView view, String url) {
		for(WebViewCallback callback:webViewCallbacks){
			callback.onPageFinished(view, url);
		}
	}
	
	private void init(Context context) {
		this.context = context;
		getSettings().setJavaScriptEnabled(true);
		getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		getSettings().setDomStorageEnabled(true);
		
		setWebChromeClient(new CrossChromeClient(context));
		setWebViewClient(new CrossWebViewClient(context, this){
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				fireOnPageStart(view, url, favicon);
				loaded = false;
			}
			
			@Override
			public void onPageFinished(WebView view, String url) {
				if (!loaded) {
					super.onPageFinished(view, url);
					fireOnPageFinished(view, url);
					loaded = true;
				}
			}

		});
		
		// 增加 native 能力
		enhance();
	}
	
	public void enhance(){
		Handler webViewHandler = new WebViewHandler(this);
		addJavascriptInterface(new JSObject(webViewHandler), JSObject.NAMESPACE);
	}
	
	@Override
	protected void onScrollChanged(int l, int t, int oldl, int oldt) {
		super.onScrollChanged(l, t, oldl, oldt);
		updatePosition();
	}
	
	private void onGetMessageFromPage(Message msg) {
		Bundle data = msg.getData();
		String type = data.getString("type");
		switch (msg.what) {
		case 1:
			if(type.equals("Contact")){
				WebComponent webComponent = new ContactPicker(context, this);
				addWebComponent(webComponent);
				webComponent.updateAll(data);
			} else if(type.equals("Slider")){
				WebComponent webComponent = new Slider(context, this);
				addWebComponent(webComponent);
				webComponent.updateAll(data);
			}
			break;
		case 2:
			updateAll(data);
			break;
		case 3:
			dismiss();
			break;
		}
	}
	
	private static class WebViewHandler extends Handler {

		private final WeakReference<CrossWebView> mWebView;

		public WebViewHandler(CrossWebView webView) {
			mWebView = new WeakReference<CrossWebView>(webView);
		}

		@Override
		public void handleMessage(Message msg) {
			CrossWebView webView = mWebView.get();
			if (webView != null) {
				webView.onGetMessageFromPage(msg);
			}
		}
	}
	public void addWebComponent(WebComponent webComponent) {
		webComponents.add(webComponent);
	}

	public void updatePosition() {
		for (WebComponent webComponent : webComponents) {
			webComponent.updatePosition();
		}
	}

	public void updateSize(int width, int height) {
		for (WebComponent webComponent : webComponents) {
			webComponent.updateSize(width, height);
		}
	}

	public void updateAll(Bundle data) {
		for (WebComponent webComponent : webComponents) {
			webComponent.updateAll(data);
		}
	}

	public void runJSMethod(String method){
		loadUrl("javascript:JSInterface."+method+";");
	}
	
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		for (WebComponent webComponent : webComponents) {
			webComponent.onActivityResult(requestCode, resultCode, data);
		}
	}

	public void dismiss() {
		Iterator<WebComponent> it = webComponents.iterator();
		while (it.hasNext()) {
			WebComponent webComponent = it.next();
			webComponent.dismiss();
			it.remove();
		}
	}
	
}
