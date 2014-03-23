package app.guohow.melody;

import java.util.HashMap;
import java.util.List;

import android.app.ActionBar;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
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
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import app.guohow.melody.adapter.MySimpleAdapter;
import app.guohow.melody.database.SQLHelper;
import app.guohow.melody.playerFragment.PlayingMain;
import app.guohow.melody.service.MelodyPlayer;
import app.guohow.melody.ui.MyToast;
import app.guohow.melody.utils.ArtWorkUtils;
import app.guohow.melody.utils.Mp3Bean;
import app.guohow.melody.utils.MusicUtils;

import com.guohow.melody_sildemenu.R;

public class LocalList extends Fragment {

	public View mRootView;
	protected Context mContext;
	static List<HashMap<String, Object>> data;
	Button btn_play, btn_next, btn_previous, btn_playMod, btn_fav;
	TextView bottomInfo;
	ImageView artImage;
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
	private static String ACTION_NAME = "SONG_CHANGED";
	private static String ACTION_NAME_MONITOR = "BTN_PRESSED";

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ACTION_NAME)) {
				System.out.println("Local list 正在处理切歌广播");
				updateUI();
				updateImageUI();
			}
			if (action.equals(ACTION_NAME_MONITOR)) {
				System.out.println("Local list 正在处理BTN广播");
				UIMonitor.btnCheck();
			}
		}

	};

	public LocalList() {
		super();
	}

	private void btn_Control() {
		btn_play = (Button) mRootView.findViewById(R.id.play);
		btn_next = (Button) mRootView.findViewById(R.id.next);
		btn_previous = (Button) mRootView.findViewById(R.id.previous);
		btn_playMod = (Button) mRootView.findViewById(R.id.playMod);
		btn_fav = (Button) mRootView.findViewById(R.id.btn_set_fav_a);
		new UIMonitor(btn_play, btn_previous, btn_next, btn_playMod, null,
				btn_fav);
	}

	private void btnCheck() {

		if (MelodyPlayer.FLAG == 0) {
			btn_play.setBackgroundResource(R.drawable.btn_play);

		}
		if (MelodyPlayer.FLAG == 1) {
			btn_play.setBackgroundResource(R.drawable.btn_pause);

		}
	}

	private boolean getHideAcBar() {
		SharedPreferences spf = PreferenceManager
				.getDefaultSharedPreferences(mContext);

		return spf.getBoolean("if_hide_acBar_perf", false);

	}

	private boolean getMod() {
		SharedPreferences spf = PreferenceManager
				.getDefaultSharedPreferences(mContext);

		return spf.getBoolean("mod_perf", false);

	}

	private void initActionBar() {

		// 设置actionBar图标
		ActionBar mActionBar = getActivity().getActionBar();
		mActionBar.setLogo(R.drawable.ac_img_menu_local);
		mActionBar.setTitle("");
		mActionBar.setBackgroundDrawable(getResources().getDrawable(
				R.color.blue));
		// 是否隐藏action bar
		if (getHideAcBar()) {
			mActionBar.hide();
		}
	}

	private void initColor() {
		if (getMod()) {
			listView.setBackgroundResource(R.color.bg_main);
			listView.setSelector(R.drawable.list_selecter_night);
			bottomInfo.setBackgroundResource(R.color.bg_dark_tab);
			getActivity().setTheme(android.R.style.Theme_Holo);
			getActivity().getActionBar().setBackgroundDrawable(
					getResources().getDrawable(R.color.bg_dark_tab));

		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onViewStateRestored(android.os.Bundle)
	 */

	public void initMusicListAdapter() {

		listView = (ListView) mRootView.findViewById(R.id.allSongsList);
		bottomInfo = (TextView) mRootView.findViewById(R.id.info_all);
		artImage = (ImageView) mRootView.findViewById(R.id.artIcon_all);

		// Context con = mRootView.getContext(); // 获取CONTEXT
		Cursor cursor = mContext.getContentResolver().query(
				// 获取CURSOR
				MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, null, null, null,
				MediaStore.Audio.Media.DEFAULT_SORT_ORDER);
		List<Mp3Bean> mp3InfoList = MusicUtils.getMp3Infos(cursor, mContext);
		MusicUtils.listUpdate(mp3InfoList);
		// 更新data
		data = MusicUtils.mp3list;

		// 自定义ADP
		// 2行显示，曲目和作者
		MySimpleAdapter adapter = new MySimpleAdapter(mContext, data,
				R.layout.allitem,
				new String[] { "title", "artist", "duration" }, new int[] {
						R.id.mTitle, R.id.mArtText, R.id.mDuration });
		listView.setAdapter(adapter);

		artImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(getActivity(), PlayingMain.class);
				startActivity(intent);
			}
		});

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
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		initMusicListAdapter();
		listItemInit();
		onItemLongPressedControler();
		btn_Control();
		initColor();
		updateUI();
		updateImageUI();
		UIMonitor.btnCheck();
		// if (!HAS_UPDATED_DB_ALL) {
		//
		// createAllSongsTable();
		// }

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mContext = activity.getApplicationContext();
		// 初始化
		MelodyPlayer.context = mContext;
		UIMonitor.context = mContext;
		// 注册广播
		registerBoradcastReceiver();
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
			Folder.UPDATE = 1;
			MyToast.makeText(mContext, "已添加", Toast.LENGTH_SHORT).show();
			break;

		case 3:
			Intent intent = new Intent(Intent.ACTION_SEND);

			intent.setType("text/plain");
			// intent.setType();//其他类型是什么？

			intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
			intent.putExtra(Intent.EXTRA_TEXT,
					"这首歌挺好听，推荐给你！歌名是：" + data.get(_index).get("title")
							+ ",一定要听哦！ 来自:Melody");
			startActivity(Intent.createChooser(intent, "分享到"));
			break;

		default:
			break;
		}

		return super.onContextItemSelected(item);

	}

	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		// TODO Auto-generated method stub

		// getActivity().getMenuInflater().inflate(R.menu.ac_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mRootView = inflater.inflate(R.layout.all, container, false);
		initActionBar();
		return mRootView;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		mContext.unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
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
				menu.add(0, 0, 0, "添加到我喜欢");
				menu.add(0, 3, 0, "分享");

			}
		});

	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub
		handler.post(new Runnable() {
			@Override
			public void run() {
				btnCheck();
				// UIMonitor.btnCheck();
				handler.postDelayed(this, 1000);
			}
		});
		super.onResume();
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onViewStateRestored(savedInstanceState);
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ACTION_NAME);
		myIntentFilter.addAction(ACTION_NAME_MONITOR);
		// 注册广播
		mContext.registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private void updateImageUI() {

		UIMonitor.btnCheck();
		ArtWorkUtils.getContent(artImage);
	}

	private void updateUI() {

		UIMonitor.btnCheck();
		if (MelodyPlayer.hasEverPlayed) {
			bottomInfo.setText(UIMonitor.updatePlayingSongInfo());
		} else {
			bottomInfo.setText("本地曲目：" + data.size() + "首");
		}
	}

}
