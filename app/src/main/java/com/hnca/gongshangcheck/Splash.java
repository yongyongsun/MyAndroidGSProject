package com.hnca.gongshangcheck;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.lang.reflect.Method;


public class Splash extends AppCompatActivity {

    private ImageView imgView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate( savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//这里取消标题设置
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//这里全屏显示

        setContentView(R.layout.activity_splash);

        imgView = (ImageView) findViewById(R.id.imageView2);
        Thread myThread=new Thread(){//创建子线程
            @Override
            public void run() {
                try{
                    windowManager_ScreenDensity();
                    sleep(2400);//使程序休眠五秒

                    int nwidth = imgView.getWidth();
                    int nheight = imgView.getHeight();
                    Log.i("Splash","imageView2,width = " + nwidth + " nheight = " + nheight);

                    //其中 sw320dp 代表屏幕的最小宽度是320dp，下面是获取屏幕最小宽度的代码
                    Configuration config = getResources().getConfiguration();
                    int  smallestScreenWidth = config.smallestScreenWidthDp;
                    Log.i("Splash", "smallestScreenWidth: "  + smallestScreenWidth);

                    Intent it=new Intent(getApplicationContext(),LoginActivity.class);//启动MainActivity
                    startActivity(it);
                    finish();//关闭当前活动
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }


    private void windowManager_ScreenDensity() {
        //DisplayMetrics 封装了显示区域的各种属性
        //实例化分辨率容器
        DisplayMetrics displayMetrics = new DisplayMetrics();
        //获取分辨率信息
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;
        //屏幕密度
        //每英寸内容容纳的点的个数

        /*
         常用的几个值
        DENSITY_LOW = 120
        DENSITY_MEDIUM = 160  //默认值
        DENSITY_TV = 213      //TV专用
        DENSITY_HIGH = 240
        DENSITY_XHIGH = 320
        DENSITY_400 = 400
        DENSITY_XXHIGH = 480
        DENSITY_XXXHIGH = 640
        */
        int dpi = displayMetrics.densityDpi;
        float density = displayMetrics.density;

        Log.i("Splash", "width->" + width + "--height-->" + height
                + "--dpi-->" + dpi + "---density-->" + density + "---xdpi -->" + displayMetrics.xdpi
                + "--ydpi-->" + displayMetrics.ydpi);

    }

    /**
     * @param context
     * @return 获取屏幕原始尺寸高度，包括虚拟功能键高度
     */
    private int getTotalHeight(Context context) {
        int dpi = 0;
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        @SuppressWarnings("rawtypes")
        Class c;
        try {
            c = Class.forName("android.view.Display");
            @SuppressWarnings("unchecked")
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, displayMetrics);
            dpi = displayMetrics.heightPixels;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dpi;
    }

    /**
     * @param context
     * @return 获取屏幕内容高度不包括虚拟按键
     */
    private int getScreenHeight(Context context) {
        WindowManager wm = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(outMetrics);
        return outMetrics.heightPixels;
    }
}