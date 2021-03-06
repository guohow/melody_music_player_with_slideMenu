/**
 * 
 */
package app.guohow.melody.service;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import app.guohow.melody.UIMonitor;

/**
 * @author Administrator
 * 
 */
public class MelodyPlayer extends MediaPlayer {

	/*
	 * 需要传入的参数： current itemList seekBar
	 */

	private final class MyPlayerListener implements OnCompletionListener {
		// 歌曲播放完后自动播放下一首歌曲

		@Override
		public void onCompletion(MediaPlayer mp) {
			try {
				next();
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	public class MySeekBarListener implements OnSeekBarChangeListener {
		// 移动触发
		@Override
		public void onProgressChanged(SeekBar seekBar, int progress,
				boolean fromUser) {
		}

		// 起始触发
		@Override
		public void onStartTrackingTouch(SeekBar seekBar) {

		}

		// 结束触发
		@Override
		public void onStopTrackingTouch(SeekBar seekBar) {
			player.seekTo(seekBar.getProgress());

		}
	}

	public static SeekBar seekBar;
	public static boolean hasEverPlayed;
	private static String ACTION_NAME = "SONG_CHANGED";
	public static Context context;
	public static int FLAG = 0;// 0正在播放
	public static List<HashMap<String, Object>> itemList;
	public static int current;

	public static MelodyPlayer player = new MelodyPlayer();

	public static MelodyPlayer currentPlayer = player;

	public MelodyPlayer() {
	}

	public static MelodyPlayer getCurrentPlayerObj() {

		return currentPlayer;
	}

	// 0默认顺序循环 1全部循环2单曲循环3随机播放
	public static int getNextCursor() {
		int next = 0;
		switch (getPlayMod()) {
		case 0:
			if (current != itemList.size() - 1)
				next = current + 1 % itemList.size();
			else
				player.stop();
			break;
		case 1:
			if (current != itemList.size() - 1)
				next = current + 1 % itemList.size();
			else
				next = 0;
			break;
		case 2:
			next = current;
			break;
		case 3:
			next = new Random().nextInt(itemList.size());
			break;

		}

		return next;
	}

	private static int getPlayMod() {
		return UIMonitor.playMod;

	}

	public static int getPreviousCursor() {
		int previous = 0;
		switch (getPlayMod()) {
		case 0:
			previous = current - 1 % itemList.size();
			if (previous < 0) {
				previous = itemList.size() - 1;
			}
			break;
		case 1:
			previous = current - 1 % itemList.size();
			if (previous < 0) {
				previous = itemList.size() - 1;
			}
			break;
		case 2:
			previous = current;
			break;
		case 3:
			previous = new Random().nextInt(itemList.size());
			break;
		}
		return previous;
	}

	public static void mPause() {
		if (player != null)
			player.pause();
		FLAG = 0;
	}

	public static void mResume() {
		if (player != null)
			player.start();
	}

	public static void mStop() {

		if (player != null)
			player.stop();
		FLAG = 0;
	}

	public static void next() {

		current = getNextCursor();
		player.stop();
		player.play();

	}

	public static void previous() {
		current = getPreviousCursor();
		player.stop();
		player.play();

	}

	public static void startPlaying(int current,
			List<HashMap<String, Object>> itemList) {
		MelodyPlayer.current = current;
		MelodyPlayer.itemList = itemList;
		player.play();
	}

	@Override
	public int getDuration() {
		// TODO Auto-generated method stub
		return super.getDuration();
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated method stub
		return super.isPlaying();
	}

	@Override
	public void pause() throws IllegalStateException {
		// TODO Auto-generated method stub

		super.pause();
	}

	public void play() {
		player.reset();
		// 设置播放资源
		try {
			player.setDataSource((String) itemList.get(current).get("url"));
			player.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		player.start();
		FLAG = 1;
		sentBoradCast();
		// 进度条监听
		player.seekBarMonit();
		// 播放器监听器
		player.setOnCompletionListener(new MyPlayerListener());

	}

	@Override
	public void reset() {
		// TODO Auto-generated method stub
		super.reset();
	}

	private void seekBarMonit() {
		// 设置进度条长度
		if (seekBar != null) {
			seekBar.setMax(player.getDuration());
			seekBar.setOnSeekBarChangeListener(new MySeekBarListener());
		}
	}

	private void sentBoradCast() {

		Intent mIntent = new Intent(ACTION_NAME);
		mIntent.putExtra("guohow", "曲目已经改变，来自MELODY PLAYER的广播");

		// 发送广播
		context.sendBroadcast(mIntent);
	}

	@Override
	public void start() throws IllegalStateException {
		// TODO Auto-generated method stub
		FLAG = 1;
		hasEverPlayed = true;
		super.start();
	}

	@Override
	public void stop() throws IllegalStateException {
		// TODO Auto-generated method stub
		super.stop();
	}

}
