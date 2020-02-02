package jp.co.se.android.recipe.chapter11;

import jp.co.se.android.recipe.utils.NfcUtil;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.widget.TextView;
import android.widget.Toast;

public class Ch1112 extends Activity {
    protected NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1112_main);

        // 取得要操作NFC的實例
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // 檢查是否搭載NFC功能
        if (mNfcAdapter != null) {
            // 檢查NFC功能是否生效
            if (!mNfcAdapter.isEnabled()) {
                // 當NFC功能為無效時則通知使用者
                Toast.makeText(getApplicationContext(),
                        getString(R.string.error_nfc_disable),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // 若沒有搭載NFC則通知使用者
            Toast.makeText(getApplicationContext(),
                    getString(R.string.error_nfc_nosupport), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            // 設定讓使用中的Activity能優先接收NFC
            Intent intent = new Intent(this, this.getClass())
                    .setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            PendingIntent pendingIntent = PendingIntent.getActivity(
                    getApplicationContext(), 0, intent, 0);
            mNfcAdapter.enableForegroundDispatch(this, pendingIntent, null,
                    null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mNfcAdapter != null) {
            // 當Activity變為非顯示時，則解除優先接收NFC的設定
            mNfcAdapter.disableForegroundDispatch(this);
        }
    }

    @SuppressLint("NewApi")
    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        String action = intent.getAction();
        if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)
                || NfcAdapter.ACTION_TECH_DISCOVERED.equals(action)) {

            // 取得IDm
            byte[] idm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

            // 將IDm轉換為字串顯示
            TextView tvRead = (TextView) findViewById(R.id.Read);
            tvRead.setText(NfcUtil.bytesToHex(idm));
        }
    }
}