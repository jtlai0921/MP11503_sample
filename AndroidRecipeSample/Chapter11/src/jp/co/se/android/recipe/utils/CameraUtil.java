package jp.co.se.android.recipe.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Size;

public class CameraUtil {
    /**
     * // ±q¸Ë¸mÅã¥Ü¾¹ªº¤Ø¤o¿ï¾Ü³Ì¾A¦X·Ó¬Û¾÷¹wÄýªº¤Ø¤o (Android‚ÌSample ¤¤¨Ï¥Îªº¤èªk)
     * 
     * @param sizes
     *            : ¥i¥H¨Ï¥Îªº¹wÄý¤Ø¤o
     * @param w
     *            : Åã¥Ü¾¹ªº¼e«×
     * @param h
     *            : Åã¥Ü¾¹ªº°ª«×
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
     * ªð¦^·Ó¬Û¾÷¥i¨Ï¥Îªº¤Ø¤o
     * 
     * @return
     */
    public static Size getSupportPictureSize(Camera camera) {
        Size pictureSize = null;
        List<Size> list = camera.getParameters().getSupportedPictureSizes();
        if (list != null && list.size() > 0) {
            // ³]©w0¬°³Ì°ªªº¸ÑªR«×
            pictureSize = list.get(0);
        }
        return pictureSize;
    }

    /**
     * ªð¦^·Ó¬Û¾÷¹wÄýªº¤ä´©Ãþ«¬
     * 
     * @return
     */
    public static List<String> getSupportFlash(Camera camera) {
        return camera.getParameters().getSupportedFlashModes();
    }

    /**
     * ªð¦^¥Õ¥­¿Åªº¤ä´©Ãþ«¬
     * 
     * @return
     */
    public static List<String> getSupportWhiteBalance(Camera camera) {
        return camera.getParameters().getSupportedWhiteBalance();
    }

    /**
     * ½T»{¬O§_¥i¥H¤ä´©¦Û°Ê¹ïµJ
     * 
     * @return
     */
    public static boolean isSupportFocus(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }
}
