package com.baidu.fex.cross.browser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class CrossWebViewClient extends WebViewClient {
	

	private static final String JS_FILE_IN_ASSETS = "js.js";
	
	private CrossWebView webView;
	
	private Context context;

	public CrossWebViewClient(Context context, CrossWebView webView) {
		this.webView = webView;
		this.context = context;
	}

	private String loadInjectJS(){
		AssetManager assetManager = context.getAssets();
		String jString = "";
		try {
			InputStream is = assetManager.open(JS_FILE_IN_ASSETS);
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			String line = "";
			while ((line = br.readLine()) != null){
				jString += line;
			}
			
			is.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return "javascript:" + jString;
	}
	
	private void executeInjectJS(){
		String injectJString = loadInjectJS();
		webView.loadUrl(injectJString);
	}
	
	@Override
	public void onLoadResource(WebView view, String url) {
		super.onLoadResource(view, url);
	}

	@Override
	public void onPageStarted(WebView view, String url, Bitmap favicon) {
		System.out.println("page start...");
		webView.dismiss();
	}
	
	@Override
	public void onPageFinished(WebView view, String url) {
		System.out.println("page finish...");
		executeInjectJS();
	}
	
}
