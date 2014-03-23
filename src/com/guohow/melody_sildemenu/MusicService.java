/**
 * 
 */
package com.guohow.melody_sildemenu;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import app.guohow.melody.service.MelodyPlayer;

/**
 * @author Administrator
 * 
 */
public class MusicService extends Service {

	// 返回music service
	public class MusicBinder extends Binder {
		MusicService getService() {

			return MusicService.this;
		}
	}

	private static String TAG = "---MusicService";
	private IBinder binder = new MusicService.MusicBinder();

	MelodyPlayer player = null;

	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return binder;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onCreate");
		super.onCreate();
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		Log.i(TAG, "onDestroy");
		super.onDestroy();
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.i(TAG, "onStartCommond");
		return super.onStartCommand(intent, flags, startId);
	}

}
