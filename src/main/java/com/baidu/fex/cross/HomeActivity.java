package com.baidu.fex.cross;

import java.util.List;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.GridView;

import com.baidu.fex.cross.adapter.AppGridAdapter;
import com.baidu.fex.cross.adapter.AppGridAdapter.OnItemClickListener;
import com.baidu.fex.cross.dao.DatabaseHelper;
import com.baidu.fex.cross.model.App;
import com.baidu.fex.cross.utils.AppUtils;
import com.baidu.fex.cross.utils.NotificationUtils;
import com.baidu.fex.cross.utils.NotificationUtils.NotificationItem;
import com.baidu.fex.cross.utils.ShortcutUtils;
import com.j256.ormlite.dao.RuntimeExceptionDao;

public class HomeActivity extends BaseActivity<DatabaseHelper> {

	private GridView gridView;

	private AppGridAdapter gridAdapter;

	private Context mContext;

	private List<App> apps;
	
	private RuntimeExceptionDao<App, String> dao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		mContext = this;
		
		dao = getHelper().getSimpleDataDao();
		
		setContentView(R.layout.home);
		findViewById(R.id.home_tab_app_page).setSelected(true);
		apps = dao.queryForAll();
		gridAdapter = new AppGridAdapter(this, apps){
			@Override
			public void notifyDataSetChanged() {
				
				super.notifyDataSetChanged();
			}
		};
		gridAdapter.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(View view, int position) {
				App app = apps.get(position);
				switch (view.getId()) {
				case R.id.icon:
					if(app.isHasNewMsg()){
						app.setHasNewMsg(false);
						ShortcutUtils.updateShortcut(mContext, app);
						dao.update(app);
						gridAdapter.notifyDataSetChanged();
					}
					openApp(app.getUrl());
					break;
				case R.id.btn_install:
					ShortcutUtils.createShortcut(mContext,app);
					app.setInstalled(true);
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
				
				NotificationUtils.build(mContext, new NotificationItem(R.drawable.msg, "百度贴吧", "1条新消息,点击查看", ShortcutUtils.generateShorcut(mContext, app.getAppIcon()),  "http://tieba.baidu.com/i/400304304/replyme"));
				if(!app.isHasNewMsg()){
					app.setHasNewMsg(true);
					ShortcutUtils.updateShortcut(mContext, app);
					dao.update(app);
					gridAdapter.notifyDataSetChanged();
				}
				
				
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
		checkChange();
	}
	
	protected void checkChange() {
		new AsyncTask<Void, Void, Void>(){

			@Override
			protected Void doInBackground(Void... params) {
				
				for(App app : apps){
					boolean ischange = false;
					App _app = dao.queryForId(app.getName());
					if(_app != null && _app.isHasNewMsg() != app.isHasNewMsg()){
						app.setHasNewMsg(_app.isHasNewMsg());
						ischange = true;
					}
					boolean installed = AppUtils.checkInstall(mContext, app);
					if(installed != app.isInstalled()){
						app.setInstalled(installed);
						ischange = true;
					}
					if(ischange){
						dao.update(app);
					}
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
