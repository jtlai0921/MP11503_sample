package jp.co.se.android.recipe.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Size;

public class CameraUtil {
    /**
     * // �q�˸m��ܾ����ؤo��ܳ̾A�X�Ӭ۾��w�����ؤo (Android��Sample ���ϥΪ���k)
     * 
     * @param sizes
     *            : �i�H�ϥΪ��w���ؤo
     * @param w
     *            : ��ܾ����e��
     * @param h
     *            : ��ܾ�������
     * @return
     */
    public static Size getOptimalPreviewSize(List<Size> sizes, int w, int h) {
        final double ASPECT_TOLERANCE = 0.1;
        double targetRatio = (double) w / h;
        if (sizes == null)
            return null;

        Size optimalSize = null;
        double minDiff = Double.MAX_VALUE;

        int targetHeight = h;

        // Try to find an size match aspect ratio and size
        for (Size size : sizes) {
            double ratio = (double) size.width / size.height;
            if (Math.abs(ratio - targetRatio) > ASPECT_TOLERANCE)
                continue;
            if (Math.abs(size.height - targetHeight) < minDiff) {
                optimalSize = size;
                minDiff = Math.abs(size.height - targetHeight);
            }
        }

        // Cannot find the one match the aspect ratio, ignore the requirement
        if (optimalSize == null) {
            minDiff = Double.MAX_VALUE;
            for (Size size : sizes) {
                if (Math.abs(size.height - targetHeight) < minDiff) {
                    optimalSize = size;
                    minDiff = Math.abs(size.height - targetHeight);
                }
            }
        }
        return optimalSize;
    }

    /**
     * ��^�Ӭ۾��i�ϥΪ��ؤo
     * 
     * @return
     */
    public static Size getSupportPictureSize(Camera camera) {
        Size pictureSize = null;
        List<Size> list = camera.getParameters().getSupportedPictureSizes();
        if (list != null && list.size() > 0) {
            // �]�w0���̰����ѪR��
            pictureSize = list.get(0);
        }
        return pictureSize;
    }

    /**
     * ��^�Ӭ۾��w�����䴩����
     * 
     * @return
     */
    public static List<String> getSupportFlash(Camera camera) {
        return camera.getParameters().getSupportedFlashModes();
    }

    /**
     * ��^�ե��Ū��䴩����
     * 
     * @return
     */
    public static List<String> getSupportWhiteBalance(Camera camera) {
        return camera.getParameters().getSupportedWhiteBalance();
    }

    /**
     * �T�{�O�_�i�H�䴩�۰ʹ�J
     * 
     * @return
     */
    public static boolean isSupportFocus(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }
}
