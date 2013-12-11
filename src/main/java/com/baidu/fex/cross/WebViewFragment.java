package com.baidu.fex.cross;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class WebViewFragment extends Fragment{

	private Context mContext;
	
	
	private WebView mWebView;
	
	public static interface WebViewCallback{
		public void onPageStarted(WebView view, String url, Bitmap favicon);
		public void onPageFinished(WebView view, String url);
	}
	
	private WebViewCallback webViewCallback;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = getActivity();
	}
	
	public void setWebViewCallback(WebViewCallback webViewCallback) {
		this.webViewCallback = webViewCallback;
	}
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mWebView = new WebView(mContext);
		mWebView.getSettings().setJavaScriptEnabled(true);
		mWebView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		mWebView.setWebChromeClient(new WebChromeClient() {
			@Override
			public boolean onConsoleMessage(ConsoleMessage cm) {
				Log.d("MyApplication",
						cm.message() + " -- From line " + cm.lineNumber()
								+ " of " + cm.sourceId());
				return true;
			}

			@Override
			public void onShowCustomView(View view, CustomViewCallback callback) {
				Log.d("MyApplication", "onShowCustomView:" + view);
			}

			@Override
			public void onHideCustomView() {
				Log.d("MyApplication", "onHideCustomView");
			}
		});
		
		mWebView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				if(webViewCallback != null){
					webViewCallback.onPageFinished(view, url);
				}
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if(webViewCallback != null){
					webViewCallback.onPageStarted(view, url, favicon);
				}
			}
		});
		
		mWebView.loadUrl(getArguments().getString("url"));
//		container.addView(mWebView, new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		return mWebView;
	}
	
	public boolean goBack(){
		boolean canGoback = mWebView.canGoBack();
		if(canGoback){
			mWebView.goBack();
		}
		return canGoback;
	}
	
	


}
