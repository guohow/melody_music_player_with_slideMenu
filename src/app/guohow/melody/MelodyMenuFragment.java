package app.guohow.melody;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import app.guohow.melody.settings.DevelopersSettings;
import app.guohow.melody.settings.PersonalizeSettings;
import app.guohow.melody.utils.MenuUtils;

import com.guohow.melody_sildemenu.R;

public class MelodyMenuFragment extends ListFragment {

	static List<HashMap<String, Object>> items;

	public static int icon_0, icon_1, icon_2, icon_3, icon_4;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.list, null);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		ListView listView = getListView();
		listView.setBackgroundResource(R.color.white);

		listView.setKeepScreenOn(true);

		items = new MenuUtils().getMenuItemList();
		SimpleAdapter adapter = new SimpleAdapter(getActivity(), items,
				R.layout.menuitem, new String[] { "icon", "menu_title" },
				new int[] { R.id.icon_menu_item, R.id.mMTitle });

		setListAdapter(adapter);
	}

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {
		Fragment newContent = null;
		switch (position) {
		case 0:
			Intent intent4 = new Intent();
			intent4.setClass(getActivity(), Folder.class);
			startActivity(intent4);
			break;

		case 1:
			newContent = new LocalList();
			break;
		case 2:
			Intent intent3 = new Intent();
			intent3.setClass(getActivity(), Playing.class);
			startActivity(intent3);
			break;

		case 3:
			Intent intent = new Intent();
			intent.setClass(getActivity(), PersonalizeSettings.class);
			startActivity(intent);
			break;
		case 4:
			Intent intent2 = new Intent();
			intent2.setClass(getActivity(), DevelopersSettings.class);
			startActivity(intent2);
			break;

		}
		if (newContent != null)
			switchFragment(newContent);
	}

	private void switchFragment(Fragment fragment) {
		if (getActivity() == null)
			return;

		if (getActivity() instanceof Melody) {
			Melody fca = (Melody) getActivity();
			fca.switchContent(fragment);
		}
	}

	public int getDrawable(int _id) {
		int icon_ = 0;
		switch (_id) {
		case 0:
			icon_ = R.drawable.img_menu_favour;
			break;
		case 1:
			icon_ = R.drawable.img_menu_local;
			break;
		case 2:
			icon_ = R.drawable.img_menu_ing;
			break;
		case 3:
			icon_ = R.drawable.img_menu_per;
			break;
		case 4:
			icon_ = R.drawable.img_about;
			break;

		}

		return icon_;
	}

	@Override
	public void onAttach(Activity activity) {
		// TODO Auto-generated method stub
		super.onAttach(activity);
		icon_0 = getDrawable(0);
		icon_1 = getDrawable(1);
		icon_2 = getDrawable(2);
		icon_3 = getDrawable(3);
		icon_4 = getDrawable(4);

	}

}
