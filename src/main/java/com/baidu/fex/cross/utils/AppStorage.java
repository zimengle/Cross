package com.baidu.fex.cross.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OptionalDataException;
import java.io.StreamCorruptedException;
import java.util.List;

import android.content.Context;

import com.baidu.fex.cross.adapter.AppGridAdapter.App;


public class AppStorage{

	private ObjectInputStream inputStream;
	
	private ObjectOutputStream outputStream;
	
	private static AppStorage instance;
	
	public synchronized static AppStorage getInstance(Context context) {
		if(instance == null){
			instance = new AppStorage(context);
		}
		return instance;
	}
	
	private AppStorage(Context context) {
		File file = new File(context.getExternalCacheDir(),".applight");
		
		try {
			if(!file.exists()){
				file.createNewFile();
			}
			outputStream = new ObjectOutputStream(new FileOutputStream(file));
			inputStream = new ObjectInputStream(new FileInputStream(file));
		} catch (StreamCorruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void save(List<App> apps){
		try {
			outputStream.writeObject(apps);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<App> get(){
		try {
			return (List<App>) inputStream.readObject();
		} catch (OptionalDataException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	
}
