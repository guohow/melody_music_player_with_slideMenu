package app.guohow.melody;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import app.guohow.melody.service.MelodyPlayer;

import com.guohow.melody_sildemenu.R;

public class UIMonitor {

	public static String playingSongTitle = "melody rhythm";
	public static String playingSongArtist = "art-melody";
	static Button btn_play, btn_previous, btn_next, btn_playMod;
	// 0默认顺序循环 1全部循环2单曲循环3随机播放
	public static int playMod = 0;
	static SeekBar seekBar;
	private static String ACTION_NAME_MONITOR = "BTN_PRESSED";
	public static Context context;

	public UIMonitor(Button btn_play, Button btn_previous, Button btn_next,
			Button btn_playMod, SeekBar seekBar) {
		UIMonitor.btn_next = btn_next;
		UIMonitor.btn_play = btn_play;
		UIMonitor.btn_previous = btn_previous;
		UIMonitor.btn_playMod = btn_playMod;
		UIMonitor.seekBar = seekBar;
		btnControler();
		seekBarInit();

	}

	private static void sentBoradCast() {

		Intent mIntent = new Intent(ACTION_NAME_MONITOR);
		mIntent.putExtra("button", "按钮被按下的广播");

		// 发送广播
		// 需要先初始化context
		context.sendBroadcast(mIntent);
	}

	private static void btnControler() {

		// 按钮监听
		// 正在播放则暂停
		if (btn_play != null && LocalList.data != null) {
			btn_play.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					sentBoradCast();

					if (!MelodyPlayer.hasEverPlayed) {
						MelodyPlayer.startPlaying(0, LocalList.data);
						// btn_play.setBackgroundResource(R.drawable.btn_pause);
						MelodyPlayer.FLAG = 1;
					} else {
						switch (MelodyPlayer.FLAG) {
						case 0:
							MelodyPlayer.mResume();
							// btn_play.setBackgroundResource(R.drawable.btn_pause);
							MelodyPlayer.FLAG = 1;

							break;
						case 1:
							MelodyPlayer.mPause();
							// btn_play.setBackgroundResource(R.drawable.btn_play);
							MelodyPlayer.FLAG = 0;
							break;
						}
					}
				}
			});
		}

		// next按钮监听
		if (btn_next != null && LocalList.data != null) {

			btn_next.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (seekBar != null) {
						seekBar.setVisibility(View.VISIBLE);
					}// 解决方案：seekBar只能从0开始更新，所以再按下playing界面的btn之前不显示
					if (MelodyPlayer.FLAG == 1) {

						MelodyPlayer.next();

						btn_play.setBackgroundResource(R.drawable.btn_pause);
					} else {
						MelodyPlayer.startPlaying(0, LocalList.data);
						btn_play.setBackgroundResource(R.drawable.btn_pause);

					}
				}

			});

		}

		if (btn_previous != null && LocalList.data != null) {

			btn_previous.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (seekBar != null) {
						seekBar.setVisibility(View.VISIBLE);
					}
					if (MelodyPlayer.FLAG == 1) {
						MelodyPlayer.previous();
						btn_play.setBackgroundResource(R.drawable.btn_pause);
					} else {
						MelodyPlayer.startPlaying(0, LocalList.data);
						btn_play.setBackgroundResource(R.drawable.btn_pause);
					}
				}

			});

		}

		if (btn_playMod != null) {

			btn_playMod.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					sentBoradCast();
					switch (playMod) {
					case 0:
						playMod = 1;
						btn_playMod
								.setBackgroundResource(R.drawable.btn_repeat_all);
						break;
					case 1:
						playMod = 2;
						btn_playMod
								.setBackgroundResource(R.drawable.btn_repeat_one);
						break;
					case 2:
						playMod = 3;
						btn_playMod
								.setBackgroundResource(R.drawable.btn_shuffle_state);
						break;
					case 3:
						playMod = 0;
						btn_playMod
								.setBackgroundResource(R.drawable.btn_repeat_off);
						break;
					}

				}
			});

		}

	}

	public static void btnCheck() {

		if (MelodyPlayer.FLAG == 0) {
			btn_play.setBackgroundResource(R.drawable.btn_play);

		}
		if (MelodyPlayer.FLAG == 1) {
			btn_play.setBackgroundResource(R.drawable.btn_pause);

		}
		if (playMod == 0) {
			btn_playMod.setBackgroundResource(R.drawable.btn_repeat_off);
		}
		if (playMod == 1) {
			btn_playMod.setBackgroundResource(R.drawable.btn_repeat_all);
		}
		if (playMod == 2) {
			btn_playMod.setBackgroundResource(R.drawable.btn_repeat_one);
		}
		if (playMod == 3) {
			btn_playMod.setBackgroundResource(R.drawable.btn_shuffle_state);
		}
	}

	public static String updatePlayingSongInfo() {
		String str = "";
		System.out.println("============================is callback");
		if (MelodyPlayer.itemList != null) {
			playingSongTitle = (String) MelodyPlayer.itemList.get(
					MelodyPlayer.current).get("title");
			playingSongArtist = (String) MelodyPlayer.itemList.get(
					MelodyPlayer.current).get("artist");
			str = "正在播放" + ":" + playingSongTitle + "		" + "艺术家" + ":"
					+ playingSongArtist;
		}

		return str;
	}

	private void seekBarInit() {

		if (seekBar != null)
			MelodyPlayer.seekBar = seekBar;

	}
}
