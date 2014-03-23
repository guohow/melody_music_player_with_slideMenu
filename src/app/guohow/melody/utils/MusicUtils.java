package app.guohow.melody.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.provider.MediaStore.Audio.AudioColumns;
import android.provider.MediaStore.MediaColumns;
import android.util.Log;

public class MusicUtils {

	public static List<HashMap<String, Object>> mp3list;

	public static String formatTime(long time) {
		String min = time / (1000 * 60) + "";
		String sec = time % (1000 * 60) + "";
		if (min.length() < 2) {
			min = "0" + time / (1000 * 60) + "";
		} else {
			min = time / (1000 * 60) + "";
		}
		if (sec.length() == 4) {
			sec = "0" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 3) {
			sec = "00" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 2) {
			sec = "000" + (time % (1000 * 60)) + "";
		} else if (sec.length() == 1) {
			sec = "0000" + (time % (1000 * 60)) + "";
		}
		return min + ":" + sec.trim().substring(0, 2);
	}

	public static String getAlbumArt(String trackId, Context context) {
		Log.e("trackId", "trackId---" + trackId);
		Log.e("context_", "context:" + context + "");
		String mUriTrack = "content://media/external/audio/media/#";
		String[] projection = new String[] { "album_id" };
		String selection = "_id = ?";
		String[] selectionArgs = new String[] { trackId };
		Cursor cur = context.getContentResolver().query(Uri.parse(mUriTrack),
				projection, selection, selectionArgs, null);
		int album_id = 0;
		if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
			cur.moveToNext();
			album_id = cur.getInt(0);
		}
		cur.close();
		cur = null;

		if (album_id < 0) {
			return null;
		}
		String mUriAlbums = "content://media/external/audio/albums";
		projection = new String[] { "album_art" };
		cur = context.getContentResolver().query(
				Uri.parse(mUriAlbums + "/" + Integer.toString(album_id)),
				projection, null, null, null);

		String album_art = null;
		if (cur.getCount() > 0 && cur.getColumnCount() > 0) {
			cur.moveToNext();
			album_art = cur.getString(0);
		}
		cur.close();
		cur = null;
		Log.e("album_art", "album_art---" + album_art);
		Log.e("trackId", "trackId---" + trackId);
		return album_art;
	}

	public static List<Mp3Bean> getMp3Infos(Cursor cursor, Context context) {

		List<Mp3Bean> mp3Infos = new ArrayList<Mp3Bean>();
		for (int i = 0; i < cursor.getCount(); i++) {
			cursor.moveToNext();
			long id = cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)); // 音乐id
			String title = cursor.getString((cursor
					.getColumnIndex(MediaColumns.TITLE)));// 音乐标题
			String artist = cursor.getString(cursor
					.getColumnIndex(AudioColumns.ARTIST));// 艺术家
			long duration = cursor.getLong(cursor
					.getColumnIndex(AudioColumns.DURATION));// 时长
			long size = cursor
					.getLong(cursor.getColumnIndex(MediaColumns.SIZE)); // 文件大小
			String url = cursor.getString(cursor
					.getColumnIndex(MediaColumns.DATA)); // 文件路径

			int isMusic = cursor.getInt(cursor
					.getColumnIndex(AudioColumns.IS_MUSIC));// 是否为音乐
			if (isMusic != 0) {
				// 如果是音乐，创建一个音乐对象并加入集合
				Mp3Bean mp3Info = new Mp3Bean();

				// 只把音乐添加到集合当中
				mp3Info.setId(id);
				mp3Info.setTitle(title);
				mp3Info.setArt(artist);
				mp3Info.setDuration(formatTime(duration));
				mp3Info.setSize(size);
				mp3Info.setUrl(url);
				mp3Infos.add(mp3Info);
			}
		}
		return mp3Infos;
	}

	public static void listUpdate(List<Mp3Bean> mp3Infos) {

		mp3list = new ArrayList<HashMap<String, Object>>();
		for (Iterator iterator = mp3Infos.iterator(); iterator.hasNext();) {
			Mp3Bean mp3Info = (Mp3Bean) iterator.next();
			HashMap<String, Object> map = new HashMap<String, Object>();
			map.put("title", mp3Info.getTitle());
			map.put("artist", mp3Info.getArt());
			map.put("duration", (mp3Info.getDuration()));
			map.put("size", String.valueOf(mp3Info.getSize()));
			map.put("url", mp3Info.getUrl());
			map.put("id", mp3Info.getId());
			mp3list.add(map);
		}

	}

}
