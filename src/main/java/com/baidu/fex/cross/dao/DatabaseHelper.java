package com.baidu.fex.cross.dao;

import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.baidu.fex.cross.R;
import com.baidu.fex.cross.model.App;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

	private static final String DATABASE_NAME = "com.baidu.fex.cross.db";
	
	private static final int DATABASE_VERSION = 1;
	
	private Dao<App, String> simpleDao = null;
	
	private RuntimeExceptionDao<App, String> simpleRuntimeDao = null;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTableIfNotExists(connectionSource, App.class);
			RuntimeExceptionDao<App,String> dao = getSimpleDataDao();
			dao.createIfNotExists(new App(R.drawable.app_tieba_icon,
					"http://tieba.baidu.com",false,"百度贴吧",false));
			dao.createIfNotExists(new App(R.drawable.app_zhidao_icon,
					"http://zhidao.baidu.com",false, "百度知道",false));
			dao.createIfNotExists(new App(R.drawable.app_wenku_icon, "http://wk.baidu.com",false, "百度文库",false));
			dao.createIfNotExists(new App(R.drawable.app_yuedu_icon, "http://yd.baidu.com",false, "百度阅读",false));
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, App.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(database, connectionSource);
		} catch (SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
	
	public Dao<App, String> getDao() throws SQLException {
		if (simpleDao == null) {
			simpleDao = getDao(App.class);
		}
		return simpleDao;
	}
	
	public RuntimeExceptionDao<App, String> getSimpleDataDao() {
		if (simpleRuntimeDao == null) {
			simpleRuntimeDao = getRuntimeExceptionDao(App.class);
		}
		return simpleRuntimeDao;
	}
	
	@Override
	public void close() {
		super.close();
		simpleDao = null;
		simpleRuntimeDao = null;
	}


}
