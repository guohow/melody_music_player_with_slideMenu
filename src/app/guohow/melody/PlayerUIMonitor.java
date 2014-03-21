package app.guohow.melody;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.SeekBar;
import app.guohow.melody.service.MelodyPlayer;

import com.guohow.melody_sildemenu.R;

public class PlayerUIMonitor {

	public static String playingSongTitle = "melody rhythm";
	public static String playingSongArtist = "art-melody";
	static Button btn_play, btn_previous, btn_next, btn_playMod;
	// 0默认顺序循环 1全部循环2单曲循环3随机播放
	public static int playMod = 0;
	static SeekBar seekBar;

	public PlayerUIMonitor(Button btn_play, Button btn_previous,
			Button btn_next, Button btn_playMod, SeekBar seekBar) {
		PlayerUIMonitor.btn_next = btn_next;
		PlayerUIMonitor.btn_play = btn_play;
		PlayerUIMonitor.btn_previous = btn_previous;
		PlayerUIMonitor.btn_playMod = btn_playMod;
		PlayerUIMonitor.seekBar = seekBar;
		btnControler();
		seekBarInit();

	}

	private static void btnControler() {

		btnCheck();

		updatePlayingSongInfo();
		// 按钮监听
		// 正在播放则暂停
		if (btn_play != null && All.data != null) {
			btn_play.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (MelodyPlayer.FLAG == 1) {
						MelodyPlayer.mPause();
						btn_play.setBackgroundResource(R.drawable.btn_play);
						MelodyPlayer.FLAG = 0;
					} else if (MelodyPlayer.FLAG == 0
							&& MelodyPlayer.hasEverPlayed) {

						MelodyPlayer.mResume();
						MelodyPlayer.FLAG = 1;

						btn_play.setBackgroundResource(R.drawable.btn_pause);
					} else {
						MelodyPlayer.startPlaying(0, All.data);
						btn_play.setBackgroundResource(R.drawable.btn_pause);
						MelodyPlayer.FLAG = 1;
					}

				}
			});
		}

		// next按钮监听
		if (btn_next != null && All.data != null) {

			btn_next.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (MelodyPlayer.FLAG == 1) {

						MelodyPlayer.next();

						btn_play.setBackgroundResource(R.drawable.btn_pause);
					} else {
						MelodyPlayer.startPlaying(0, All.data);
						btn_play.setBackgroundResource(R.drawable.btn_pause);

					}
				}

			});

		}

		if (btn_previous != null && All.data != null) {

			btn_previous.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					if (MelodyPlayer.FLAG == 1) {
						MelodyPlayer.previous();
						btn_play.setBackgroundResource(R.drawable.btn_pause);
					} else {
						MelodyPlayer.startPlaying(0, All.data);
						btn_play.setBackgroundResource(R.drawable.btn_pause);
					}
				}

			});

		}

		if (btn_playMod != null) {

			btn_playMod.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {

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

	public static void updatePlayingSongInfo() {
		if (MelodyPlayer.player != null && MelodyPlayer.itemList != null) {
			playingSongTitle = (String) MelodyPlayer.itemList.get(
					MelodyPlayer.current).get("title");
			playingSongArtist = (String) MelodyPlayer.itemList.get(
					MelodyPlayer.current).get("artist");
		}
	}

	private void seekBarInit() {

		if (seekBar != null)
			MelodyPlayer.seekBar = seekBar;

	}
}
