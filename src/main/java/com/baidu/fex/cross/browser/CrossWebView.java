package com.baidu.fex.cross.browser;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;

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

@SuppressLint("SetJavaScriptEnabled")
public class CrossWebView extends WebView implements WebComponent{

	private Context context;
	
	private WebViewCallback webViewCallback;
	
	private ArrayList<WebComponent> webComponents = new ArrayList<WebComponent>();

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
	
	public CrossWebView(Context mContext, WebViewCallback webViewCallback) {
		this(mContext);
		this.webViewCallback = webViewCallback;
	}

	private void init(Context context) {
		this.context = context;
		if(webViewCallback == null){
			webViewCallback = new WebViewCallbackImpl();
		}
		getSettings().setJavaScriptEnabled(true);
		getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		
		setWebChromeClient(new CrossChromeClient(context));
		setWebViewClient(new CrossWebViewClient(context, this){
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				super.onPageStarted(view, url, favicon);
				webViewCallback.onPageStarted(view, url, favicon);
			}
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				webViewCallback.onPageFinished(view, url);
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
		switch (msg.what) {
		case 1:
			WebComponent webComponent = new ContactPicker(context, this);
			addWebComponent(webComponent);
			webComponent.updateAll(data);
			break;
		case 2:
			updateAll(data);
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
