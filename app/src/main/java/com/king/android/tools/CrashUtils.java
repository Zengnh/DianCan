package com.king.android.tools;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.lang.reflect.Field;

public
class CrashUtils implements Thread.UncaughtExceptionHandler {

    private Application context;

    private CrashUtils(Application context) {
        this.context = context;
    }

    public static void init(Application app) {
        Thread.setDefaultUncaughtExceptionHandler(new CrashUtils(app));
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable != null) {
            StringBuffer info = getDeviceInfo(context);
            info.append(getExceptionInfo(throwable));
            saveFile(info.toString());
        }
    }

    public StringBuffer getDeviceInfo(Context context) {
        StringBuffer sb = new StringBuffer();
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), PackageManager.GET_ACTIVITIES);
            if (pi != null) {
                String versionName = pi.versionName == null ? "null" : pi.versionName;
                String versionCode = pi.versionCode + "";
                sb.append("versionName").append(":").append(versionName).append("\n");
                sb.append("versionCode").append(":").append(versionCode).append("\n");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        Field[] fields = Build.class.getDeclaredFields();
        for (Field field : fields) {
            try {
                field.setAccessible(true);
                sb.append(field.getName()).append(":").append(field.get(null)).append("\n");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sb;
    }

    public String getExceptionInfo(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        Throwable cause = ex.getCause();
        // 循环着把所有的异常信息写入writer中
        while (cause != null) {
            cause.printStackTrace(pw);
            cause = cause.getCause();
        }
        pw.close();
        return writer.toString();
    }

    private void saveFile(String content) {
        File dir = context.getExternalFilesDir("crash");
        String fileName = System.currentTimeMillis() + ".txt";
        try {
            FileOutputStream fos = new FileOutputStream(new File(dir, fileName));
            fos.write(content.getBytes());
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
