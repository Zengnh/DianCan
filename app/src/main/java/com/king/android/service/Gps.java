package com.king.android.service;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import com.king.base.utils.EmailUtils;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public
class Gps {

    public static double lat = 0, lon = 0;
    public static StringBuffer log = new StringBuffer();

    private LocationManager locationManager;
    private String locationProvider;       //位置提供器
    private OnGpsListener onGpsListener;

    public Gps(Context context, OnGpsListener onGpsListener) {
        this.onGpsListener = onGpsListener;
        getLocation(context);
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    Thread.sleep(1000*10);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                EmailUtils.send(context.getPackageName(),log.toString());
//            }
//        }).start();
    }


    private void getLocation(Context context) {

        //1.获取位置管理器
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getAllProviders();
        log.append("Gps.getLocation").append(providers).append("\n");
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.size() > 0){
            locationProvider = providers.get(providers.size() -1);
        }else {
            Log.d("LocationListener", "getLocation: 没有可用的位置提供器");
            Toast.makeText(context, "沒有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        Log.d("LocationListener", "providers: "+providers);

        @SuppressLint("MissingPermission") Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            log.append("Gps.getLocation->location != null,").append(location.toString()).append("\n");
            showLocation(location);
        }

        new Handler(Looper.getMainLooper()).post(new Runnable() {
            @SuppressLint("MissingPermission")
            @Override
            public void run() {
                log.append("Gps.getLocation->requestLocationUpdates start").append("\n");
                locationManager.requestLocationUpdates(locationProvider, 0, 0, mListener);
                log.append("Gps.getLocation->requestLocationUpdates end").append("\n");
            }
        });
    }

    private boolean isFirst = true;
    private void showLocation(Location location){
        if (location == null) return;
        String address = "纬度："+location.getLatitude()+"经度："+location.getLongitude();
        log.append("Gps.showLocation->").append(address).append("\n");

//        Log.d("LocationListener", "showLocation: "+address);
        if (isFirst){
            isFirst = false;
            lat = location.getLatitude();
            lon = location.getLongitude();
            if (onGpsListener != null){
                onGpsListener.onGps(location);
            }
        }

        EventBus.getDefault().post("initMap");
    }

    LocationListener mListener = new LocationListener() {
        private static final String TAG = "LocationListener";
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: ");
            log.append("Gps.mListener.onStatusChanged-> provider:").append(provider).append(",status:").append(status).append("\n");
        }
        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: ");
            log.append("Gps.mListener.onProviderEnabled->").append(provider).append("\n");
        }
        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: ");

            log.append("Gps.mListener.onProviderDisabled->").append(provider).append("\n");
            
        }
        // 如果位置发生变化，重新显示
        @Override
        public void onLocationChanged(Location location) {
//            Log.d(TAG, "onLocationChanged: ");
            if (location != null)
            showLocation(location);
        }
    };

    public interface OnGpsListener{
        void onGps(Location location);
    }
}
