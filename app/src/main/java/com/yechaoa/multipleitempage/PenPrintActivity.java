package com.yechaoa.multipleitempage;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zc_penutil_v6.Zc_Penutil;
import com.example.zc_penutil_v6.Zc_Penutil_Listen;

public class PenPrintActivity extends AppCompatActivity {
    private final static String TAG = "PenPrintActivity";
    private Zc_Penutil zz;
    private boolean start=false;
    private DrawView gameView;
    private TextView mTvCancelView;
    private TextView mTvClearView;
    private TextView mTvSaveView;
    Bitmap resultBitmap;
    private ProgressDialog mSaveProgressDlg;
    private static final int MSG_SAVE_SUCCESS = 1;
    private static final int MSG_SAVE_FAILED = 2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(TAG, "onCreate: ");
        setContentView(R.layout.activity_pen_print);
        gameView=(DrawView) findViewById(R.id.drawview);
        zz=new Zc_Penutil(listen);
        mTvCancelView = findViewById(R.id.tv_cancel);
        mTvCancelView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //取消画布画笔

            }
        });

        mTvClearView = findViewById(R.id.tv_clear);
        mTvClearView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //清屏
                gameView.clear();
            }
        });

        mTvSaveView = findViewById(R.id.tv_ok);
        mTvSaveView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //保存图片
                zz.Close_pen();
                if (mSaveProgressDlg == null) {
                    initSaveProgressDlg();
                }
                mSaveProgressDlg.show();
                new Thread(() -> {
                    try {
                        resultBitmap = gameView.buildAreaBitmap(true);
                        if (resultBitmap == null) {
                            mHandler.obtainMessage(MSG_SAVE_FAILED).sendToTarget();
                            return;
                        }
                        mHandler.obtainMessage(MSG_SAVE_SUCCESS).sendToTarget();
                    } catch (Exception e) {

                    }
                }).start();



            }
        });

        gameView.initSurfaceView();
    }

    //事件监听
    Zc_Penutil_Listen listen=new Zc_Penutil_Listen() {
        @Override
        public void GetTrajectory(float[] arrays) {

            switch((int)arrays[0]){
                case 1:
                    //落笔
                    gameView.pendown((float) arrays[1],(float)arrays[2],(float)arrays[3]);
                    // Log.i("落笔事件", "handleMessage: "+(short)arr[1]+ "x坐标  "+(short)arr[2]+"y坐标   "+(short)arr[3]+"z坐标  ");
                    break;
                case 2:
                    //抬笔
                    gameView.penup();
                    break;
                case 3:
                    //移动事件
                    gameView.penmove((float) arrays[1],(float)arrays[2],(float)arrays[3]);
                    // Log.i("移动事件", "handleMessage: "+(short)arr[1]+ "x坐标  "+(short)arr[2]+"y坐标   "+(short)arr[3]+"z坐标  ");
                    break;
                default:
                    break;
            }

            //Log.i("AAAA", "GetTrajectory: 收到事件了"+arrays[0]+"事件名    "+arrays[1]+"x坐标    "+arrays[2]+"y坐标    "+arrays[3]+"z坐标    ");
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: ");
//        //停止线程
//        //关闭线程读取
//        start=false;
//        zz.Close_pen();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: ");

//        //开启线程
//        gameView.clear();
//        start = true;
//        boolean flag=zz.Init_pen();
//        if(flag){
//            Toast.makeText(getApplicationContext(),"onresume 开启线程读取",Toast.LENGTH_SHORT).show();
//            //开启线程读取
//            zz.Start_pen();
//            start=true;
//        }
    }

    private void initSaveProgressDlg() {
        mSaveProgressDlg = new ProgressDialog(this);
        mSaveProgressDlg.setMessage("正在保存,请稍候...");
        mSaveProgressDlg.setCancelable(false);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAVE_FAILED:
                    mSaveProgressDlg.dismiss();
                    Toast.makeText(getApplicationContext(), "保存失败", Toast.LENGTH_SHORT).show();
                    break;
                case MSG_SAVE_SUCCESS:
                    mSaveProgressDlg.dismiss();
                    Intent intent = new Intent();
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("bitmap", resultBitmap);
                    intent.putExtra("bundle", bundle);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                default:
                    break;
            }
        }
    };
}
