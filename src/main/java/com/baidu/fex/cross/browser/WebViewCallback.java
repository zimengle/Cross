package com.baidu.fex.cross.browser;

import android.graphics.Bitmap;
import android.webkit.WebView;

public interface WebViewCallback {
	void onPageStarted(WebView view, String url, Bitmap favicon);

	void onPageFinished(WebView view, String url);
}
