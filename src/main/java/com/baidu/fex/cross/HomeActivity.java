package com.baidu.fex.cross;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.baidu.fex.cross.adapter.AppGridAdapter;
import com.baidu.fex.cross.adapter.AppGridAdapter.App;
import com.baidu.fex.cross.adapter.AppGridAdapter.OnItemClickListener;
import com.baidu.fex.cross.utils.AppStorage;
import com.baidu.fex.cross.utils.ShortcutUtils;

public class HomeActivity extends Activity {

	private GridView gridView;

	

	private AppGridAdapter gridAdapter;

	private Context mContext;

	private List<App> apps;
	
	private AppStorage appStorage;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;
		appStorage = AppStorage.getInstance(mContext);
		setContentView(R.layout.home);
		findViewById(R.id.home_tab_app_page).setSelected(true);
		apps = appStorage.get();
		if(apps == null){
			apps = new ArrayList<App>() {
				{
					add(new App(mContext, R.drawable.app_tieba_icon,
							"http://tieba.baidu.com", "百度贴吧",false));
					add(new App(mContext, R.drawable.app_zhidao_icon,
							"http://zhidao.baidu.com", "百度知道",false));
					add(new App(mContext, R.drawable.app_wenku_icon, "http://wk.baidu.com", "百度文库",false));
				}
			};
		}
		gridAdapter = new AppGridAdapter(this, apps);
		gridAdapter.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(View view, int position) {
				App app = apps.get(position);
				switch (view.getId()) {
				case R.id.icon:
					openApp(app.getUrl());
					break;
				case R.id.btn_install:
					ShortcutUtils.createShortcut(mContext,app);
					app.setInstalled(true);
					appStorage.save(apps);
					gridAdapter.notifyDataSetChanged();
					break;
				}
			}
		});
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(gridAdapter);
		findViewById(R.id.home_tab_msg).setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				App app = apps.get(0);
				app.setHasNewMsg(true);
				ShortcutUtils.updateShortcut(mContext, app);
				
//				ShortcutUtils.createShortcut(mContext, app);
				gridAdapter.notifyDataSetChanged();
			}
		});
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
