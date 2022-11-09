package com.king.base.utils;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;


public class FileUtil {

    /**
     * 获取assets下的文件
     *
     * @param context
     * @param fileName
     * @return
     */
    public static String getAssetsText(Context context, String fileName) {
        try {
           return readText(context.getAssets().open(fileName));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String readText(InputStream in){
        String str = null;
        try {
            byte b[] = new byte[in.available()];
            int len = 0;
            int temp = 0;          //所有读取的内容都使用temp接收
            while ((temp = in.read()) != -1) {    //当没有读取完时，继续读取
                b[len] = (byte) temp;
                len++;
            }
            str = new String(b, 0, len);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return str;
    }

    /**
     * 保存文件
     * @param path
     * @param fileName
     * @param content
     */
    public static void writeText(String path,String fileName,String content){
        OutputStream os = null;
        try {

            File tempFile = new File(path);
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            os = new FileOutputStream(tempFile.getPath() + File.separator + fileName);
            os.write(content.getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                os.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     *     <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" />
     *      <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE" />
     *
     * application
     *      android:requestLegacyExternalStorage="true"
     *      >
     *
     * 打开图片选择器
     * @param context
     */
    public static void chooseImage(Activity context,int requestCode) {
        if (Build.VERSION.SDK_INT >= 23) {
            String[] permissions = {
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
            for (String str : permissions) {
                if (context.checkSelfPermission(str) != PackageManager.PERMISSION_GRANTED) {
                    context.requestPermissions(permissions, requestCode);
                    return;
                }
            }
        }
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        context.startActivityForResult(intent, requestCode);
    }

    /**
     * 从相册中读取图片,在4.4之后
     *
     * @param uri:打开图片选择后返回的intent
     * @param context
     * @return
     */
    public static String uriToPath(Uri uri ,Context context) {
        String imagePath = null;
        if (DocumentsContract.isDocumentUri(context, uri)) {
            // 如果是document类型的Uri，则通过document id处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                String id = docId.split(":")[1]; // 解析出数字格式的id
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection, context);
            } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(docId));
                imagePath = getImagePath(contentUri, null, context);
            }
        } else if ("content".equals(uri.getScheme().toLowerCase())) {
            // 如果是content类型的Uri，则使用普通方式处理
            imagePath = getImagePath(uri, null, context);
        } else if ("file".equals(uri.getScheme().toLowerCase())) {
            // 如果是file类型的Uri，直接获取图片路径即可
            imagePath = uri.getPath();
        }
        return imagePath;
    }

    /**
     * 查询图库中是否存在有指定路径的图片
     *
     * @param uri:路径URI
     * @param selection:筛选条件
     * @param context
     * @return
     */
    @SuppressLint("Range")
    private static String getImagePath(Uri uri, String selection, Context context) {
        String path = null;
        // 通过Uri和selection来获取真实的图片路径
        Cursor cursor = context.getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    /**
     * 压缩图片
     * @param context
     * @param bitmap
     * @return
     */
    public static File compressImage(Context context, Bitmap bitmap){
        byte[] bytes = compressImage(bitmap, 300);

        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
        String filename = format.format(new Date());
        File file = new File(context.getExternalFilesDir("image"),filename+".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(bytes);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (!bitmap.isRecycled()){
            bitmap.recycle();
        }
        return file;
    }

    /**
     * 压缩图片
     * @param bitmap
     * @param compressSize 压缩图片的大小 单位 k
     * @return
     */
    public static byte[] compressImage(Bitmap bitmap,int compressSize){
        int options = 100;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,options,baos);
        byte[] bytes = null;
        while ((bytes = baos.toByteArray()).length/1024>compressSize){
            baos.reset();
            options -= 10;
            bitmap.compress(Bitmap.CompressFormat.JPEG,options,baos);
        }
        return bytes;
    }

    /**
     * 复制文件
     * @param input
     * @param output
     * @throws IOException
     */
    public static void copy(InputStream input, OutputStream output) throws IOException {
        try {
            byte[] buf = new byte[1024];
            int bytesRead;
            while ((bytesRead = input.read(buf)) != -1) {
                output.write(buf, 0, bytesRead);
            }
        } finally {
            input.close();
            output.close();
        }
    }

}
