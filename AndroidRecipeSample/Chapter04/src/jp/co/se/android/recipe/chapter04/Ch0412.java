package jp.co.se.android.recipe.chapter04;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.widget.Toast;

public class Ch0412 extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch0412_main);

        // Åã¥ÜDialogFragment‚ð•\Ž¦
        FragmentManager manager = getSupportFragmentManager();
        MyDialogFragment dialog = (MyDialogFragment) manager
                .findFragmentByTag("tag");
        if (dialog == null) {
            dialog = new MyDialogFragment();
            Bundle args = new Bundle();
            args.putString("message", "Message");
            dialog.setArguments(args);
            dialog.show(manager, "tag");
        }
    }

    public static class MyDialogFragment extends DialogFragment implements
            DialogInterface.OnClickListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // ¨ú±o­È’l‚ÌŽæ“¾
            String message = getArguments().getString("message");
            return new AlertDialog.Builder(getActivity()).setTitle("Title")
                    .setMessage(message).setPositiveButton("OK", this)
                    .setNegativeButton("Cancel", this).create();
        }

        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
            case AlertDialog.BUTTON_POSITIVE:
                Toast.makeText(getActivity(), "OK", Toast.LENGTH_SHORT).show();
                break;
            case AlertDialog.BUTTON_NEGATIVE:
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT)
                        .show();
                break;
            default:
                break;
            }
        }
    }
}
