package com.baidu.fex.cross.model;

public class Favorites {

	private int _id;
	
	private String title;
	
	private String intent;

	public Favorites(int _id, String title, String intent) {
		this._id = _id;
		this.title = title;
		this.intent = intent;
	}

	public int get_id() {
		return _id;
	}

	public String getTitle() {
		return title;
	}

	public String getIntent() {
		return intent;
	}
	
	
	
	
	
}
