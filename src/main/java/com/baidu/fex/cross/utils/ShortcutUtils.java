package com.baidu.fex.cross.utils;

import java.util.List;

import com.baidu.fex.cross.BrowserActivity;
import com.baidu.fex.cross.R;

import android.content.ComponentName;
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
	
	public static Bitmap generateShorcut(Context context,Bitmap icon){
		Bitmap newIcon = Bitmap.createBitmap(icon.getWidth(), icon.getHeight(), Config.ARGB_8888);
		Bitmap light = ((BitmapDrawable)context.getResources().getDrawable(R.drawable.light_app_icon)).getBitmap();
		Bitmap scaleLight = Bitmap.createScaledBitmap(light, icon.getWidth(), icon.getHeight(), true);
		Canvas canvas = new Canvas(newIcon);
		canvas.drawBitmap(icon, 0, 0, null);
		canvas.drawBitmap(scaleLight,0, 0,null);
		canvas.save(Canvas.ALL_SAVE_FLAG );
		canvas.restore();
		return newIcon;
	}
	
	public static boolean hasShortcut(Context context,String name) {
		String AUTHORITY = getAuthorityFromPermission(context,"com.android.launcher.permission.READ_SETTINGS");
		if (AUTHORITY == null) {
			return false;
		}
		Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY+ "/favorites?notify=true");
		Cursor c = context.getContentResolver().
				   query(CONTENT_URI,new String[] { "title" }, "title=?", new String[] { name },null);
		if (c != null && c.getCount() > 0) {
			return true;
		}
		return false;
	}
	
	public static void createShortcut(Context context,String url,String name,int resID) {
		final Intent intent = new Intent();
		Intent shortcutIntent = new Intent();
		shortcutIntent.putExtra("duplicate", false);
		shortcutIntent.setComponent(new ComponentName(
				context.getPackageName(), BrowserActivity.class.getName()));
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		shortcutIntent.setAction(ACTION_APP_LAUNCHER);
		shortcutIntent.putExtra("url", url);

		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		intent.putExtra(
				Intent.EXTRA_SHORTCUT_ICON,generateShorcut(context,((BitmapDrawable)context.getResources().getDrawable(resID)).getBitmap()));		
		intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
		context.sendBroadcast(intent);
	}
	
	private static String getAuthorityFromPermission(Context context,String permission) {
		if (TextUtils.isEmpty(permission)) {
			return null;
		}
		List<PackageInfo> packageInfoList = 
		context.getPackageManager().getInstalledPackages(PackageManager.GET_PROVIDERS);
		if (packageInfoList == null) {
			return null;
		}
		for (PackageInfo packageInfo : packageInfoList) {
			ProviderInfo[] providerInfos = packageInfo.providers;
			if (providerInfos != null) {
				for (ProviderInfo providerInfo : providerInfos) {
					if (permission.equals(providerInfo.readPermission)|| 
						permission.equals(providerInfo.writePermission)) {
						return providerInfo.authority;
					}
				}
			}
		}
		return null;
	}

}
