package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

public class Ch0124 extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0123_main);

        Spinner myspinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> myadapter = ArrayAdapter.createFromResource(
                this, R.array.list_entries, R.layout.ch0124_spinner_item);
        myadapter.setDropDownViewResource(R.layout.ch0124_dropdown_item);
        myspinner.setAdapter(myadapter);
    }
}