package app.guohow.melody.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import app.guohow.melody.MelodyMenuFragment;

public class MenuUtils extends Activity {
	static List<HashMap<String, Object>> items;

	public List<HashMap<String, Object>> getMenuItemList() {
		items = new ArrayList<HashMap<String, Object>>();

		HashMap<String, Object> map = new HashMap<String, Object>();
		HashMap<String, Object> map2 = new HashMap<String, Object>();
		HashMap<String, Object> map3 = new HashMap<String, Object>();
		HashMap<String, Object> map4 = new HashMap<String, Object>();
		HashMap<String, Object> map5 = new HashMap<String, Object>();

		map.put("menu_title", "我的收藏");
		int icon0 = MelodyMenuFragment.icon_0;
		map.put("icon", icon0);

		map2.put("menu_title", "本地歌曲");
		int icon1 = MelodyMenuFragment.icon_1;
		map2.put("icon", icon1);

		map3.put("menu_title", "正在播放");
		int icon2 = MelodyMenuFragment.icon_2;
		map3.put("icon", icon2);

		map4.put("menu_title", "个性化");
		int icon3 = MelodyMenuFragment.icon_3;
		map4.put("icon", icon3);

		map5.put("menu_title", "开发者");
		int icon4 = MelodyMenuFragment.icon_4;
		map5.put("icon", icon4);

		items.add(map);
		items.add(map2);
		items.add(map3);
		items.add(map4);
		items.add(map5);

		for (int i = 0; i < 5; i++) {
			System.out.println("" + items.get(i).get("menu_title"));
		}

		return items;
	}

}
