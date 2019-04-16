package com.hnca.gongshangcheck;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.hnca.gongshangcheck.R;

import java.util.ArrayList;

/**
 * 执行画方法 继承surfaceview
 */
public class DrawView extends SurfaceView  implements SurfaceHolder.Callback{
    private final static String TAG="DrawView";
    private final static float penwidth=1.5f;
    private float upx,upy,upz;
    private Paint paint=new Paint();
    private Path path=new Path();
    private SurfaceHolder mSurfaceHolder;
    private Canvas canvas;
    private Canvas mcanvas;
    private Bitmap mBitmap;
    private Rect rect=new Rect(0,getResources().getDimensionPixelSize(R.dimen.sign_actionbar_height),1280,800);
    //笔轨迹、笔压力
    private ArrayList<Path> pen_paths=new ArrayList<>();
    private ArrayList<Float> pen_widths=new ArrayList<>();
    //线程控制
    private boolean drawing=false;
    public DrawView(Context context) {
        super(context);
        //初始化
        //initSurfaceView();

    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        //initSurfaceView();

    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //initSurfaceView();
    }

    public void initSurfaceView(){
        mBitmap = Bitmap.createBitmap(rect.width(), rect.height(), Bitmap.Config.ARGB_8888);
        mcanvas = new Canvas(mBitmap);
        //设置画布的背景色为透明
        mcanvas.drawColor(Color.TRANSPARENT);

        //初始化画笔
        paint.setColor(Color.BLACK);
        //画笔设置
        paint.setAntiAlias(true);
        paint.setDither(true);
        paint.setStrokeWidth(penwidth);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeJoin(Paint.Join.ROUND);
        paint.setStrokeCap(Paint.Cap.ROUND);

        mSurfaceHolder = getHolder();
        mSurfaceHolder.addCallback(this);
    }



    //移动事件
    public void penmove(float x,float y,float z){

        float zlx=(x+upx)/2;
        float zly=(y+upy)/2;

        path.quadTo(upx,upy,zlx,zly);

        /*float valx=Math.abs(x-upx);
        float valy=Math.abs(y-upy);
        if(valx>=3||valy>=3){

            float zlx=(x+upx)/2;
            float zly=(y+upy)/2;

            path.quadTo(upx,upy,zlx,zly);
        }else{
            path.lineTo(x,y);
        }*/


        //移动使用lineto()
        float valupz=((z/100)/2)*penwidth;
        if(upz!=valupz){
            //粗细减缓处理
            if(valupz>upz){
                upz=upz/0.9f;
            }else{
                upz=upz*0.9f;
            }
            pen_widths.add(upz);
            pen_paths.add(path);
            path=new Path();
            path.moveTo(upx,upy);
            //paint.setStrokeWidth(upz);
        }
        upx=zlx;
        upy=zly;
        setdirty(upx,upy,upz);

        rect.left-=10;
        rect.right+=10;
        rect.top-=10;
        rect.bottom+=10;
    }

    //按下事件
    public void pendown(float x,float y,float z){

        path.moveTo(x,y);
        upx=x;
        upy=y;
        upz=((z/100)/2)*penwidth;
        rect.left=(int)(upx-upz);
        rect.right=(int)(upx+upz);
        rect.top=(int)(upy-upz);
        rect.bottom=(int)(upy+upz);
    }

    //抬起事件
    public void penup(){
        //初始化画笔宽度
        //pen_widths.add(upz);
        //pen_widths.clear();
        //pen_paths.clear();
        // pen_paths.add(path);
    }

    //清理画布
    public  void clear(){
        //路径重置
        path.reset();
        pen_widths.clear();
        pen_paths.clear();
        rect.left=0;
        rect.right=1280;
        rect.top=getResources().getDimensionPixelSize(R.dimen.sign_actionbar_height);
        rect.bottom=800;
    }

    //surfaceview 创建时
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        //开启线程
        drawing=true;
        CanvasDraw.start();
    }
    //surfaceview 发生改变时
    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    //surfaceview 销毁时
    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        //关闭线程
        drawing=false;
    }

    //线程控制canvas的刷新生成，保持在30ms一次
    private Thread CanvasDraw=new Thread(){
        @Override
        public void run() {
            while (drawing){
                long start_time = System.currentTimeMillis();
                draw_Penpath();
                long end_time = System.currentTimeMillis();

                long value_time=end_time - start_time;
                if (value_time < 50) {
                    try {
                        Thread.sleep(50 - (value_time));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    };

    public void stop() {
        if (CanvasDraw != null && CanvasDraw.isAlive()) {
            CanvasDraw.interrupt();
            CanvasDraw = null;
        }
    }
    private void draw_Penpath(){
        try {
            //获得canvas对象        局部刷新
            float retx=upx;
            float rety=upy;
            canvas = mSurfaceHolder.lockCanvas(rect);
            //Log.i(TAG, "draw_Penpath: "+rect.toString());
            //重置脏区
            resetdirty(retx,rety);
            //绘制背景
            //canvas.drawColor(Color.TRANSPARENT, android.graphics.PorterDuff.Mode.CLEAR);
            canvas.drawColor(Color.WHITE);
            //绘制路径  多重路径
            if(pen_paths.size()==0){
            }else{
                long start_time = System.currentTimeMillis();
                for(int i=0;i<pen_paths.size();i++){
                    paint.setStrokeWidth(pen_widths.get(i));
                    canvas.drawPath(pen_paths.get(i), paint);
                    mcanvas.drawPath(pen_paths.get(i), paint);
                }
                long end_time = System.currentTimeMillis();
                //Log.i(TAG, "draw_Penpath:耗时"+(end_time-start_time));
                //pen_widths.clear();
                //pen_paths.clear();
                if(!path.isEmpty()){
                    paint.setStrokeWidth(upz);
                    canvas.drawPath(path, paint);
                    mcanvas.drawPath(path, paint);
                }
                paint.setStrokeWidth(0);
            }
        }catch (Exception e){
            Log.i(TAG, e.toString());
        }finally {
            if (canvas != null){
                //释放canvas对象并提交画布
                mSurfaceHolder.unlockCanvasAndPost(canvas);
            }
        }
    }

    private void setdirty(float x, float y, float upz){
        if(x<rect.left){
            rect.left=(int)(x);
        }else if(x>rect.right){
            rect.right=(int)(x);
        }

        if(rect.top>y){
            rect.top=(int)(y);
        }else if(rect.bottom<y){
            rect.bottom=(int)(y);
        }
    }

    private void resetdirty(float resetx,float resety){
        rect.left=(int)(resetx-10);
        rect.right=(int)(resetx+10);
        rect.top=(int)(resety-10);
        rect.bottom=(int)(resety+10);
    }

    /**
     * 构建Bitmap
     *
     * @return 所绘制的bitmap
     */
    public Bitmap buildAreaBitmap(boolean isCrop) {
        Bitmap result ;
        if (isCrop) {
            result = BitmapUtil.clearBlank(mBitmap, 50, Color.TRANSPARENT);
        } else {
            result = mBitmap;
        }
        destroyDrawingCache();
        return result;
    }

}
