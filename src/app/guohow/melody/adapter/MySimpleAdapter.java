package app.guohow.melody.adapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.widget.SectionIndexer;
import android.widget.SimpleAdapter;

public class MySimpleAdapter extends SimpleAdapter implements SectionIndexer {

	HashMap<String, Integer> alphaIndexer;
	String[] sections;

	@SuppressLint("DefaultLocale")
	public MySimpleAdapter(Context context,
			List<? extends Map<String, ?>> data, int resource, String[] from,
			int[] to) {
		super(context, data, resource, from, to);
		// TODO Auto-generated constructor stub
		alphaIndexer = new HashMap<String, Integer>();
		int size = data.size();

		for (int x = 0; x < size; x++) {

			// get the first letter of the store
			String ch = ((String) data.get(x).get("title")).substring(0, 1);

			// convert to uppercase otherwise lowercase a -z will be sorted
			// after upper A-Z
			ch = ch.toUpperCase();

			// HashMap will prevent duplicates
			alphaIndexer.put(ch, x);
		}

		Set<String> sectionLetters = alphaIndexer.keySet();

		// create a list from the set to sort
		ArrayList<String> sectionList = new ArrayList<String>(sectionLetters);

		Collections.sort(sectionList);

		sections = new String[sectionList.size()];

		sectionList.toArray(sections);
	}

	@Override
	public int getPositionForSection(int arg0) {
		// TODO Auto-generated method stub
		// return 0;
		return alphaIndexer.get(sections[arg0]);
	}

	@Override
	public int getSectionForPosition(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object[] getSections() {
		// TODO Auto-generated method stub
		// return null;
		return sections;
	}

}
