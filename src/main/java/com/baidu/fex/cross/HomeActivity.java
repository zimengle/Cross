package com.baidu.fex.cross;

import java.util.ArrayList;
import java.util.List;

import com.baidu.fex.cross.HomeActivity.AppGridAdapter.App;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeActivity extends Activity{
	
	private GridView gridView;
	
	private List<App> apps = new ArrayList<App>(){{
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
		add(new App(R.drawable.app_tieba_icon, "http://tieba.baidu.com", "贴吧"));
	}};
	
	private AppGridAdapter gridAdapter;
	
	private Context mContext;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		setContentView(R.layout.home);
		findViewById(R.id.home_tab_app_page).setSelected(true);
		gridAdapter = new AppGridAdapter(this, apps);
		gridView = (GridView) findViewById(R.id.gridview);
		gridView.setAdapter(gridAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				App app = apps.get(position);
				String url = app.url;
				Intent intent = new Intent(mContext, BrowserActivity.class);
				intent.putExtra("url", url);
				mContext.startActivity(intent);
			}
			
		});
	}
	
	public static class AppGridAdapter extends BaseAdapter{

		public static class App{
			private int appIcon;
			private String url;
			private String name;
			public App(int appIcon, String url, String name) {
				this.appIcon = appIcon;
				this.url = url;
				this.name = name;
			}

			public int getAppIcon() {
				return appIcon;
			}

			public String getUrl() {
				return url;
			}
			public String getName() {
				return name;
			}
		}
		
		private List<App> list;
		
		private Context context;
		
		public AppGridAdapter(Context context,List<App> list) {
			this.list = list;
			this.context = context;
		}

		public int getCount() {
			return list.size();
		}

		public Object getItem(int position) {
			return list.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}
		
		public View getView(int position, View convertView, ViewGroup parent) {
			AppViewHolder holder;
			if(convertView == null){
				holder = new AppViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.app, null);
				holder.icon = (ImageView) convertView.findViewById(R.id.icon);
				holder.title = (TextView) convertView.findViewById(R.id.title);
				convertView.setTag(holder);
			}else{
				holder = (AppViewHolder) convertView.getTag();
			}
			App app = list.get(position);
			holder.icon.setImageResource(app.appIcon);
			holder.title.setText(app.name);
			return convertView;
		}
		
		public static class AppViewHolder{
			public ImageView icon;
			public TextView title;
		}
		
	}


}
