package com.baidu.fex.cross;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.io.IOUtils;

import com.baidu.fex.cross.component.Album;
import com.baidu.fex.cross.component.Album.AlbumListener;
import com.baidu.fex.cross.model.AlbumResult;
import com.google.gson.Gson;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class AlbumFragment extends Fragment{

	private String url;
	
	private Album album;
	
	private Context mContext;
	

	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		album = new Album(mContext,(AlbumListener) getArguments().getSerializable("AlbumListener"));
		return album;
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getActivity();
		
		url = getArguments().getString("url");
		new AsyncTask<String, Void, AlbumResult>(){

			@Override
			protected AlbumResult doInBackground(String... params) {
				try {
					String html = IOUtils.toString(new URL(params[0]));
					Pattern pattern = Pattern.compile("var\\s+PD=(.*);_slideImage");
					Matcher matcher =  pattern.matcher(html);
					if(matcher.find(1)){
						String json = matcher.group(1);
						Gson gson = new Gson();
						AlbumResult albumResult = gson.fromJson(json, AlbumResult.class);
						return albumResult;
					}
				} catch (MalformedURLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				return null;
			}
			
			@Override
			protected void onPostExecute(AlbumResult result) {
				album.setAlbumResult(result);
			}
			
		}.execute(url);
		
	}
	
}
