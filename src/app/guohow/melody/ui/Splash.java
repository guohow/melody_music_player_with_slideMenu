package app.guohow.melody.ui;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;
import app.guohow.melody.Melody;

import com.guohow.melody_sildemenu.R;

public class Splash extends Activity {

	String text;

	public String getText() {

		SharedPreferences spf = PreferenceManager
				.getDefaultSharedPreferences(this);
		text = spf.getString("flash_text_perf", "MELODY");
		return text;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		// 全屏
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		setContentView(R.layout.splash);

		TextView textView = (TextView) findViewById(R.id.splash);
		textView.setText(getText());

		final Intent intent = new Intent();

		intent.setClass(Splash.this, Melody.class);
		Timer timer = new Timer();
		TimerTask task = new TimerTask() {
			@Override
			public void run() {
				startActivity(intent);
				Splash.this.finish();

			}
		};
		timer.schedule(task, 1000 * 1);

	}

}
