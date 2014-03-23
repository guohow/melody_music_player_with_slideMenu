package app.guohow.melody.utils;

import android.graphics.Bitmap;

public class Mp3Bean {
	long id, size;

	public String title;

	String url;

	String art;

	String duration;
	Bitmap icon;

	public String getArt() {

		return this.art;
	}

	public String getDuration() {
		return this.duration;
	}

	public Bitmap getIcon() {
		return this.icon;

	}

	public long getId() {
		return this.id;
	}

	public long getSize() {
		return this.size;
	}

	public String getTitle() {
		return this.title;
	}

	public String getUrl() {
		return this.url;

	}

	public void setArt(String art) {

		this.art = art;
	}

	public void setDuration(String dur) {
		this.duration = dur;
	}

	public void setIcon(Bitmap icon) {
		this.icon = icon;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setUrl(String url) {
		this.url = url;
		;
	}

}
