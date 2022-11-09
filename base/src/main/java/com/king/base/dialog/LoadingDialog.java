package com.king.base.dialog;

import android.app.Activity;
import android.graphics.Color;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.king.base.R;
import com.king.base.utils.DpUtils;

public
class LoadingDialog {

    private AlertDialog dialog;
    private Activity context;
    private boolean isCreateView;
    private View view;

    public LoadingDialog(Activity context){
        this.context = context;
        view = createView();
    }

    private View createView(){
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading,null,false);
        ImageView img = view.findViewById(R.id.image);
        Glide.with(context).load(R.mipmap.loading_gif).into(img);

//        FrameLayout layout = new FrameLayout(context);
//        layout.setBackgroundColor(Color.parseColor("#000000"));
//
//        ProgressBar bar = new ProgressBar(context);
//        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams((int)DpUtils.dp2px(25),(int)DpUtils.dp2px(25));
//        params.gravity = Gravity.CENTER;
//        layout.addView(bar,params);
//
//        int dp45 = (int) DpUtils.dp2px(15);
//        layout.setPadding(dp45,dp45,dp45,dp45);

        return view;
    }

    public void show(){
        if (dialog == null){
            dialog = new AlertDialog.Builder(context,R.style.MyDialog).create();
        }
        if (dialog.isShowing()){
            return;
        }
        if (context.isFinishing()){
            return;
        }
        dialog.show();
        if (isCreateView == false){
            isCreateView = true;

            WindowManager.LayoutParams lp = dialog.getWindow().getAttributes();
            lp.width = 300;
            lp.height = 300;
            dialog.getWindow().setAttributes(lp);

            dialog.setContentView(view);
        }
    }

    public void dismiss(){
        if (context.isFinishing()){
            return;
        }
        if (dialog != null){
            dialog.dismiss();
        }
    }
}
