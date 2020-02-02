package jp.co.se.android.recipe.chapter04;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.Browser;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager.LoaderCallbacks;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.SimpleCursorAdapter;
import android.widget.ListView;

public class Ch0413 extends FragmentActivity implements LoaderCallbacks<Cursor> {

    private SimpleCursorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0413_main);
        adapter = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1, null,
                new String[] { Browser.BookmarkColumns.TITLE },
                new int[] { android.R.id.text1 }, 0);

        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);

        getSupportLoaderManager().initLoader(0, savedInstanceState, this);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        return new CursorLoader(this, Browser.BOOKMARKS_URI, null, null, null,
                null);
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
        adapter.swapCursor(cursor);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        adapter.swapCursor(null);
    }
}
