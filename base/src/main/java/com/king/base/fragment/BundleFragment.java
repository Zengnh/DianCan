package com.king.base.fragment;

import android.app.Activity;

import androidx.fragment.app.Fragment;

import com.google.gson.Gson;
import com.king.base.model.IntentData;

public
class BundleFragment extends Fragment {

    public IntentData setIntentData(){
        return new IntentData(this);
    }

    public IntentData getIntentData(){
        if (getArguments() == null){
            return new IntentData();
        }
        String data = getArguments().getString("data");
        IntentData intentData = null;
        try {
            Gson gson = new Gson();
            intentData = gson.fromJson(data, IntentData.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intentData == null) {
            intentData = new IntentData();
        }
        return intentData;
    }

    /**
     * 拉起一个activity并传送数据
     * @param activityClass
     * @return
     */
    public IntentData launch(Class<? extends Activity> activityClass){
        return new IntentData(getContext(),activityClass);
    }
}
