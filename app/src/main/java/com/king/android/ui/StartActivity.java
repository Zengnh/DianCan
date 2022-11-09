package com.king.android.ui;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.XXPermissions;
import com.king.android.service.GpsService;

import java.util.List;

public
class StartActivity extends Activity {

    private static final String[] permissions = {
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (isOpenGps(this)){
            next();
        }else {
            Intent intent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivityForResult(intent, 2);
        }
    }
    
    private void next(){
        if (XXPermissions.isGranted(this,permissions)){
            startService(new Intent(this, GpsService.class));
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else {
            XXPermissions per =  XXPermissions.with(this);
            per .permission(permissions);
            per.request(new OnPermissionCallback() {

                @Override
                public void onGranted(List<String> permissions, boolean all) {
                    if (all){
                        next();
                    }else {
                        Toast.makeText(StartActivity.this, "請允許授權GPS定位權限，再使用本app", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                }
            });
        }
    }

    public static boolean isOpenGps(Activity activity){
        LocationManager locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2){
            if (isOpenGps(this)){
                next();
            }else {
                Toast.makeText(this, "請開啓GPS定位再使用本app", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }
}
