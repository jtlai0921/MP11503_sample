package jp.co.se.android.recipe.chapter03;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Ch0312 extends FragmentActivity {

    private FragmentTabHost mTabHost;
    private String[] tags = new String[] { "Tab1", "Tab2" };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0312_main);
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        for (String tag : tags) {
            Bundle bundle = new Bundle();
            bundle.putString("value", tag);
            mTabHost.addTab(mTabHost.newTabSpec(tag).setIndicator(tag),
                    TabFragment.class, bundle);
        }
    }

    public static class TabFragment extends Fragment {

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
