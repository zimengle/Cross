package com.baidu.fex.cross;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.GridView;

import com.baidu.fex.cross.adapter.AppGridAdapter;
import com.baidu.fex.cross.adapter.AppGridAdapter.App;
import com.baidu.fex.cross.adapter.AppGridAdapter.OnItemClickListener;
import com.baidu.fex.cross.utils.ShortcutUtils;

public class HomeActivity extends Activity {

	private GridView gridView;

	

	private AppGridAdapter gridAdapter;

	private Context mContext;

	private List<App> apps;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;
		setContentView(R.layout.home);
		findViewById(R.id.home_tab_app_page).setSelected(true);
		apps = new ArrayList<App>() {
			{
				add(new App(mContext, R.drawable.app_tieba_icon,
						"http://tieba.baidu.com", "贴吧"));
				add(new App(mContext, R.drawable.app_zhidao_icon,
						"http://zhidao.baidu.com", "知道"));
			}
		};
		gridAdapter = new AppGridAdapter(this, apps);
		gridAdapter.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(View view, int position) {
				App app = apps.get(position);
				switch (view.getId()) {
				case R.id.icon:
					openApp(app.getUrl());
					break;
				case R.id.btn_install:
					ShortcutUtils.createShortcut(mContext,app.getUrl(), app.getShortcutName(),app.getAppIcon());
					app.setInstalled(true);
					gridAdapter.notifyDataSetChanged();
					break;
				}
			}
		});
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(gridAdapter);
	}

	public void openApp(String url) {

		Intent intent = new Intent(mContext, BrowserActivity.class);
		intent.putExtra("url", url);
		mContext.startActivity(intent);
	}

	@Override
	protected void onResume() {
		super.onResume();
		checkInstalled();
	}
	
	protected void checkInstalled(){
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				for(App app : apps){
					app.checkInstall();
				}
				return null;
			}
			
			@Override
			protected void onPostExecute(Void result) {
				gridAdapter.notifyDataSetChanged();
			}
			
		}.execute();
	}

}
