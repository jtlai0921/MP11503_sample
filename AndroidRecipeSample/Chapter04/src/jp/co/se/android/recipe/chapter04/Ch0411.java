package jp.co.se.android.recipe.chapter04;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

public class Ch0411 extends FragmentActivity implements OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0411_main);

        findViewById(R.id.btn_add_code).setOnClickListener(this);
        findViewById(R.id.btn_add_xml).setOnClickListener(this);
        findViewById(R.id.btn_remove).setOnClickListener(this);
    }

    public static class MyCodeFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            TextView textView = new TextView(getActivity());
            textView.setLayoutParams(new ViewGroup.LayoutParams(
                    LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
            textView.setGravity(Gravity.CENTER);
            textView.setText(getArguments().getString("value"));

            return textView;
        }
    }

    public static class MyXmlFragment extends Fragment {

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            return inflater.inflate(R.layout.ch0411_fragment, container, false);
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setGravity(Gravity.CENTER);
            textView.setText(getArguments().getString("value"));
        }
    }

    @Override
    public void onClick(View v) {
        FragmentManager manager = getSupportFragmentManager();
        Fragment fragment = manager.findFragmentById(R.id.parent);
        if (R.id.btn_add_code == v.getId() || R.id.btn_add_xml == v.getId()) {
            if (fragment == null) {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.add(R.id.parent, getMyFragment(v.getId(), true),
                        "MyFragment");
                transaction.commit();
            } else {
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.parent,
                        getMyFragment(v.getId(), false));
                transaction.commit();
            }
        } else if (R.id.btn_remove == v.getId() && fragment != null) {
            FragmentTransaction transaction = manager.beginTransaction();
            transaction.remove(fragment);
            transaction.commit();
        }
    }

    private Fragment getMyFragment(int _id, boolean _add) {
        Fragment fragment = null;
        if (R.id.btn_add_code == _id) {
            fragment = new MyCodeFragment();
        } else {
            fragment = new MyXmlFragment();
        }
        Bundle bundle = new Bundle();
        if (_add) {
            bundle.putString("value", "Add Fragment");
        } else {
            bundle.putString("value", "Replace Fragment");
        }
        fragment.setArguments(bundle);

        return fragment;
    }
}
