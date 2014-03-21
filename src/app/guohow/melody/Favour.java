package app.guohow.melody;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
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

import com.guohow.melody_sildemenu.R;

public class Favour extends Fragment {
	protected View mRootView;
	TextView bottomInfo;
	public static ListView mFavourList;
	Button btn_play, btn_next, btn_previous, btn_playMod;
	SeekBar seekBar;
	protected Context mContext;
	public static int UPDATE = 0;
	private static SQLHelper dbHelper;
	private static String DB_NAME = "mTable.db";
	private static int DB_VERSION = 1;
	private static int _index;
	public static SQLiteDatabase db;
	public static SQLiteDatabase db2;
	private Cursor cursor;
	static List<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
	Handler handler = new Handler();

	// 数据库

	public Favour() {
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
		// add by guohow

		mRootView = inflater.inflate(R.layout.favourite, container, false);
		getActivity().getActionBar().setLogo(R.drawable.ic_fav);
		getActivity().getActionBar().setTitle("我的收藏");
		return mRootView;
	}

	@Override
	public void onResume() {
		// TODO Auto-generated method stub

		handler.post(new Runnable() {
			@Override
			public void run() {
				PlayerUIMonitor.btnCheck();
				if (UPDATE == 1) {
					createCursorTable();
					// 初始化列表
					initList();
					UPDATE = 0;
				}
				if (bottomInfo != null && MelodyPlayer.hasEverPlayed) {
					PlayerUIMonitor.updatePlayingSongInfo();
					bottomInfo.setText("正在播放:"
							+ PlayerUIMonitor.playingSongTitle + "  " + "艺术家:"
							+ PlayerUIMonitor.playingSongArtist);

				}

				handler.postDelayed(this, 1000);
			}
		});
		super.onResume();
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		// 表操作
		createCursorTable();
		// 初始化列表
		initList();
		// 监听列表事件
		listItemAda();
		// 长按事件监听
		onItemLongPressedControler();
		// 按钮监听
		btn_Control();

		super.onActivityCreated(savedInstanceState);
	}

	@Override
	public void onViewStateRestored(Bundle savedInstanceState) {
		// TODO Auto-generated method stub

		super.onViewStateRestored(savedInstanceState);
	}

	// 创建、查询
	private void createCursorTable() {

		try {
			// 初始化&创建数据库
			dbHelper = new SQLHelper(mRootView.getContext(), DB_NAME, null,
					DB_VERSION);
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

	private void initList() {
		mFavourList = (ListView) mRootView.findViewById(R.id.FavSongsList);
		// 更新data

		bottomInfo = (TextView) mRootView.findViewById(R.id.info_fav);
		//护眼模式
		initColor();
		bottomInfo.setText("红心队列：" + data.size() + "首");
		// simpleAdapter
		if (data != null) {
			mFavourList.setAdapter(new MySimpleAdapter(mContext, data,
					R.layout.favouritem, new String[] { "title", "artist",
							"url" }, new int[] { R.id.mMTitle, R.id.mSummary,
							R.id.mFUrl }) {

			});
		}

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
				} else
					MelodyPlayer.startPlaying(position, data);

			}
		});

		// mFavourList.setOnScrollListener(new OnScrollListener() {
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

						menu.add(0, 6, 0, "移出我喜欢");
						// menu.add(0, 8, 0, "设为铃声");
						menu.add(0, 9, 0, "分享");

					}
				});

	}

	// 长按菜单响应函数
	@Override
	public boolean onContextItemSelected(MenuItem item) {

		switch (item.getItemId()) {

		case 6:
			db.delete(SQLHelper.TABLE_NAME, "title" + "=?",
					new String[] { (String) data.get(_index).get("title") });
			UPDATE = 1;

			break;

		case 9:
			Intent intent = new Intent(Intent.ACTION_SEND);

			intent.setType("text/plain");
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
	private boolean getMod() {
		SharedPreferences spf = PreferenceManager
				.getDefaultSharedPreferences(mContext);
		
		return spf.getBoolean("mod_perf",false);

	}

	private void initColor() {
		if(getMod()){
			mFavourList.setBackgroundResource(R.color.darker_gray);
			mFavourList.setSelector(R.drawable.list_selecter_night);
			bottomInfo.setBackgroundResource(R.color.bg_dark_tab);
			
		}
	}
}
