package com.topstar.thepredictor;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import java.io.ByteArrayInputStream;
import java.io.InputStream;


public class Livescore extends Fragment {
    View view;
    WebView webView;
    private final String TAG = PostDetails.class.getSimpleName();
    ProgressBar progressBar;

    SwipeRefreshLayout refresher;

    public Livescore() {

    }

   final private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message message) {
            switch (message.what) {
                case 1: {
                    webViewGoBack();
                }
                break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.liverscore, container, false);
        progressBar = view.findViewById(R.id.progressBar2);
        progressBar.setMax(100);


        refresher = view.findViewById(R.id.refresher);
        refresher.setColorSchemeResources(R.color.blue, R.color.lightBlue, R.color.deepPurple, R.color.purple, R.color.pink, R.color.orange, R.color.red);

        webView = view.findViewById(R.id.webView);
        webView.setFocusableInTouchMode(false);
        webView.setFocusable(false);
        webView.setWebViewClient(new Livescore.WebViewClientDemo());

        webView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                return true;
            }

            @Nullable
            @Override
            public WebResourceResponse shouldInterceptRequest(WebView view, String url) {

                if (url.contains("google")||url.contains("facebook")){
                    InputStream textstream = new ByteArrayInputStream("".getBytes());
                    return getTextWebResource(textstream);
                }



                return super.shouldInterceptRequest(view, url);
            }
        });

        webView.getSettings().setJavaScriptEnabled(true);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.setWebChromeClient(new Livescore.myWebChrome());
        webView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (keyCode == KeyEvent.KEYCODE_BACK
                        && event.getAction() == MotionEvent.ACTION_UP
                        && webView.canGoBack()) {
                    handler.sendEmptyMessage(1);
                    return true;
                }


                return false;
            }
        });
        CookieManager.getInstance().setAcceptCookie(true);
        ConnectivityManager cm =
                (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            webView.loadUrl("http://www.livescore.cz");

        } else {
            Toast.makeText(getActivity(), "Network problem, please reload the page", Toast.LENGTH_SHORT).show();
        }
        refresher.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                ConnectivityManager cm =
                        (ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = cm.getActiveNetworkInfo();
                if (netInfo != null && netInfo.isConnectedOrConnecting()) {
                    webView.loadUrl("http://www.livescore.cz");


                } else {
                    Toast.makeText(getActivity(), "Network problem, please reload the page", Toast.LENGTH_SHORT).show();
                }

            }
        });


        return view;
    }
    private  WebResourceResponse getTextWebResource(InputStream data){
        return  new WebResourceResponse("text/plain","UTF-8",data);
    }

    private void webViewGoBack() {
        webView.goBack();
    }
    private class myWebChrome extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            progressBar.setProgress(newProgress);
            if (newProgress == 100) {
                progressBar.setVisibility(View.GONE);
            }


        }

    }

    private class WebViewClientDemo extends WebViewClient {
        @SuppressWarnings("deprecation")


        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @TargetApi(Build.VERSION_CODES.N)
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            view.loadUrl(request.getUrl().toString());

            return true;

        }

        @Override
        public void onPageFinished(WebView view, String url) {
            webView.loadUrl("javascript:(function (){" +

                    "document.getElementsByClass('adsbygoogle').style.display='none'; })()");
            super.onPageFinished(view, url);
            progressBar.setVisibility(View.GONE);
            refresher.setRefreshing(false);
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            refresher.setRefreshing(true);
            progressBar.setVisibility(View.VISIBLE);

        }
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
    }