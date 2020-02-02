package jp.co.se.android.recipe.chapter11;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;
import android.nfc.tech.NdefFormatable;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.text.TextUtils;
import android.widget.EditText;
import android.widget.Toast;

public class Ch1113 extends Activity {
    protected NfcAdapter mNfcAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1113_main);

        // 取得為要操作NFC的實例
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // 檢查是否搭載NFC
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

            // 取得分頁
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if (tag != null) {
                // 取得輸入完成的文字
                EditText etWrite = (EditText) findViewById(R.id.Write);
                String ndefMsg = etWrite.getText().toString();
                if (!TextUtils.isEmpty(ndefMsg)) {

                    // 建立NdefRecord
                    NdefRecord[] ndefRecords = new NdefRecord[] { NdefRecord
                            .createUri(ndefMsg), };
                    // 建立NdefMessage
                    NdefMessage msg = new NdefMessage(ndefRecords);

                    // 寫入NFC分頁
                    write(tag, msg);
                }
            }
        }
    }

    /**
     * 寫入NFC分頁
     * 
     * @param tag
     * @param msg
     */
    private void write(Tag tag, NdefMessage msg) {
        try {
            List<String> techList = Arrays.asList(tag.getTechList());
            // 確認要寫入的分頁中是否有儲存NDEF資料
            if (techList.contains(Ndef.class.getName())) {
                // 有包含NDEF的情況
                Ndef ndef = Ndef.get(tag);
                try {
                    // 直接在NDEF資料上寫入NDEF訊息
                    ndef.connect();
                    ndef.writeNdefMessage(msg);
                } catch (IOException e) {
                    throw new RuntimeException(
                            getString(R.string.error_connect), e);
                } catch (FormatException e) {
                    throw new RuntimeException(
                            getString(R.string.error_format), e);
                } finally {
                    try {
                        ndef.close();
                    } catch (IOException e) {
                    }
                }
            } else if (techList.contains(NdefFormatable.class.getName())) {
                // 包含NDEFFormattable的情況
                NdefFormatable ndeffmt = NdefFormatable.get(tag);
                try {
                    // 直接一面執行NDEF格式化、一面寫入NDEF訊息
                    ndeffmt.connect();
                    ndeffmt.format(msg);
                } catch (IOException e) {
                    throw new RuntimeException(
                            getString(R.string.error_connect), e);
                } catch (FormatException e) {
                    throw new RuntimeException(
                            getString(R.string.error_format), e);
                } finally {
                    try {
                        ndeffmt.close();
                    } catch (IOException e) {
                    }
                }
            }
            // 顯示已寫入成功的Toast訊息
            Toast.makeText(this, getString(R.string.write_success),
                    Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            // 顯示寫入失敗的Toast訊息
            Toast.makeText(this, getString(R.string.write_failure),
                    Toast.LENGTH_SHORT).show();
        }
    }
}

