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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import app.guohow.melody.UIMonitor;
import app.guohow.melody.service.MelodyPlayer;
import app.guohow.melody.utils.ArtWorkUtils;

import com.guohow.melody_sildemenu.R;

public class PlayingFragmentCenter extends Fragment {
	protected Context mContext;
	protected View mRootView;

	ImageView imageView;
	TextView title, artist;
	SeekBar seekBar;
	Button btn_play, btn_next, btn_pre, btn_playMod, btn_fav;
	Handler handler = new Handler();
	private static String ACTION_NAME = "SONG_CHANGED";
	private static String ACTION_NAME_MONITOR = "BTN_PRESSED";

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ACTION_NAME)) {
				System.out.println(" Playing 正在处理广播");
				updateUI();
				updateImageUI();
			}
			if (action.equals(ACTION_NAME_MONITOR)) {
				UIMonitor.btnCheck();
				System.out.println("ing  正在处理按钮广播");
			}
		}

	};

	private void initBg() {
		imageView = (ImageView) mRootView.findViewById(R.id.img_art);
		ArtWorkUtils.getContent(imageView);

	}

	private void initBtn() {
		btn_play = (Button) mRootView.findViewById(R.id.btn_play_ing);
		btn_next = (Button) mRootView.findViewById(R.id.btn_next_ing);
		btn_pre = (Button) mRootView.findViewById(R.id.btn_pre_ing);
		btn_playMod = (Button) mRootView.findViewById(R.id.play_mod_ing);
		btn_fav = (Button) mRootView.findViewById(R.id.btn_set_favourite);

	}

	private void initMonitor() {
		new UIMonitor(btn_play, btn_pre, btn_next, btn_playMod, seekBar,
				btn_fav);
	}

	private void initSeekBar() {
		seekBar = (SeekBar) mRootView.findViewById(R.id.seekBar_ing);
	}

	private void initText() {
		title = (TextView) mRootView.findViewById(R.id.title_text);
		artist = (TextView) mRootView.findViewById(R.id.art_text);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		mRootView = inflater.inflate(R.layout.playing, container, false);
		initBg();
		initText();
		initSeekBar();

		initBtn();
		initMonitor();
		updateUI();
		updateImageUI();
		UIMonitor.btnCheck();
		MelodyPlayer.context = mContext;
		UIMonitor.context = mContext;
		// 注册广播
		registerBoradcastReceiver();

		handler.post(new Runnable() {
			@Override
			public void run() {
				updateSeekBarState();
				System.out.println("输出的player对象============"
						+ MelodyPlayer.player.getCurrentPosition());
				System.out
						.println(seekBar
								+ "===========================================seekBar id__");
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

	private void updateImageUI() {
		UIMonitor.btnCheck();
		ArtWorkUtils.getContent(imageView);

	}

	private void updateSeekBarState() {
		seekBar.setProgress(MelodyPlayer.player.getCurrentPosition());
		// seekBar.invalidate();
	}

	private void updateUI() {
		UIMonitor.updatePlayingSongInfo();
		title.setText(UIMonitor.playingSongTitle);
		artist.setText(UIMonitor.playingSongArtist);

	}

}
