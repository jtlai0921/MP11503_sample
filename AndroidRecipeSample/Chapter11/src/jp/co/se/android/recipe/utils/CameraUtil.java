package jp.co.se.android.recipe.utils;

import java.util.List;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Camera.Size;

public class CameraUtil {
    /**
     * // 從裝置顯示器的尺寸選擇最適合照相機預覽的尺寸 (Android�妴ample 中使用的方法)
     * 
     * @param sizes
     *            : 可以使用的預覽尺寸
     * @param w
     *            : 顯示器的寬度
     * @param h
     *            : 顯示器的高度
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
     * 返回照相機可使用的尺寸
     * 
     * @return
     */
    public static Size getSupportPictureSize(Camera camera) {
        Size pictureSize = null;
        List<Size> list = camera.getParameters().getSupportedPictureSizes();
        if (list != null && list.size() > 0) {
            // 設定0為最高的解析度
            pictureSize = list.get(0);
        }
        return pictureSize;
    }

    /**
     * 返回照相機預覽的支援類型
     * 
     * @return
     */
    public static List<String> getSupportFlash(Camera camera) {
        return camera.getParameters().getSupportedFlashModes();
    }

    /**
     * 返回白平衡的支援類型
     * 
     * @return
     */
    public static List<String> getSupportWhiteBalance(Camera camera) {
        return camera.getParameters().getSupportedWhiteBalance();
    }

    /**
     * 確認是否可以支援自動對焦
     * 
     * @return
     */
    public static boolean isSupportFocus(Context context) {
        return context.getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_AUTOFOCUS);
    }
}
