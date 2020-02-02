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

        // ���o�n�ާ@NFC�����
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // �ˬd�O�_�f��NFC�\��
        if (mNfcAdapter != null) {
            // �ˬdNFC�\��O�_�ͮ�
            if (!mNfcAdapter.isEnabled()) {
                // ��NFC�\�ର�L�Įɫh�q���ϥΪ�
                Toast.makeText(getApplicationContext(),
                        getString(R.string.error_nfc_disable),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // �Y�S���f��NFC�h�q���ϥΪ�
            Toast.makeText(getApplicationContext(),
                    getString(R.string.error_nfc_nosupport), Toast.LENGTH_SHORT)
                    .show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mNfcAdapter != null) {
            // �]�w���ϥΤ���Activity���u������NFC
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
            // ��Activity�ܬ��D��ܮɡA�h�Ѱ��u������NFC���]�w
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

            // ���oIDm
            byte[] idm = intent.getByteArrayExtra(NfcAdapter.EXTRA_ID);

            // �NIDm�ഫ���r�����
            TextView tvRead = (TextView) findViewById(R.id.Read);
            tvRead.setText(NfcUtil.bytesToHex(idm));
        }
    }
}