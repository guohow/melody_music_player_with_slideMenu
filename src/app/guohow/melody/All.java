package app.guohow.melody;

import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import app.guohow.melody.adapter.MySimpleAdapter;
import app.guohow.melody.database.SQLHelper;
import app.guohow.melody.service.MelodyPlayer;
import app.guohow.melody.utils.Mp3Bean;
import app.guohow.melody.utils.MusicUtils;

import com.guohow.melody_sildemenu.R;

public class All extends Fragment {

	public View mRootView;
	protected Context mContext;
	static List<HashMap<String, Object>> data;
	Button btn_play, btn_next, btn_previous, btn_playMod;
	SeekBar seekBar;
	TextView bottomInfo;
	public static ListView listView = null;
	// private static boolean HAS_UPDATED_DB_ALL = false;
	private static SQLHelper dbHelper;
	// private static String DB_NAME_ALL = "mAllSongsTable.db";
	// private static int DB_VERSION_ALL = 1;
	private static String DB_NAME = "mTable.db";
	private static int DB_VERSION = 1;

	public static int _index;
	public static SQLiteDatabase db;
	Handler handler = new Handler();

	public All() {
		super();
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.all, container, false);
		// 设置actionBar图标
		getActivity().getActionBar().setLogo(R.drawable.ic_local);
		getActivity().getActionBar().setTitle("本地音乐");
		// 是否隐藏action bar
		if (getHideAcBar()) {
			getActivity().getActionBar().hide();
		}
		return mRootView;
	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub

		// getActivity().getMenuInflater().inflate(R.menu.ac_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		initMusicListAdapter();
		listItemInit();
		onItemLongPressedControler();
		btn_Control();
		// if (!HAS_UPDATED_DB_ALL) {
		//
		// createAllSongsTable();
		// }

		super.onActivityCreated(savedInstanceState);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onResume()
	 */
	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		// handler更新底兰
		handler.post(new Runnable() {
			@Override
			public void run() {
				PlayerUIMonitor.btnCheck();
				if (bottomInfo != null && MelodyPlayer.hasEverPlayed) {
					PlayerUIMonitor.updatePlayingSongInfo();
					bottomInfo.setText("正在播放:"
							+ PlayerUIMonitor.playingSongTitle + "  " + "艺术家:"
							+ PlayerUIMonitor.playingSongArtist);
				}
				// 1秒之后再次发送
				handler.postDelayed(this, 1000);
			}
		});

		super.onResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onViewStateRestored(android.os.Bundle)
	 */

	public void initMusicListAdapter() {

		listView = (ListView) mRootView.findViewById(R.id.allSongsList);
		// mSlideBar = (SlideBarView) mRootView.findViewById(R.id.slideBar);

		bottomInfo = (TextView) mRootView.findViewById(R.id.info_all);
		// 设置颜色
		 initColor();
		// 獲取CONTEXT
		Context con = mRootView.getContext();
		// 获取CURSOR
		Cursor cursor = con.getContentResolver().query(
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		List<Mp3Bean> mp3InfoList = MusicUtils.getMp3Infos(cursor, con);
		MusicUtils.listUpdate(mp3InfoList);
		// 更新data
		data = MusicUtils.mp3list;
		bottomInfo.setText("本地队列：" + data.size() + "首");
		// 自定义ADP
		// 2行显示，曲目和作者
		MySimpleAdapter adapter = new MySimpleAdapter(con, data,
				R.layout.allitem,
				new String[] { "title", "artist", "duration" }, new int[] {
						R.id.mTitle, R.id.mArt, R.id.mDuration });
		listView.setAdapter(adapter);

	}

	private void listItemInit() {

		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {

				// TODO Auto-generated method stub
				if (MelodyPlayer.hasEverPlayed && MelodyPlayer.player != null) {
					MelodyPlayer.current = position;
					MelodyPlayer.itemList = data;
					MelodyPlayer.player.stop();
					MelodyPlayer.player.play();
				}
				if (!MelodyPlayer.hasEverPlayed) {
					MelodyPlayer.startPlaying(position, data);
				}
			}
		});
		// listView.setOnScrollListener(new OnScrollListener() {
		//
		// @Override
		// public void onScrollStateChanged(AbsListView view, int scrollState) {
		// // TODO Auto-generated method stub
		//
		// switch (scrollState) {
		// case OnScrollListener.SCROLL_STATE_IDLE: //
		// if (bottomInfo != null) {
		// bottomInfo.setVisibility(View.VISIBLE);
		// }
		//
		// System.out.println("停止...");
		// break;
		// case OnScrollListener.SCROLL_STATE_TOUCH_SCROLL:
		// if (bottomInfo != null) {
		// bottomInfo.setVisibility(View.GONE);
		//
		// }
		//
		// System.out.println("正在滑动...");
		// break;
		// case OnScrollListener.SCROLL_STATE_FLING:
		//
		// System.out.println("开始滚动...");
		//
		// break;
		// }
		// }
		//
		// @Override
		// public void onScroll(AbsListView view, int firstVisibleItem,
		// int visibleItemCount, int totalItemCount) {
		// // TODO Auto-generated method stub
		//
		// }
		// });

	}

	private void btn_Control() {
		btn_play = (Button) mRootView.findViewById(R.id.play);
		btn_next = (Button) mRootView.findViewById(R.id.next);
		btn_previous = (Button) mRootView.findViewById(R.id.previous);
		btn_playMod = (Button) mRootView.findViewById(R.id.playMod);
		new PlayerUIMonitor(btn_play, btn_previous, btn_next, btn_playMod,
				seekBar);
	}

	private void onItemLongPressedControler() {
		listView.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

			@Override
			public void onCreateContextMenu(ContextMenu menu, View arg1,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				_index = ((AdapterContextMenuInfo) menuInfo).position;// 获取menu点击项的position
				menu.setHeaderIcon(R.drawable.icon_share_sns_music_circle);
				menu.setHeaderTitle(data.get(_index).get("title").toString());
				menu.add(0, 0, 0, "标记为我喜欢");
				menu.add(0, 3, 0, "分享");

			}
		});

	}

	/*
	 * // 创建、查询 private void createAllSongsTable() {
	 * 
	 * try { // 初始化&创建数据库 dbHelper = new SQLHelper(mRootView.getContext(),
	 * DB_NAME_ALL, null, DB_VERSION_ALL); // 创建表 // TABLE_NAME数据库名 *.db //
	 * SQLHelper.TABLE_NAME 表名 db = dbHelper.getWritableDatabase(); //
	 * 自动调用SQLiteHelper.OnCreate() // 添加到数据库 for (int i = 0; i < data.size();
	 * i++) { ContentValues values = new ContentValues(); values.put("title",
	 * (String) data.get(i).get("title")); values.put("artist", (String)
	 * data.get(i).get("artist")); values.put("url", (String)
	 * data.get(i).get("url"));
	 * 
	 * db.insert(SQLHelper.TABLE_NAME, null, values); } // 通知更新数据库完毕不要在更新了 //
	 * HAS_UPDATED_DB_ALL = true;
	 * 
	 * } catch (IllegalArgumentException e) { e.printStackTrace(); //
	 * 当版本变更时会调用SQLiteHelper.onUpgrade()方法重建表 // 注：表以前数据将丢失 ++DB_VERSION_ALL;
	 * dbHelper.onUpgrade(db, --DB_VERSION_ALL, DB_VERSION_ALL); }
	 * 
	 * }
	 */
	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 0:
			// 初始化&创建数据库
			dbHelper = new SQLHelper(mRootView.getContext(), DB_NAME, null,
					DB_VERSION);
			// 创建表
			db = dbHelper.getWritableDatabase(); // 自动调用SQLiteHelper.OnCreate()
			// 添加到我喜欢
			ContentValues values = new ContentValues();
			values.put("title", (String) data.get(_index).get("title"));
			values.put("artist", (String) data.get(_index).get("artist"));
			values.put("url", (String) data.get(_index).get("url"));

			db.insert(SQLHelper.TABLE_NAME, null, values);
			Favour.UPDATE = 1;
			break;

		case 3:
			Intent intent = new Intent(Intent.ACTION_SEND);

			intent.setType("text/plain");
			// intent.setType();//其他类型是什么？

			intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			intent.putExtra(Intent.EXTRA_TEXT,
					"这首歌挺好听，推荐给你！歌名是：" + data.get(_index).get("title")
							+ ",一定要听哦！ shared by Melody!");
			startActivity(Intent.createChooser(intent, "分享到"));
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);

	}

	private boolean getHideAcBar() {
		SharedPreferences spf = PreferenceManager
				.getDefaultSharedPreferences(mContext);

		return spf.getBoolean("if_hide_acBar_perf", false);

	}

	private boolean getMod() {
		SharedPreferences spf = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		
		return spf.getBoolean("mod_perf",false);

	}

	private void initColor() {
		if(getMod()){
			listView.setBackgroundResource(R.color.darker_gray);
			listView.setSelector(R.drawable.list_selecter_night);
			bottomInfo.setBackgroundResource(R.color.bg_dark_tab);
			
		}
	}

}
