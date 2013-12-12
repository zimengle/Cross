package com.baidu.fex.cross.browser;

import android.app.Activity;
import android.content.Context;
import android.webkit.ConsoleMessage;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

public class CrossChromeClient extends WebChromeClient {

	private Context context;

	public CrossChromeClient(Context context) {
		this.context = context;
	}

	@Override
	public void onProgressChanged(WebView view, int newProgress) {
		((Activity) context).setProgress(newProgress * 100);
	}

	@Override
	public void onReceivedTitle(WebView view, String title) {
		super.onReceivedTitle(view, title);
		((Activity) context).setTitle(title);
	}

	@Override
	public boolean onConsoleMessage(ConsoleMessage consoleMessage) {
		System.out.println("js console: " + consoleMessage.message());
		return true;
	}

}
