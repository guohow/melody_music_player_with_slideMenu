package app.guohow.melody.playerFragment;

import java.util.List;

import android.os.Bundle;
import android.view.WindowManager;
import app.guohow.melody.ui.IndicatorFragmentActivity;

//test android studio git
public class PlayingMain extends IndicatorFragmentActivity {
	/*
	 * (non-Javadoc)
	 * 
	 * @see com.guohow.melody.ui.IndicatorFragmentActivity#onDestroy()
	 */

	public static final int FRAGMENT_ONE = 0;
	public static final int FRAGMENT_TWO = 1;
	public static final int FRAGMENT_THREE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
	}

	@Override
	protected int supplyTabs(List<TabInfo> tabs) {
		tabs.add(new TabInfo(FRAGMENT_ONE, "歌曲信息", PlayingFragmentLeft.class));
		tabs.add(new TabInfo(FRAGMENT_TWO, "可视化", PlayingFragmentCenter.class));
		tabs.add(new TabInfo(FRAGMENT_THREE, "歌词", PlayingFragmentRight.class));

		return FRAGMENT_TWO;

	}

}
