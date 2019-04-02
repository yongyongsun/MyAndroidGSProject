package com.yechaoa.multipleitempage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.yechaoa.multipleitempage.R;

public class Fragment2 extends Fragment {
   // private WebView mWebView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment2, container, false);


    }

    @Override
    public void onStart() {
        super.onStart();

//        mWebView = getActivity().findViewById(R.id.fragment2_main_webview);
//
//        // Force links and redirects to open in the WebView instead of in a browser
//        mWebView.setWebViewClient(new WebViewClient());
//
//        // Enable Javascript
//        WebSettings webSettings = mWebView.getSettings();
//        webSettings.setJavaScriptEnabled(true);
//
//        // REMOTE RESOURCE
//         mWebView.loadUrl("http://www.baidu.com");
//         mWebView.setWebViewClient(new MyWebViewClient());
    }

}
