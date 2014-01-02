package com.baidu.fex.cross.utils;

import java.util.Date;

import com.baidu.fex.cross.BrowserActivity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;

public class NotificationUtils {
	
	public static class NotificationItem{
		public int smallIcon;
		public String title;
		public String content;
		public Bitmap largeIcon;
		public String url;
		public NotificationItem(int smallIcon, String title, String content,
				Bitmap largeIcon, String url) {
			super();
			this.smallIcon = smallIcon;
			this.title = title;
			this.content = content;
			this.largeIcon = largeIcon;
			this.url = url;
		}
		
	}

	public static void build(Context context,NotificationItem item){
		NotificationCompat.Builder mBuilder =
		        new NotificationCompat.Builder(context)
		        .setSmallIcon(item.smallIcon)
		        .setLargeIcon(item.largeIcon)
		        .setContentTitle(item.title)
		        .setWhen(new Date().getTime())
		        .setContentText(item.content).setAutoCancel(true)
		        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE | Notification.DEFAULT_LIGHTS);
		Intent shortcutIntent = new Intent();
		shortcutIntent.setComponent(new ComponentName(context.getPackageName(),
				BrowserActivity.class.getName()));
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		shortcutIntent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
		shortcutIntent.setAction(ShortcutUtils.ACTION_APP_LAUNCHER);
		shortcutIntent.putExtra("url", item.url);
		// Creates an explicit intent for an Activity in your app
//		Intent resultIntent = new Intent(this, ResultActivity.class);
//	
//		// The stack builder object will contain an artificial back stack for the
//		// started Activity.
//		// This ensures that navigating backward from the Activity leads out of
//		// your application to the Home screen.
		TaskStackBuilder stackBuilder = TaskStackBuilder.from(context);
//		// Adds the back stack for the Intent (but not the Intent itself)
//		stackBuilder.addParentStack(ResultActivity.class);
		// Adds the Intent that starts the Activity to the top of the stack
		stackBuilder.addNextIntent(shortcutIntent);
		PendingIntent resultPendingIntent =
		        stackBuilder.getPendingIntent(
		            0,
		            PendingIntent.FLAG_UPDATE_CURRENT
		        );
		mBuilder.setContentIntent(resultPendingIntent);
		NotificationManager mNotificationManager =
		    (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(13231, mBuilder.getNotification());
	}
}
