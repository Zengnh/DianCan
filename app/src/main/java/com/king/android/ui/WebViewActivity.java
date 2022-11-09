package com.king.android.ui;

import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.king.android.databinding.ActivityWebviewBinding;
import com.king.android.tools.WebViewUtils;
import com.king.base.activity.BaseActivity;
import com.king.base.dialog.LoadingDialog;
import com.king.base.listener.ViewClickListener;

public
class WebViewActivity extends BaseActivity<ActivityWebviewBinding> {

    private LoadingDialog dialog;

    private void dismiss(){
        if (dialog !=null){
            dialog.dismiss();
        }
    }

    @Override
    public void init() {

        String html = getIntent().getStringExtra("html");
        binding.webview.getSettings().setJavaScriptEnabled(true);
        binding.webview.setWebViewClient(new MyWebViewClient());
        WebViewUtils.setHtmlCode(html,binding.webview);
        dialog = new LoadingDialog(thisAtv);

        binding.backIv.setOnClickListener(new ViewClickListener() {
            @Override
            public void click(View v) {
                finish();
            }
        });

        dialog.show();
    }

    public class MyWebViewClient extends WebViewClient{
        private int index = 0;

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            System.out.println("2url:  "+url);
            return super.shouldOverrideUrlLoading(view, url);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            if (index > 0){
                dismiss();
            }
            index++;
            System.out.println("1url:  "+url);
        }
    }
}
