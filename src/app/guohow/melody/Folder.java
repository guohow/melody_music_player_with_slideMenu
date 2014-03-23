package app.guohow.melody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnCreateContextMenuListener;
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

import com.guohow.melody_sildemenu.R;

public class Folder extends Activity {

	TextView bottomInfo;
	public static ListView mFavourList;
	Button btn_play, btn_next, btn_previous, btn_playMod, btn_fav;
	public static int UPDATE = 1;
	private static SQLHelper dbHelper;
	private static String DB_NAME = "mTable.db";
	private static int DB_VERSION = 1;
	private static int _index;
	public static SQLiteDatabase db;
	public static SQLiteDatabase db2;
	private Cursor cursor;
	static List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	Handler handler = new Handler();
	ImageView artImage;
	private static String ACTION_NAME = "SONG_CHANGED";
	private static String ACTION_NAME_MONITOR = "BTN_PRESSED";

	BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if (action.equals(ACTION_NAME)) {
				System.out.println(" Folder list 正在处理广播");
				updateUI();
				updateImageUI();
			}
			if (action.equals(ACTION_NAME_MONITOR)) {
				UIMonitor.btnCheck();
				System.out.println("folder list 正在处理按钮广播");
			}
		}

	};

	// 初始化按钮，并将按钮交给MONITOR处理
	private void btn_Control() {
		btn_play = (Button) findViewById(R.id.play);
		btn_next = (Button) findViewById(R.id.next);
		btn_previous = (Button) findViewById(R.id.previous);
		btn_playMod = (Button) findViewById(R.id.playMod);
		btn_fav = (Button) findViewById(R.id.btn_set_fav_f);

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

	// 创建、查询
	private void createCursorTable() {

		try {
			// 初始化&创建数据库
			dbHelper = new SQLHelper(this, DB_NAME, null, DB_VERSION);
			// 创建表
			// TABLE_NAME数据库名 *.db
			// SQLHelper.TABLE_NAME 表名
			db = dbHelper.getWritableDatabase(); // 自动调用SQLiteHelper.OnCreate()
			/* 查询表，得到cursor对象 */
			cursor = db.query(SQLHelper.TABLE_NAME, null, null, null, null,
					null, "title" + " DESC");
			cursor.moveToFirst();
			data.clear();
			while (!cursor.isAfterLast() && cursor.getString(1) != null) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				// mList casue error????????????guohow
				// not List
				map.put("title", cursor.getString(1));
				map.put("artist", cursor.getString(2));
				map.put("url", cursor.getString(3));

				data.add(map);
				cursor.moveToNext();
			}

		} catch (IllegalArgumentException e) {
			e.printStackTrace();
			// 当版本变更时会调用SQLiteHelper.onUpgrade()方法重建表
			// 注：表以前数据将丢失
			++DB_VERSION;
			dbHelper.onUpgrade(db, --DB_VERSION, DB_VERSION);
		}

	}

	// 获取主题设置
	private boolean getMod() {
		SharedPreferences spf = PreferenceManager
				.getDefaultSharedPreferences(this);

		return spf.getBoolean("mod_perf", false);

	}

	private void initActionBarHome() {
		// 将应用程序图标设置为可点击的按钮
		getActionBar().setHomeButtonEnabled(true);
		// 将应用程序图标设置为可点击的按钮,并且在图标上添加向左的箭头
		// 该句代码起到了决定性作用
		getActionBar().setDisplayHomeAsUpEnabled(true);
	}

	// 初始化主题设置
	private void initColor() {
		if (getMod()) {
			mFavourList.setBackgroundResource(R.color.bg_main);
			mFavourList.setSelector(R.drawable.list_selecter_night);
			bottomInfo.setBackgroundResource(R.color.bg_dark_tab);
			setTheme(android.R.style.Theme_Holo);
			getActionBar().setBackgroundDrawable(
					getResources().getDrawable(R.color.bg_dark_tab));
		}
	}

	private void initList() {
		mFavourList = (ListView) findViewById(R.id.FavSongsList);
		bottomInfo = (TextView) findViewById(R.id.info_fav);
		artImage = (ImageView) findViewById(R.id.artIcon_fav);

		// ADAPTER
		if (data != null) {
			mFavourList.setAdapter(new MySimpleAdapter(this, data,
					R.layout.favouritem, new String[] { "title", "artist",
							"url" }, new int[] { R.id.mMTitle, R.id.mSummary,
							R.id.mFUrl }) {

			});
		}

		artImage.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				Intent intent = new Intent();
				intent.setClass(Folder.this, PlayingMain.class);
				startActivity(intent);
			}
		});

	}

	private void listItemAda() {

		mFavourList.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				// TODO Auto-generated method stub

				if (MelodyPlayer.hasEverPlayed && MelodyPlayer.player != null) {
					MelodyPlayer.current = position;
					MelodyPlayer.itemList = data;
					MelodyPlayer.player.stop();
					MelodyPlayer.player.play();
				} else {
					MelodyPlayer.startPlaying(position, data);
				}

			}

		});
	}

	// 长按菜单响应
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 6:
			db.delete(SQLHelper.TABLE_NAME, "title" + "=?",
					new String[] { (String) data.get(_index).get("title") });
			UPDATE = 1;
			MyToast.makeText(this, "已移除", Toast.LENGTH_SHORT).show();
			break;

		case 9:
			Intent intent = new Intent(Intent.ACTION_SEND);
			intent.setType("text/plain");
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
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.folder);
		setUI();
		initActionBarHome();
		setListBtn();
		MelodyPlayer.context = this;
		UIMonitor.context = this;
		// 注册广播
		registerBoradcastReceiver();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		getMenuInflater().inflate(R.menu.folder_ac_menu, menu);
		return super.onCreateOptionsMenu(menu);
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		unregisterReceiver(mBroadcastReceiver);
		super.onDestroy();
	}

	private void onItemLongPressedControler() {
		mFavourList
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu,
							View arg1, ContextMenuInfo menuInfo) {
						// TODO Auto-generated method stub
						_index = ((AdapterContextMenuInfo) menuInfo).position;// 获取menu点击项的position

						menu.setHeaderIcon(R.drawable.icon_share_sns_music_circle);
						menu.setHeaderTitle(data.get(_index).get("title")
								.toString());

						menu.add(0, 6, 0, "移出收藏列表");
						menu.add(0, 9, 0, "分享");
					}
				});
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		switch (item.getItemId()) {
		case android.R.id.home:
			finish();
			return true;
		case R.id.action_menu_jump_f:
			Intent intent = new Intent();
			intent.setClass(this, PlayingMain.class);
			startActivity(intent);
			return true;
		case R.id.action_menu_help_f:
			MyToast.makeText(this, "您可以点击返回按键返回上层目录", Toast.LENGTH_LONG).show();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		handler.post(new Runnable() {
			@Override
			public void run() {

				if (UPDATE == 1) {
					createCursorTable();
					// 初始化列表
					initList();
					UPDATE = 0;
				}
				btnCheck();
				handler.postDelayed(this, 2000);
			}
		});
		super.onResume();
	}

	public void registerBoradcastReceiver() {
		IntentFilter myIntentFilter = new IntentFilter();
		myIntentFilter.addAction(ACTION_NAME);
		myIntentFilter.addAction(ACTION_NAME_MONITOR);
		// 注册广播
		registerReceiver(mBroadcastReceiver, myIntentFilter);
	}

	private void setListBtn() {
		// TODO Auto-generated method stub

		// 表操作
		if (UPDATE == 1) {
			createCursorTable();
			UPDATE = 0;
		}
		// 初始化列表
		initList();
		// 监听列表事件
		listItemAda();
		// 长按事件监听
		onItemLongPressedControler();
		// 按钮监听
		btn_Control();
		initColor();
		updateUI();
		updateImageUI();
		UIMonitor.btnCheck();

	}

	private void setUI() {

		getActionBar().setLogo(R.drawable.ac_img_menu_favour);
		getActionBar().setBackgroundDrawable(
				getResources().getDrawable(R.drawable.actionbar_bg));
		getActionBar().setTitle("");
	}

	private void updateImageUI() {
		UIMonitor.btnCheck();
		System.out.println(artImage + "---------------");
		ArtWorkUtils.getContent(artImage);

	}

	private void updateUI() {

		UIMonitor.btnCheck();
		if (MelodyPlayer.hasEverPlayed) {
			bottomInfo.setText(UIMonitor.updatePlayingSongInfo());
		} else {
			bottomInfo.setText("收藏曲目：" + data.size() + "首");
		}
	}
}
