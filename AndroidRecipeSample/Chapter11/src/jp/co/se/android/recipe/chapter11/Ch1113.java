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

        // ���o���n�ާ@NFC�����
        mNfcAdapter = NfcAdapter.getDefaultAdapter(this);

        // �ˬd�O�_�f��NFC
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

            // ���o����
            Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

            if (tag != null) {
                // ���o��J��������r
                EditText etWrite = (EditText) findViewById(R.id.Write);
                String ndefMsg = etWrite.getText().toString();
                if (!TextUtils.isEmpty(ndefMsg)) {

                    // �إ�NdefRecord
                    NdefRecord[] ndefRecords = new NdefRecord[] { NdefRecord
                            .createUri(ndefMsg), };
                    // �إ�NdefMessage
                    NdefMessage msg = new NdefMessage(ndefRecords);

                    // �g�JNFC����
                    write(tag, msg);
                }
            }
        }
    }

    /**
     * �g�JNFC����
     * 
     * @param tag
     * @param msg
     */
    private void write(Tag tag, NdefMessage msg) {
        try {
            List<String> techList = Arrays.asList(tag.getTechList());
            // �T�{�n�g�J���������O�_���x�sNDEF���
            if (techList.contains(Ndef.class.getName())) {
                // ���]�tNDEF�����p
                Ndef ndef = Ndef.get(tag);
                try {
                    // �����bNDEF��ƤW�g�JNDEF�T��
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
                // �]�tNDEFFormattable�����p
                NdefFormatable ndeffmt = NdefFormatable.get(tag);
                try {
                    // �����@������NDEF�榡�ơB�@���g�JNDEF�T��
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
            // ��ܤw�g�J���\��Toast�T��
            Toast.makeText(this, getString(R.string.write_success),
                    Toast.LENGTH_SHORT).show();
        } catch (RuntimeException e) {
            // ��ܼg�J���Ѫ�Toast�T��
            Toast.makeText(this, getString(R.string.write_failure),
                    Toast.LENGTH_SHORT).show();
        }
    }
}

