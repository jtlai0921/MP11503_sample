package jp.co.se.android.recipe.chapter03;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Ch0314 extends FragmentActivity {

	private ViewPager mPager;
	private ViewPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ch0314_main);

		mAdapter = new ViewPagerAdapter(getSupportFragmentManager());
		mPager = (ViewPager) findViewById(R.id.pager);
		mPager.setAdapter(mAdapter);
	}

    public static class ViewPagerAdapter extends FragmentPagerAdapter {

		private String[] values = new String[] { "page1", "page2" };

		public ViewPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			ViewPagerFragment fragment = new ViewPagerFragment();
			Bundle args = new Bundle();
			args.putString("value", values[position]);
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public int getCount() {
			return values.length;
		}
	}

	public static class ViewPagerFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			TextView textView = new TextView(getActivity());
			textView.setGravity(Gravity.CENTER);
			textView.setText(getArguments().getString("value"));
			return textView;
		}
	}

}
