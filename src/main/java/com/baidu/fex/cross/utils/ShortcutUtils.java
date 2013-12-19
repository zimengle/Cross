package com.baidu.fex.cross.utils;

import java.io.ByteArrayOutputStream;
import java.sql.Blob;
import java.util.List;

import com.baidu.fex.cross.BrowserActivity;
import com.baidu.fex.cross.R;
import com.baidu.fex.cross.model.App;

import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Bitmap.Config;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;

public class ShortcutUtils {

	public static final String ACTION_APP_LAUNCHER = "ACTION_APP_LAUNCHER";

	public static Bitmap generateShorcut(Context context, int resID) {
		return generateShorcut(context, ((BitmapDrawable) context
				.getResources().getDrawable(resID)).getBitmap());
	}

	public static Bitmap generateBitmap(Bitmap... icons) {
		int width = icons[0].getWidth();
		int height = icons[0].getHeight();
		Bitmap newIcon = Bitmap.createBitmap(width, height, Config.ARGB_8888);
		Canvas canvas = new Canvas(newIcon);
		for (Bitmap icon : icons) {
			canvas.drawBitmap(
					Bitmap.createScaledBitmap(icon, width, height, true), 0, 0,
					null);
		}
		canvas.save(Canvas.ALL_SAVE_FLAG);
		canvas.restore();
		return newIcon;
	}

	public static Bitmap generateNewShorcut(Context context, Bitmap icon) {
		return generateBitmap(icon, ((BitmapDrawable) context.getResources()
				.getDrawable(R.drawable.new_icon)).getBitmap());
	}

	public static Bitmap generateShorcut(Context context, Bitmap icon) {
		Bitmap light = ((BitmapDrawable) context.getResources().getDrawable(
				R.drawable.light_app_icon)).getBitmap();
		return generateBitmap(icon, light);
	}

	public static boolean hasShortcut(Context context, String name) {
		String AUTHORITY = getAuthorityFromPermission(context,
				"com.android.launcher.permission.READ_SETTINGS");
		if (AUTHORITY == null) {
			return false;
		}
		
		Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		Cursor c = context.getContentResolver().query(CONTENT_URI,
				new String[] { "title"}, "title=? and iconType=1 and intent like ?", new String[] { name,"%"+ACTION_APP_LAUNCHER+"%"},
				null);
		if (c != null && c.getCount() > 0) {
			c.moveToFirst();
//			byte[] hehe = c.getString(1);
			
			return true;
		}
		return false;
	}

	public static void createShortcut(Context context, App app) {
		final Intent intent = new Intent();
		Intent shortcutIntent = new Intent();

		shortcutIntent.setComponent(new ComponentName(context.getPackageName(),
				BrowserActivity.class.getName()));
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		shortcutIntent.setAction(ACTION_APP_LAUNCHER);
		shortcutIntent.putExtra("url", app.getUrl());
		shortcutIntent.putExtra("name", app.getName());
		intent.putExtra("duplicate", true);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, app.getName());
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, AppUtils.getIcon(context, app));
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		context.sendBroadcast(intent);
	}
	
	public static void createShortcut(Context context, String name,String url,Bitmap bitmap,String type) {
		final Intent intent = new Intent();
		Intent shortcutIntent = new Intent();

		shortcutIntent.setComponent(new ComponentName(context.getPackageName(),
				BrowserActivity.class.getName()));
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		shortcutIntent.setAction(ACTION_APP_LAUNCHER);
		shortcutIntent.putExtra("url", url);
		shortcutIntent.putExtra("name", name);
		shortcutIntent.putExtra("type", type);
		intent.putExtra("duplicate", true);
		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		intent.putExtra(Intent.EXTRA_SHORTCUT_ICON, bitmap);
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		context.sendBroadcast(intent);
	}

	public static void updateShortcut(Context context,App app) {
		String AUTHORITY = getAuthorityFromPermission(context,
				"com.android.launcher.permission.WRITE_SETTINGS");
		if (AUTHORITY == null) {
			return ;
		}
		ContentResolver cr = context.getContentResolver();
		Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY
				+ "/favorites?notify=true");
		
		ContentValues cv = new ContentValues();
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		AppUtils.getIcon(context, app).compress(Bitmap.CompressFormat.PNG, 100, baos); 
		byte[] bytes = baos.toByteArray();
		cv.put("icon",bytes);
		cr.update(CONTENT_URI, cv, "title=?", new String[] {app.getName()});
	}

	private static String getAuthorityFromPermission(Context context,
			String permission) {
		if (TextUtils.isEmpty(permission)) {
			return null;
		}
		List<PackageInfo> packageInfoList = context.getPackageManager()
				.getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (packageInfoList == null) {
			return null;
		}
		for (PackageInfo packageInfo : packageInfoList) {
			ProviderInfo[] providerInfos = packageInfo.providers;
			if (providerInfos != null) {
				for (ProviderInfo providerInfo : providerInfos) {
					if (permission.equals(providerInfo.readPermission)
							|| permission.equals(providerInfo.writePermission)) {
						return providerInfo.authority;
					}
				}
			}
		}
		return null;
	}

}
