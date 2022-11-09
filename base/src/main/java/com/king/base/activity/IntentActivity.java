package com.king.base.activity;

import android.app.Activity;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.king.base.model.IntentData;

public
class IntentActivity extends AppCompatActivity {

    /**
     * 获取上一个activity传来的数据
     * @return
     */
    public IntentData getIntentData(){
        String data = getIntent().getStringExtra("data");
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
        return new IntentData(this,activityClass);
    }
}
