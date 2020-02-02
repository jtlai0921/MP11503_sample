package jp.co.se.android.recipe.chapter13;

import jp.co.se.android.recipe.chapter13.R;
import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.Toast;

public class Ch1303 extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1303_main);

        Switch installAppSwitch = (Switch) findViewById(R.id.InstallAppSwitch);
        installAppSwitch
                .setOnCheckedChangeListener(new OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView,
                            boolean isChecked) {
                        if (isChecked) {
                            // �ҰʪA�ȃT�[�r�X�J�n
                            startInstallAppService();
                        } else {
                            // �����A��
                            stopInstallAppService();
                        }
                    }
                });

    }

    @Override
    protected void onDestroy() {
        stopInstallAppService();
        super.onDestroy();
    }

    /**
    * �Ұʺ�ť�w�˪��A��
     */
    private void startInstallAppService() {
        Intent service = new Intent(Ch1303.this, DetectInstallAppService.class);
        startService(service);
    }

    /**
     * ������ť�w�˪��A��
     */
    private void stopInstallAppService() {
        Intent service = new Intent(Ch1303.this, DetectInstallAppService.class);
        stopService(service);
    }

    /**
     * ��ť�w�˻P�Ϧw�˪��A�ȮɡAActivity�u�����̤W�h�~���ť�A�ҥH�ݭn�ϥΦ����A��
     */
    public static class DetectInstallAppService extends Service {
        private InstallAppReceiver mInstallAppReciever = null;

        @Override
        public IBinder onBind(Intent intent) {
            return null;
        }

        @Override
        public void onCreate() {
            super.onCreate();
            // ���U��ť�w�˻P�Ϧw�˪��s��������
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addDataScheme("package");
            intentFilter.addAction(Intent.ACTION_PACKAGE_ADDED);
            intentFilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
            mInstallAppReciever = new InstallAppReceiver();
            registerReceiver(mInstallAppReciever, intentFilter);
        }

        @Override
        public void onDestroy() {
            // �P����ť�w�˻P�Ϧw�˪��s��������
            if (mInstallAppReciever != null) {
                unregisterReceiver(mInstallAppReciever);
            }
            super.onDestroy();
        }

        /**
         * ��ť�w�˻P�Ϧw�˪��s��������
         */
        private class InstallAppReceiver extends BroadcastReceiver {
            @Override
            public void onReceive(Context context, Intent intent) {
                String action = intent.getAction();
                if (action.indexOf("PACKAGE_ADDED") != -1) {
                    // ��ť�w��
                    Toast.makeText(context,
                            getString(R.string.label_detect_installapp),
                            Toast.LENGTH_SHORT).show();
                }
                if (action.indexOf("PACKAGE_REMOVED") != -1) {
                    // �Ѱ��w�˪���ť
                    Toast.makeText(context,
                            getString(R.string.label_detect_uninstallapp),
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
