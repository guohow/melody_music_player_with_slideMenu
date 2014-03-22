package app.guohow.melody.utils;

import java.io.File;

import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.id3.AbstractID3v2Frame;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.framebody.FrameBodyAPIC;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.ImageView;
import app.guohow.melody.service.MelodyPlayer;

public class ArtWorkUtils {

	/**
	 * 获得歌曲内容
	 */
	public static void getContent(ImageView view) {

		Bitmap bitmap = null;
		String url = "";
		if (MelodyPlayer.hasEverPlayed) {
			url = (String) MelodyPlayer.itemList.get(MelodyPlayer.current).get(
					"url");
		}
		try {
			File sourceFile = new File(url);
			MP3File mp3file = null;

			mp3file = new MP3File(sourceFile);
			AbstractID3v2Tag tag = mp3file.getID3v2Tag();
			AbstractID3v2Frame frame = (AbstractID3v2Frame) tag
					.getFrame("APIC");
			FrameBodyAPIC body = (FrameBodyAPIC) frame.getBody();
			byte[] imageData = body.getImageData();
			bitmap = BitmapFactory.decodeByteArray(imageData, 0,
					imageData.length);
			System.out.println("img--------------------" + bitmap);
		} catch (Exception e) {
			e.printStackTrace();
		}

		view.setImageBitmap(bitmap);

	}

}