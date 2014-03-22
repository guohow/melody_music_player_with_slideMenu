package app.guohow.melody.help;

import android.app.Activity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;
import app.guohow.melody.ui.MyToast;

import com.guohow.melody_sildemenu.R;

public class HelpForMain extends Activity {
	private LinearLayout layout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help);
		layout = (LinearLayout) findViewById(R.id.help);
		layout.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				MyToast.makeText(getApplicationContext(), "点击外部窗口即可关闭提示",
						Toast.LENGTH_SHORT).show();
			}
		});
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		finish();
		return true;
	}

}
