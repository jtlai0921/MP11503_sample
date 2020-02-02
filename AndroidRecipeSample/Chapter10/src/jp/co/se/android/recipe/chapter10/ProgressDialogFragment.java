package jp.co.se.android.recipe.chapter10;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

/**
 * Usage: show: {@link ProgressDialogFragment#newInstance(String)} hide:
 * {@link DialogFragment#dismissAllowingStateLoss()}
 */
public class ProgressDialogFragment extends DialogFragment {

    private static final String EXTRA_MESSAGE = "extra.message";
    public static final String FRG_TAG = "ProgressDialogFragment";

    public static ProgressDialogFragment newInstance(String message) {
        ProgressDialogFragment f = new ProgressDialogFragment();
        Bundle args = new Bundle();
        args.putString(EXTRA_MESSAGE, message);
        f.setArguments(args);
        return f;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.setMessage(getArguments().getString(EXTRA_MESSAGE));
        progressDialog.hide();

        return progressDialog;
    }
}
