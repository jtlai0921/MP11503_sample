package jp.co.se.android.recipe.chapter16;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import android.app.Activity;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

public class Ch1604 extends Activity {
    private static final String TAG = "Ch1604";

    private static final String PACKAGE_NAME = "jp.co.se.android.recipe.chapter16";
    private static final File DATABASE_DIRECTORY = new File(
            Environment.getDataDirectory() + "/data/" + PACKAGE_NAME
                    + "/databases/");
    private static final File EXPORT_DIRECTORY = new File(
            Environment.getExternalStorageDirectory(), "MyBackup");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ch1604_main);

        findViewById(R.id.buttonExport).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (exportDb("backup.db")) {
                            Toast.makeText(Ch1604.this, "已匯出資料庫",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        findViewById(R.id.buttonRestore).setOnClickListener(
                new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (restoreDb("backup.db")) {
                            Toast.makeText(Ch1604.this, "已還原資料庫",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    public static boolean exportDb(String backupFile) {
        File dbFile = new File(DATABASE_DIRECTORY,
                ContactDbOpenHelper.DATABASE_NAME);
        File exportFile = new File(EXPORT_DIRECTORY, backupFile);

        if (!EXPORT_DIRECTORY.exists()) {
            EXPORT_DIRECTORY.mkdirs();
        }

        try {
            exportFile.createNewFile();
            copyFile(dbFile, exportFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean restoreDb(String backupFile) {
        if (!Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            return false;
        }
        File dbFile = new File(DATABASE_DIRECTORY,
                ContactDbOpenHelper.DATABASE_NAME);
        File importFile = new File(EXPORT_DIRECTORY, backupFile);

        if (!importFile.exists()) {
            Log.d(TAG, "匯入的檔案不存在:" + importFile.getAbsolutePath());
            return false;
        }

        try {
            dbFile.createNewFile();
            copyFile(importFile, dbFile);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }
}
