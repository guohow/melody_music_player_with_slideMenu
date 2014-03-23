package app.guohow.melody.playerFragment;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import app.guohow.melody.UIMonitor;
import app.guohow.melody.service.MelodyPlayer;

import com.guohow.melody_sildemenu.R;

public class PlayingFragmentRight extends Fragment {
	protected Context mContext;
	protected View mRootView;
	Handler handler = new Handler();
	private static String ACTION_NAME = "SONG_CHANGED";
	private static String ACTION_NAME_MONITOR = "BTN_PRESSED";

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ACTION_NAME)) {
				System.out.println(" right 正在处理song changed广播");
			}
			if (action.equals(ACTION_NAME_MONITOR)) {

				System.out.println("right  正在处理按钮广播");
			}
		}

	};

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.playing_right, container, false);
		MelodyPlayer.context = mContext;
		UIMonitor.context = mContext;
		// 注册广播
		registerBoradcastReceiver();

		handler.post(new Runnable() {
			@Override
			public void run() {

				handler.postDelayed(this, 1000);
			}
		});

		return mRootView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mContext.unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ACTION_NAME);
		myIntentFilter.addAction(ACTION_NAME_MONITOR);
		// 注册广播
		mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}
	//
	// private void updateImageUI() {
	// ArtWorkUtils.getContent(imageView);
	//
	// }

	//
	// private void updateUI() {
	// UIMonitor.updatePlayingSongInfo();
	// title.setText(UIMonitor.playingSongTitle);
	// artist.setText(UIMonitor.playingSongArtist);
	//
	// }

}
