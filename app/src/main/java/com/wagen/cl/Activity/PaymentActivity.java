package com.wagen.cl.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.ValueCallback;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.wagen.cl.Constant.Constants;
import com.wagen.cl.R;

public class PaymentActivity extends AppCompatActivity {
    WebView webView;
    private static ProgressDialog progDailog;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        webView = (WebView)findViewById(R.id.webview);

        progDailog = ProgressDialog.show(this, "Loading","Please wait...", true);
        progDailog.setCancelable(false);
        String url = Constants.webviewurl+"?orderid="+Constants.orderid+"&amount="+Constants.orderamount;

        Log.d("weburl==", url);

        initWebView();

        webView.loadUrl(url);

        webView.setWebViewClient(new WebViewClient()
        {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);


                Log.d("WebView", "your current url when webpage loading.." + url);
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                Log.d("WebView", "your current url when webpage loading.. finish" + url);
                //super.onPageFinished(view, url);
                if(url.contains("Paymentend")){
                    webView.evaluateJavascript("javascript:sendmessage();", new ValueCallback<String>() {
                        @Override
                        public void onReceiveValue(String s) {
                            // Do what you want with the return value
                            if(s.equals("0")){
                                Constants.frompagestatus_formembership = 1;
                                //Toast.makeText(PaymentActivity.this, getString(R.string.paymentsuccess), Toast.LENGTH_SHORT).show();
                            }else{
                                Constants.frompagestatus_formembership = 2;
                                //Toast.makeText(PaymentActivity.this, getString(R.string.paymentfailed), Toast.LENGTH_SHORT).show();
                            }

                            finish();

                        }
                    });
                }
                progDailog.dismiss();
            }

            @Override
            public void onLoadResource(WebView view, String url) {
                // TODO Auto-generated method stub
                super.onLoadResource(view, url);
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                System.out.println("when you click on any interlink on webview that time you got url :-" + url);
                //return super.shouldOverrideUrlLoading(view, url);
                progDailog.show();
                view.loadUrl(url);
                return false;
            }
        });

    }

    private static class MyWebViewClient extends WebViewClient {

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            progDailog.show();
            view.loadUrl(url);
            return false;
        }
        @Override
        public void onPageFinished(WebView view, final String url) {
            progDailog.dismiss();
        }
    }

    @SuppressLint({ "SetJavaScriptEnabled" })
    private void initWebView() {
        webView.getSettings().setJavaScriptEnabled(true);
        //webView.setWebViewClient(new MyWebViewClient());
        //webView.addJavascriptInterface(new WebViewInterface(this), "JSInterface");

    }

    /*public static class WebViewInterface {

        private PaymentActivity activity;

        public WebViewInterface(PaymentActivity activity) {
            this.activity = activity;
        }

        @JavascriptInterface
        public void returnApp(String success, String message, String orderID) {

            Log.d("web call", message + " : " + success + " : " + orderID);

            if (message.toLowerCase().contains("success")){

                //activity.successDialog();
                Toast.makeText(activity, "success", Toast.LENGTH_SHORT).show();
            }
            else {

               // activity.gotoBack();
                Toast.makeText(activity, "fail", Toast.LENGTH_SHORT).show();
            }
        }
    }*/




    public void goback(View view) {
        finish();
    }
}