package com.baidu.fex.cross;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.fex.cross.browser.CrossWebView;
import com.baidu.fex.cross.browser.WebViewCallback;

public class WebViewFragment extends Fragment{

	private Context mContext;
	
	
	private CrossWebView mWebView;

	
	private WebViewCallback webViewCallback;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
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
		
		mWebView = new CrossWebView(mContext, webViewCallback);
		mWebView.loadUrl(getArguments().getString("url"));
		return mWebView;
	}
	
	public boolean goBack(){
		boolean canGoback = mWebView.canGoBack();
		if(canGoback){
			mWebView.goBack();
		}
		return canGoback;
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		mWebView.onActivityResult(requestCode, resultCode, data);
	}

}
