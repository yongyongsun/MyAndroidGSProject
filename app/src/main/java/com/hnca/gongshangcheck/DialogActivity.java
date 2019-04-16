package com.hnca.gongshangcheck;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Base64;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.yechaoa.gongshangcheck.R;
import com.zkteco.android.biometric.core.device.ParameterHelper;
import com.zkteco.android.biometric.core.device.TransportType;
import com.zkteco.android.biometric.core.utils.LogHelper;
import com.zkteco.android.biometric.core.utils.ToolUtils;
import com.zkteco.android.biometric.nidfpsensor.NIDFPFactory;
import com.zkteco.android.biometric.nidfpsensor.NIDFPSensor;
import com.zkteco.android.biometric.nidfpsensor.exception.NIDFPException;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class DialogActivity extends FragmentActivity {

    /**
     * 当sdk版本升级到26之后的时候(不包括26)，
     * 如果Dialog样式的Activity在AndroidManifest里面
     * 设置android:screenOrientation="portrait"属性
     * 或者在代码里面设置setRequestedOrientation();//禁止横竖屏
     *
     * 都会导致程序崩溃，崩溃代码：Caused by: java.lang.IllegalStateException: Only fullscreen activities can request orientation
     *
     * 除了以上两个属性不能设置外，在AndroidManifest里面对应Activity设置的theme属性尽量和我写的一样。如果有更好的解决办法，请联系我！
     *
     */

    private static final int VID = 6997;    //zkteco device VID  6997
    private static final int PID = 772;//1023;    //NIDFPSensor PID 根据实际设置
    //测试模板，二代证采集的1024字节有2个模板
    private byte[] mTestFeature = new byte[512];
    //测试模板Base64字符串
    private String mstrTemplate = "QwH/EgFjSAAAAAAAAAAAAAAAACwBmmq0AP///////7QjTP5ZKRb8miux/JE4Yv5LP+L8RE8q/IJTV/7BV0H+cWIG/LtqlPxpcBT8i4FD/oCQO/45mS38d586/qGhKf5fsuv8arfv/JO3cPw2uD78b78B/qTGGf6SzBT+T9E8/GvR8/wo4j78b/Pu/K/0Ef4lCzD9oxEA/70WH/+MG+79ZB7i/a0hA/8sJSz9PCsq/VQ2Lv2+Pnj9jEI3/alFH/1QUhj9qlIA/YtXJv1pXCD9AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAACpAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAJc=";

    private WorkThread mWorkThread = null;
    private boolean mbStop = true;
    private boolean mbStart = false;
    private NIDFPSensor mNIDFPSensor = null;
    private byte[] mBufImage;
    private CountDownLatch countdownLatch = new CountDownLatch(1);
    private Context mContext = null;
    private UsbManager musbManager = null;
    private final String ACTION_USB_PERMISSION = "com.yechaoa.multipleitempage.USB_PERMISSION";

    private TextView mTxtReport;
    private TextView mTxtStatus;
    private ImageView mImgView;

    private Bitmap mCurBitmap;

    private BroadcastReceiver mUsbReceiver = null;

    class FinterReceiver extends  BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();

            if (ACTION_USB_PERMISSION.equals(action))
            {
                synchronized (this)
                {
                    UsbDevice device = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                    if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false))
                    {
                        openDevice();
                    }
                    else
                    {
                        Toast.makeText(mContext, "USB未授权", Toast.LENGTH_SHORT).show();
                        mTxtReport.setText("USB未授权");
                    }
                }
            }
        }
    };
    private void OpenDeviceAndRequestDevice()
    {
        if (mbStart)
        {
            mTxtReport.setText("设备已连接，请先断开连接");
            return;
        }
        musbManager = (UsbManager)this.getSystemService(Context.USB_SERVICE);
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_USB_PERMISSION);
        filter.addAction(UsbManager.ACTION_USB_ACCESSORY_ATTACHED);
        mContext.registerReceiver(mUsbReceiver, filter);

        for (UsbDevice device : musbManager.getDeviceList().values())
        {
            if (device.getVendorId() == VID && device.getProductId() == PID)
            {
                Intent intent = new Intent(ACTION_USB_PERMISSION);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(mContext, 0, intent, 0);
                musbManager.requestPermission(device, pendingIntent);
            }
        }
    }

    private void startFingerVeinSensor() {
        // Define output log level
        LogHelper.setLevel(Log.VERBOSE);
        // Start fingerprint sensor
        Map fingerprintParams = new HashMap();
        //set vid
        fingerprintParams.put(ParameterHelper.PARAM_KEY_VID, VID);
        //set pid
        fingerprintParams.put(ParameterHelper.PARAM_KEY_PID, PID);
        mNIDFPSensor = NIDFPFactory.createNIDFPSensor(this, TransportType.USBSCSI, fingerprintParams);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);//禁止横屏

        //DialogActivity必须设置的两个属性，不然布局显示不正常
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setGravity(Gravity.CENTER);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.height = (int) (display.getHeight() * 0.9);
        params.width = (int) (display.getWidth() * 0.7);
        params.alpha = 1.0f;
        getWindow().setAttributes(params);
        getWindow().setGravity(Gravity.CENTER);

        mUsbReceiver = new FinterReceiver();
        mCurBitmap = null;
        mTestFeature = Base64.decode(mstrTemplate, Base64.NO_WRAP);
        mContext = this.getApplicationContext();
        mTxtReport = (TextView)findViewById(R.id.txtReport);
        mTxtStatus = findViewById(R.id.txtStatus);
        mImgView  = findViewById(R.id.iv_finger_print);
        mTxtReport.setText("");
        startFingerVeinSensor();

        OpenDeviceAndRequestDevice();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //CloseDevice(false);  //尝试关闭设备
        if (mUsbReceiver != null) {
            mContext.unregisterReceiver(mUsbReceiver);
        }

        // Destroy fingerprint sensor when it's not used
        NIDFPFactory.destroy(mNIDFPSensor);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //return super.onKeyDown(keyCode, event);
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            OnBnClose(null);

        }
        return true;
    }
    private class WorkThread extends Thread {
        @Override
        public void run() {
            super.run();
            mbStop = false;
            while (!mbStop) {
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                try {
                    mNIDFPSensor.GetFPRawData(0, mBufImage);
                } catch (NIDFPException e) {
                    e.printStackTrace();
                    continue;
                }
                byte[] retQualityScore = new byte[1];
                int ret = mNIDFPSensor.getQualityScore(mBufImage, retQualityScore);
                final byte qualityScore = retQualityScore[0];
                runOnUiThread(new Runnable() {
                    public void run() {
                        Bitmap bitmap = ToolUtils.renderCroppedGreyScaleBitmap(mBufImage, mNIDFPSensor.getFpImgWidth(), mNIDFPSensor.getFpImgHeight());
                        mImgView.setImageBitmap(bitmap);
                        mTxtStatus.setText("ImageQualityScore:" + qualityScore);

                        //add 指纹分数超过60分后，读取成功，退出线程。
                        if (qualityScore > 60){
                            mCurBitmap = bitmap;
                            CloseDevice(true);
                        }
                    }
                });
                if (1 !=  ret || qualityScore < 45)
                {
                    continue;
                }
                final float score = mNIDFPSensor.ImageMatch(0, mBufImage, mTestFeature);
                runOnUiThread(new Runnable() {
                    public void run() {
                        mTxtReport.setText("score:" + score);
                    }
                });
            }
            countdownLatch.countDown();
            //Toast.makeText(mContext, "线程退出", Toast.LENGTH_SHORT).show();
        }
    }

    private void openDevice()
    {
        if (mbStart)
        {
            mTxtReport.setText("设备已连接");
            Toast.makeText(mContext, "设备已连接", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            //连接设备
            mNIDFPSensor.open(0);
            //分配读取指纹图像内存(width*height Bytes)
            mBufImage = new byte[mNIDFPSensor.getFpImgWidth()*mNIDFPSensor.getFpImgHeight()];
            //设置流模式探测（总是成功以及返回分数）
            mNIDFPSensor.setParameter(0, NIDFPSensor.PARAM_CODE_DETECT_FLAG, 0);
            //设置探测模式（按压一次手指显示采集一次成功）
            mNIDFPSensor.setParameter(0, NIDFPSensor.PARAM_CODE_DETECT_FLAG, 1);
            mWorkThread = new WorkThread();
            mWorkThread.start();// 线程启动
            //mTxtReport.setText("Open device succ");
            Toast.makeText(mContext, "设备连接成功，图像宽：" + mNIDFPSensor.getFpImgWidth() + ",图像高：" + mNIDFPSensor.getFpImgHeight(),
                    Toast.LENGTH_SHORT).show();
            mbStart = true;
        } catch (NIDFPException e) {
            e.printStackTrace();
            Toast.makeText(mContext, "Open device fail.errorcode:\"+ e.getErrorCode() + \"err message:\" + e.getMessage() + \"inner code:\" + e.getInternalErrorCode()", Toast.LENGTH_SHORT).show();
            //mTxtReport.setText("Open device fail.errorcode:"+ e.getErrorCode() + "err message:" + e.getMessage() + "inner code:" + e.getInternalErrorCode());
        }
    }

    public void OnBnOpen(View view)
    {
//        int dp = PixAndDpUtil.Px2Dp(getApplicationContext(),256);
//        YUtils.showToast("px = 256 ,change dp = " + dp);
        OpenDeviceAndRequestDevice();
    }

    public void OnBnVerify(View view)
    {
        /*
        try {
            mNIDFPSensor.GetFPRawData(0, mBufImage);
        } catch (NIDFPException e) {
            e.printStackTrace();
            return;
        }
        mImgView.setImageBitmap(null);
        byte[] retQualityScore = new byte[1];
        int ret = mNIDFPSensor.getQualityScore(mBufImage, retQualityScore);
        final byte qualityScore = retQualityScore[0];
                Bitmap bitmap = ToolUtils.renderCroppedGreyScaleBitmap(mBufImage, mNIDFPSensor.getFpImgWidth(), mNIDFPSensor.getFpImgHeight());
                mImgView.setImageBitmap(bitmap);
                mTxtStatus.setText("ImageQualityScore:" + qualityScore);
        if (1 !=  ret || qualityScore < 30)
        {
            return;
        }
        final float score = mNIDFPSensor.ImageMatch(0, mBufImage, mTestFeature);
        mTxtReport.setText("score:" + score);
        */
    }


    void CloseDevice(boolean bsaveData)
    {
        if (mbStart)
        {
            mbStop = true;  //停止采集线程
            try {
                //等待线程退出，10S
                countdownLatch.await(10*1000, TimeUnit.SECONDS);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
//            try {
//                mNIDFPSensor.close(0);  //关闭设备
//            } catch (NIDFPException e) {
//                e.printStackTrace();
//            }
        }
        mbStart = false;
        if (bsaveData){
            Intent intent = new Intent();
            Bundle bundle = new Bundle();
            bundle.putParcelable("bitmap", mCurBitmap);
            intent.putExtra("bundle", bundle);
            setResult(RESULT_OK, intent);
        }
        finish();
    }

    public void OnBnClose(View view)
    {
        CloseDevice(false);
        mTxtReport.setText("Close device succ!");
        //moveTaskToBack(true);
    }

}
