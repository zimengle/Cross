package com.baidu.fex.cross;



import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class ImageSliderActivity extends Activity{
	
	private WebView webView;
	
	private static final int REQUEST_ALBUM = 1;
	
	@SuppressLint("SetJavaScriptEnabled")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		webView = new WebView(this);
		webView.getSettings().setJavaScriptEnabled(true);
		webView.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
		webView.setWebChromeClient(new WebChromeClient() {
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
		webView.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				Log.d("MyApplication", "onPageFinished");
			}
			@Override
			public void onPageStarted(WebView view, String url, Bitmap favicon) {
				if(url.startsWith("http://tieba.baidu.com/mo/q/album")){
					view.stopLoading();
					Intent intent = new Intent(ImageSliderActivity.this, AlbumActivity.class);
					intent.putExtra("url", url);
					startActivityForResult(intent, REQUEST_ALBUM);
				}
			}
		});
		
		webView.loadUrl("http://tieba.baidu.com/");
		setContentView(webView,new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT));
		
	}
	
	
}
