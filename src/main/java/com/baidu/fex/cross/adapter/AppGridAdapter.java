package com.baidu.fex.cross.adapter;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import com.baidu.fex.cross.R;
import com.baidu.fex.cross.model.App;
import com.baidu.fex.cross.utils.AppUtils;
import com.baidu.fex.cross.utils.ShortcutUtils;

public class AppGridAdapter extends BaseAdapter{

	public static interface OnItemClickListener {
		public void onItemClick(View view,int position);
	}
	
	private OnItemClickListener onItemClickListener;
	
	public void setOnItemClickListener(
			OnItemClickListener onItemClickListener) {
		this.onItemClickListener = onItemClickListener;
	}
	
	
	private List<App> list;

	private Context context;

	public AppGridAdapter(Context context, List<App> list) {
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

	public View getView(final int position, View convertView, ViewGroup parent) {
		AppViewHolder holder;
		if (convertView == null) {
			holder = new AppViewHolder();
			convertView = LayoutInflater.from(context).inflate(
					R.layout.app, null);
			holder.icon = (ImageButton) convertView.findViewById(R.id.icon);
			holder.icon.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					if(onItemClickListener != null){
						onItemClickListener.onItemClick(v, position);
					}
					
				}
			});
			holder.title = (TextView) convertView.findViewById(R.id.title);
			holder.installBtn = convertView.findViewById(R.id.btn_install);
			holder.installBtn.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					if(onItemClickListener != null){
						onItemClickListener.onItemClick(v, position);
					}
				}
			});
			holder.installedBtn = convertView.findViewById(R.id.btn_installed);
			convertView.setTag(holder);
		} else {
			holder = (AppViewHolder) convertView.getTag();
		}
		App app = list.get(position);
		holder.icon.setImageBitmap(AppUtils.getIcon(context, app));
		holder.title.setText(app.getName());
		if(app.isInstalled()){
			holder.installBtn.setVisibility(View.GONE);
			holder.installedBtn.setVisibility(View.VISIBLE);
		}else{
			holder.installBtn.setVisibility(View.VISIBLE);
			holder.installedBtn.setVisibility(View.GONE);
		}
		
		return convertView;
	}

	public static class AppViewHolder {
		public ImageButton icon;
		public TextView title;
		public View installBtn;
		public View installedBtn;
	}

}
