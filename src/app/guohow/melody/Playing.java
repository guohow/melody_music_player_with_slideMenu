package app.guohow.melody;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import app.guohow.melody.service.MelodyPlayer;
import app.guohow.melody.utils.ArtWorkUtils;

import com.guohow.melody_sildemenu.R;

public class Playing extends Activity {
	ImageView imageView;
	TextView title, artist;
	SeekBar seekBar;
	Button btn_play, btn_next, btn_pre, btn_playMod;
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

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.playing);
		initBg();
		initText();
		initSeekBar();

		initBtn();
		initMonitor();
		updateUI();
		updateImageUI();
		UIMonitor.btnCheck();
		MelodyPlayer.context = this;
		UIMonitor.context = this;
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

	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ACTION_NAME);
		myIntentFilter.addAction(ACTION_NAME_MONITOR);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	private void updateUI() {
		UIMonitor.updatePlayingSongInfo();
		title.setText(UIMonitor.playingSongTitle);
		artist.setText(UIMonitor.playingSongArtist);

	}

	private void updateImageUI() {
		ArtWorkUtils.getContent(imageView);

	}

	private void updateSeekBarState() {
		seekBar.setProgress(MelodyPlayer.player.getCurrentPosition());
		// seekBar.invalidate();
	}

	private void initBg() {
		imageView = (ImageView) findViewById(R.id.img_art);
		ArtWorkUtils.getContent(imageView);

	}

	private void initText() {
		title = (TextView) findViewById(R.id.title_text);
		artist = (TextView) findViewById(R.id.art_text);
	}

	private void initSeekBar() {
		seekBar = (SeekBar) findViewById(R.id.seekBar_ing);
	}

	private void initBtn() {
		btn_play = (Button) findViewById(R.id.btn_play_ing);
		btn_next = (Button) findViewById(R.id.btn_next_ing);
		btn_pre = (Button) findViewById(R.id.btn_pre_ing);
		btn_playMod = (Button) findViewById(R.id.play_mod_playing);

	}

	private void initMonitor() {
		new UIMonitor(btn_play, btn_pre, btn_next, btn_playMod, seekBar);
	}

}
