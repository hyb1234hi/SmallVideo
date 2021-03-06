package com.hzl.smallvideo.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.hzl.smallvideo.application.MainApplication;

import java.io.File;
import java.io.FileFilter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;

/**
 * 作者：请叫我百米冲刺 on 2017/9/29 下午1:52
 * 邮箱：mail@hezhilin.cc
 */

public class AppUtil {

    private static int screenWidth;

    private static int screenHeight;

    private static final FileFilter CPU_FILTER = new FileFilter() {
        @Override
        public boolean accept(File pathname) {
            String path = pathname.getName();
            if (path.startsWith("cpu")) {
                for (int i = 3; i < path.length(); i++) {
                    if (path.charAt(i) < '0' || path.charAt(i) > '9') {
                        return false;
                    }
                }
                return true;
            }
            return false;
        }
    };

    public static int getScreenWidth() {
        if (screenWidth == 0) {
            WindowManager wm = (WindowManager) MainApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
            screenWidth = wm.getDefaultDisplay().getWidth();
        }
        return screenWidth;
    }

    public static int getScreenHeight() {
        if (screenHeight == 0) {
            //默认的高度获取方式
            WindowManager wm = (WindowManager) MainApplication.getInstance().getSystemService(Context.WINDOW_SERVICE);
            screenHeight = wm.getDefaultDisplay().getHeight();
            //带有虚拟键盘的需要宁外获取
            Display display = wm.getDefaultDisplay();
            DisplayMetrics dm = new DisplayMetrics();
            Class c;
            try {
                c = Class.forName("android.view.Display");
                Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
                method.invoke(display, dm);
                screenHeight = dm.heightPixels;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return screenHeight;
    }

    public static void saveBitmapToFile(Bitmap bm, String filePath) {
        File file = new File(filePath);
        try {
            FileOutputStream out = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 100, out);
            out.flush();
            out.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取设备的cpu核心数
     **/
    public static int getCpuCores() {
        int cores = 4;
        try {
            cores = new File("/sys/devices/system/cpu/").listFiles(CPU_FILTER).length;
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return cores;
    }


    public static int px2dip(float pxValue) {
        final float scale = MainApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    public static int dip2px(float dpValue) {
        final float scale = MainApplication.getInstance().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
