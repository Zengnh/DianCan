package com.king.android.ui;

import android.Manifest;
import android.content.Intent;
import android.os.Vibrator;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.king.android.databinding.ActivityQrcodeBinding;
import com.king.base.activity.BaseActivity;

import cn.bingoogolapple.qrcode.core.QRCodeView;

public class QRCodeActivity extends BaseActivity<ActivityQrcodeBinding> implements QRCodeView.Delegate {

    private static final String TAG = "QRCodeActivity";
    private static final int REQUEST_CODE_CAMERA = 999;
    private String tagName;

    @Override
    public void init() {
        tagName = getIntentData().getString(0);
        binding.zxScan.setDelegate(this);

        binding.lightLayout.setOnClickListener(new View.OnClickListener() {
            boolean isLight = false;
            @Override
            public void onClick(View view) {
                isLight = !isLight;
                if (isLight){
                    binding.zxScan.openFlashlight();
                    binding.lightTv.setText("關閉");
                }else {
                    binding.zxScan.closeFlashlight();
                    binding.lightTv.setText("打開");
                }
            }
        });
        binding.closeIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        Log.d(TAG, "onScanQRCodeSuccess: "+result);
        vibrate();//震动

        try {
            String[] split = result.split("樓|號");

            Intent intent = new Intent();
            intent.setAction(tagName);
            intent.putExtra("floor",split[0]);
            intent.putExtra("number",split[1]);
            sendBroadcast(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "二維碼無效,請重新掃描", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onCameraAmbientBrightnessChanged(boolean isDark) {

    }

    @Override
    public void onScanQRCodeOpenCameraError() {

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == 0) {
            binding.zxScan.startCamera();
            binding.zxScan.startSpot();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, REQUEST_CODE_CAMERA);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        binding.zxScan.showScanRect();
    }

    @Override
    protected void onStop() {
        binding.zxScan.stopCamera();
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        binding.zxScan.closeFlashlight();
        binding.zxScan.onDestroy();
        super.onDestroy();
    }

    //震动器
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(100);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_CAMERA) {
            binding.zxScan.startCamera();
            binding.zxScan.startSpot();
        }
    }
}
