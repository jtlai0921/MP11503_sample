package jp.co.se.android.recipe.chapter02;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.ActionBar.TabListener;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Ch0223 extends Activity implements TabListener {
    private static final int MAX_PAGES = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getActionBar();
        // 在分頁中設定Actionbar的導引模式
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        // 新增MAX_PAGES內文的分頁做為範例
        for (int i = 0; i < MAX_PAGES; i++) {
            String tabName = String.format("Tab(%1$s)", i);
            // 用目前的分頁名稱，新增建立另一個分頁
            actionBar.addTab(actionBar.newTab().setText(tabName)
                    .setTabListener(this));
        }
    }

    @Override
    public void onTabReselected(Tab tab, FragmentTransaction ft) {
    }

    @Override
    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // 當分頁被選擇時建立Fragment使畫面切換（此Fragment包含能顯示各分頁的View）
        ft.replace(android.R.id.content,
                PageFragment.newInstance(tab.getPosition()));
    }

    @Override
    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
    }

    /**
     * 本範例中的Fragment較單純，只是在畫面的中央顯示字串做為各分頁的畫面
     */
    public static class PageFragment extends Fragment {
        public static final String TAB_NAME = "Page";
        public static final String EXTRA_PAGE_NUM = "extra.pageNum";

        public static final PageFragment newInstance(int pageNum) {
            PageFragment f = new PageFragment();
            Bundle args = new Bundle();
            args.putInt(EXTRA_PAGE_NUM, pageNum);
            f.setArguments(args);

            return f;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            int pageNum = getArguments().getInt(EXTRA_PAGE_NUM);

            TextView textView = new TextView(getActivity());
            textView.setText(String.format("Page (%1$s)", pageNum));

            return textView;
        }
    }
}
