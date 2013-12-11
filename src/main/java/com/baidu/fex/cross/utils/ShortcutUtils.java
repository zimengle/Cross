package com.baidu.fex.cross.utils;

import java.util.List;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ProviderInfo;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.text.TextUtils;


public class ShortcutUtils {

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
	
	public static void createShortcut(Context context,String action,String url,String name,int resID) {
		final Intent intent = new Intent();
		Intent shortcutIntent = new Intent();
		shortcutIntent.putExtra("duplicate", false);
		shortcutIntent.setClassName("com.baidu.fex.cross",
				"com.baidu.fex.cross.HomeActivity");
		shortcutIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
				| Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
		shortcutIntent.setAction(action);
		shortcutIntent.putExtra("url", url);

		intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, shortcutIntent);
		intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, name);
		intent.putExtra(
				Intent.EXTRA_SHORTCUT_ICON,((BitmapDrawable)context.getResources().getDrawable(resID)).getBitmap());		
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
