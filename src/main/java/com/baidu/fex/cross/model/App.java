package com.baidu.fex.cross.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;


@DatabaseTable(tableName="App")
public class App implements Serializable{

	@DatabaseField
	private int appIcon;
	@DatabaseField
	private String shortcutName;
	@DatabaseField
	private String url;
	@DatabaseField(id=true)
	private String name;
	@DatabaseField
	private boolean isInstalled;
	@DatabaseField
	private boolean hasNewMsg;
	
	public App() {
		// TODO Auto-generated constructor stub
	}
	
	public App(int appIcon, String url, boolean isInstalled, String name,boolean hasNewMsg) {
		this.appIcon = appIcon;
		this.hasNewMsg = hasNewMsg;
		this.url = url;
		this.name = name;
		shortcutName = name;
		this.isInstalled = isInstalled;
	}
	
	
	public void setHasNewMsg(boolean hasNewMsg) {
		this.hasNewMsg = hasNewMsg;
	}
	
	public boolean isHasNewMsg() {
		return hasNewMsg;
	}
	
	public String getShortcutName() {
		return shortcutName;
	}
	
	public boolean isInstalled() {
		return isInstalled;
	}

	public void setInstalled(boolean isInstalled) {
		this.isInstalled = isInstalled;
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
