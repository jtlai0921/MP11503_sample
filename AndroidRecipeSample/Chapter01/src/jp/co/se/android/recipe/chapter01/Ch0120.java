package jp.co.se.android.recipe.chapter01;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

public class Ch0120 extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0120_main);

        String[] androids = new String[] { "Cupcake", "Donut", "Eclair",
                "Froyo", "Gingerbread", "Honeycomb", "Ice Cream Sandwich",
                "Jelly Bean", "KitKat" };

        AutoCompleteTextView textView = (AutoCompleteTextView) findViewById(R.id.autocomplete);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, androids);
        textView.setAdapter(adapter);
    }
}
