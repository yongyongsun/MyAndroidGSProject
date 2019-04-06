package com.yechaoa.multipleitempage.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;


import com.yechaoa.multipleitempage.R;
import com.yechaoa.multipleitempage.dialog.DialogHelper;
import com.yechaoa.multipleitempage.dialog.inf.OnDialogCancelListener;
import com.yechaoa.multipleitempage.dialog.inf.OnDialogConfirmDataListener;
import com.yechaoa.multipleitempage.dialog.inf.OnDialogConfirmListener;
import com.yechaoa.yutils.YUtils;

public class Fragment2 extends Fragment implements OnDialogCancelListener {
   // private WebView mWebView;

    private DialogHelper mDialogHelper;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mDialogHelper == null) {
            mDialogHelper = new DialogHelper(this.getActivity(), this);
        }
        return inflater.inflate(R.layout.fragment_fragment2, container, false);


    }

    @Override
    public void onStart() {
        super.onStart();

        TextView tv_submit_view = getActivity().findViewById(R.id.tv_submit);
        tv_submit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                EditText text = getActivity().findViewById(R.id.sub_iv_sample_name).findViewById(R.id.et_input);
//                String ss = text.getText().toString();
//                YUtils.showToast(ss);
                showConfirmDialog();


            }
        });
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

    @Override
    public void onDialogCancelListener(AlertDialog dialog) {

        YUtils.showToast( "弹窗关闭");
    }

    public void showConfirmDialog() {
        mDialogHelper.showConfirmDataDialog("ConfirmDialog", "确定", "取消", new OnDialogConfirmDataListener() {
            @Override
            public void onDialogConfirmDataListener(Bundle data) {
                String str = data.getString("string");
                YUtils.showToast(str);
            }
        }, new OnDialogCancelListener() {
            @Override
            public void onDialogCancelListener(AlertDialog dialog) {
                YUtils.showToast("cancel");
            }
        });
    }
}
