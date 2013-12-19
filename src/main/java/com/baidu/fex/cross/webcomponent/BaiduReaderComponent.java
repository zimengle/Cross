package com.baidu.fex.cross.webcomponent;

import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.io.IOUtils;

import com.baidu.fex.cross.R;
import com.baidu.fex.cross.browser.CrossWebView;
import com.baidu.fex.cross.browser.WebViewCallback;
import com.baidu.fex.cross.model.App;
import com.baidu.fex.cross.utils.ShortcutUtils;
import com.baidu.fex.cross.utils.Utils;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageLoadingListener;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.core.assist.SimpleImageLoadingListener;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Toast;

public class BaiduReaderComponent implements WebViewCallback{

	private CrossWebView webView;
	
	private Context context;

	private String type;
	
	private boolean loaded = false;
	
	public BaiduReaderComponent(final Context context,CrossWebView webView,String type) {
		this.type = type;
		
		this.context = context;
		this.webView = webView;
		this.webView.addWebviewCallback(this);
		this.webView.addJavascriptInterface(new Object(){
			@SuppressWarnings("unused")
			public void addDesktop(String json){
				Gson gson = new Gson();
				HashMap<String,String> map = gson.fromJson(json, HashMap.class);
				final String img = map.get("img");
				final String title = map.get("title");
				final String url = map.get("url");
				Bitmap bitmap = Utils.getImageLoader(context).loadImageSync(img,new ImageSize(96, 96));
				ShortcutUtils.createShortcut(context,title,url,bitmap,"yuedu");
				Toast.makeText(context, "已将书籍添加到桌面",
						Toast.LENGTH_SHORT).show();
				
				
				
			}
		}, "cross_yuedu_native");
	}
	
	private String loadInjectJS() throws IOException{
		return "javascript:"+IOUtils.toString(webView.getResources().getAssets().open("reader.js"));
	}

	public void onPageStarted(WebView view, String url, Bitmap favicon) {
	}

	public void onPageFinished(WebView view, String url) {
		if(!loaded && "yuedu".equals(type)){
			webView.loadUrl("javascript:var url = $('[data-fun=\"try-readBook\"]').attr(\"data-href\");if(/^http:\\/\\/yd\\.baidu\\.com\\/ebook/.test(location.href)){location.href=url}");
			loaded = true;
			return;
		}
		if(url.startsWith("http://yd.baidu.com/ebook/")){
			try {
				webView.loadUrl(loadInjectJS());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	

	
	
}
