/*
 * Copyright (C) 2016 Olmo Gallegos Hernández.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.yechaoa.multipleitempage;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zkteco.android.biometric.core.utils.ToolUtils;

import org.w3c.dom.Text;

import es.voghdev.pdfviewpager.library.PDFViewPager;
import es.voghdev.pdfviewpager.library.RemotePDFViewPager;
import es.voghdev.pdfviewpager.library.adapter.PDFPagerAdapter;
import es.voghdev.pdfviewpager.library.remote.DownloadFile;
import es.voghdev.pdfviewpager.library.util.FileUtil;


public class RemotePDFActivity extends BaseSampleActivity implements DownloadFile.Listener {
    private static final String TAG = "RemotePDFActivity";
    LinearLayout root;
    RemotePDFViewPager remotePDFViewPager;
    EditText etPdfUrl;
    Button btnDownload;
    PDFPagerAdapter adapter;
    private TextView mTvTipsInfo;
    private String mPdfUrl;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setTitle(R.string.remote_pdf_example);
        setContentView(R.layout.activity_remote_pdf);

        root = (LinearLayout) findViewById(R.id.remote_pdf_root);
        etPdfUrl = (EditText) findViewById(R.id.et_pdfUrl);
        btnDownload = (Button) findViewById(R.id.btn_download);
        mTvTipsInfo = findViewById(R.id.tv_tips_info);

        mPdfUrl = getIntent().getStringExtra("pdfurl");
        //setDownloadButtonListener();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep( 100 );
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        remotePDFViewPager = new RemotePDFViewPager(getBaseContext(), mPdfUrl, RemotePDFActivity.this);
                        Log.i(TAG, "running: ");
                    }
                });

            }
        }).start();
    }

    @Override
    protected void onResume() {
        Log.i(TAG, "onResume: ");
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (adapter != null) {
            adapter.close();
        }
    }

    protected void setDownloadButtonListener() {
        final Context ctx = this;
        final DownloadFile.Listener listener = this;
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //remotePDFViewPager = new RemotePDFViewPager(ctx, getUrlFromEditText(), listener);
                //remotePDFViewPager.setId(R.id.pdfViewPager);
               // hideDownloadButton();
            }
        });
    }

    protected String getUrlFromEditText() {
        return etPdfUrl.getText().toString().trim();
    }

    public static void open(Context context) {
        Intent i = new Intent(context, RemotePDFActivity.class);
        context.startActivity(i);
    }

    public void showDownloadButton() {
        btnDownload.setVisibility(View.VISIBLE);
    }

    public void hideDownloadButton() {
        btnDownload.setVisibility(View.INVISIBLE);
    }

    public void updateLayout() {
        Log.i(TAG, "updateLayout: ");
        root.removeAllViewsInLayout();
//        root.addView(etPdfUrl,
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        root.addView(btnDownload,
//                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        root.addView(remotePDFViewPager,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    public void onSuccess(String url, String destinationPath) {
        Log.i(TAG, "onSuccess: ");
        adapter = new PDFPagerAdapter(this, FileUtil.extractFileNameFromURL(url));
        remotePDFViewPager.setAdapter(adapter);
        updateLayout();
        //showDownloadButton();
    }

    @Override
    public void onFailure(Exception e) {
        e.printStackTrace();
        //showDownloadButton();
    }

    @Override
    public void onProgressUpdate(int progress, int total) {
        String str = "正在下载PDF文件，请稍后... 进度：" + progress +"/" + total;
        mTvTipsInfo.setText(str);
    }
}

