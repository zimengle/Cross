package com.baidu.fex.cross.utils;

import android.content.Context;
import android.graphics.Bitmap;

import com.baidu.fex.cross.model.App;

public class AppUtils {

	public static boolean checkInstall(Context context ,App app){
		boolean isInstalled = ShortcutUtils.hasShortcut(context, app.getShortcutName());
		return isInstalled;
	}
	
	public static Bitmap getIcon(Context context,App app){
		Bitmap bitmap = ShortcutUtils.generateShorcut(context, app.getAppIcon());
		if(app.isHasNewMsg()){
			bitmap = ShortcutUtils.generateNewShorcut(context, bitmap);
		}
		return bitmap;
	}

}
