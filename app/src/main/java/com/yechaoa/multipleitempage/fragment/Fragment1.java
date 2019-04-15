package com.yechaoa.multipleitempage.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.yechaoa.multipleitempage.App;
import com.yechaoa.multipleitempage.MainActivity;
import com.yechaoa.multipleitempage.R;

import java.io.IOException;
import java.lang.ref.WeakReference;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.yechaoa.multipleitempage.RemotePDFActivity;
import com.yechaoa.yutils.SpUtil;
import com.yechaoa.yutils.YUtils;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Fragment1 extends Fragment{

    private SwipeFlushView swipeFlushView;
    private MyAdapter adapter;

    private MyHandler mHandler;

    private List<EntityInfo.ListBean> dataList = new ArrayList<>();

    private int pageNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mHandler = new MyHandler(this);
        return inflater.inflate(R.layout.fragment_fragment1, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();

        swipeFlushView = (SwipeFlushView) getActivity().findViewById(R.id.swipeFlushView);
        ListView listView = (ListView) getActivity().findViewById(R.id.listView);
        adapter = new MyAdapter(dataList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                EntityInfo.ListBean item = (EntityInfo.ListBean)adapter.getItem(i);
                //YUtils.showToast("点击了 = " + item.getOperator());
                Intent intent = new Intent(getActivity(), RemotePDFActivity.class);
                String strUrl = item.getWorkSheetPDFurl();
                if (strUrl == null) {
                    YUtils.showToast("PDFURL为空，点击了 = " + item.getOperator());
                }
                intent.putExtra("pdfurl",strUrl);
                startActivity(intent);

            }
        });

        // 刷新事件
        swipeFlushView.setOnFlushListener(() -> {
            pageNum = 1;
            getDataList();
        });

        // 加载事件
        swipeFlushView.setOnLoadListener(() -> {
            pageNum++;
            getDataList();
        });

        pageNum = 1;
        getDataList();

    }

    private void getDataList() {
        MediaType MEDIA_TYPE_JSON= MediaType.parse("application/json; charset=utf-8");
        Gson g = new Gson();
        Map<String,Object> map = new HashMap<String,Object>();
        map.put("userId", SpUtil.getString("userId"));
        map.put("token", SpUtil.getString("token"));
        map.put("order","DESC");
        map.put("page",pageNum);
        map.put("rows",10);
        map.put("sort","samplingDate");
        String jsonStr = g.toJson(map);
        RequestBody requestBody= RequestBody.create(MEDIA_TYPE_JSON,jsonStr);

        OkHttpClient client = new OkHttpClient();

        App appState = (App)getActivity().getApplicationContext();
        String apiUrl = appState.getURLForHistory();
        Request request = new Request.Builder().url(apiUrl).post(requestBody).build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mHandler.sendEmptyMessage(-1);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (!TextUtils.isEmpty(result)) {
                    Gson gson = new Gson();
                    EntityInfo entityInfo = gson.fromJson(result, EntityInfo.class);
                    if (entityInfo.isSuccess() ) {
                        if (entityInfo.getRows() != null && entityInfo.getRows().size() > 0) {
                            if (pageNum == 1) {
                                dataList.clear();
                            }
                            dataList.addAll(entityInfo.getRows());
                            if (pageNum == 1) {
                                mHandler.sendEmptyMessage(1);
                            } else {
                                mHandler.sendEmptyMessage(2);
                            }

                        } else {
                            mHandler.sendEmptyMessage(0);
                        }
                    } else {
                        mHandler.sendEmptyMessage(3);
                    }
                } else {
                    mHandler.sendEmptyMessage(3);
                }
            }
        });
    }

    static class MyHandler extends Handler {
        private WeakReference<Fragment1> weakReference;

        MyHandler(Fragment1 activity) {
            weakReference = new WeakReference<Fragment1>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case -1:
                    weakReference.get().swipeFlushView.setFlushing(false);
                    weakReference.get().swipeFlushView.setLoading(false);
                    YUtils.showToast("获取数据失败！");
                    break;
                case 0:
                    weakReference.get().swipeFlushView.setFlushing(false);
                    weakReference.get().swipeFlushView.setLoading(false);
                    YUtils.showToast("没有更多的数据！");
                    break;
                case 1:
                    weakReference.get().adapter.notifyDataSetChanged();
                    weakReference.get().swipeFlushView.setFlushing(false);
                    break;
                case 2:
                    weakReference.get().adapter.notifyDataSetChanged();
                    weakReference.get().swipeFlushView.setLoading(false);
                    break;
                case 3:
                    if (weakReference.get().pageNum == 1) {
                        weakReference.get().swipeFlushView.setFlushing(false);
                        YUtils.showToast("刷新失败！");
                    } else if (weakReference.get().pageNum > 1) {
                        weakReference.get().swipeFlushView.setLoading(false);
                        YUtils.showToast("加载失败！");
                    }
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHandler != null) {
            mHandler.removeCallbacksAndMessages(null);
            mHandler = null;
        }
    }

    class MyAdapter extends BaseAdapter {

        private List<EntityInfo.ListBean> dataList = new ArrayList();

        MyAdapter(List<EntityInfo.ListBean> dataList) {
            this.dataList = dataList;
        }

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int i) {
            return dataList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder holder = null;
            if (view == null) {
                holder = new ViewHolder();
                view = LayoutInflater.from(getActivity()).inflate(R.layout.item_view, viewGroup, false);

                holder.txtTitle = (TextView) view.findViewById(R.id.txt_title);
                holder.imgPic = (ImageView) view.findViewById(R.id.img_Pic);
                holder.dataTime = view.findViewById(R.id.tv_datatime);
                holder.fileSize = view.findViewById(R.id.tv_filesize);

                view.setTag(holder);
            } else {
                holder = (ViewHolder) view.getTag();
            }

            holder.txtTitle.setText(dataList.get(i).getSamplingPerson() + "----" + dataList.get(i).getOperator());

            //Date data = converToDate(dataList.get(i).getSamplingDate());
            //int aa = data.getDate();
            holder.dataTime.setText(dataList.get(i).getSamplingDate());

            String strFileSize = formetFileSize(dataList.get(i).getWorkSheetFileSize());
            holder.fileSize.setText(strFileSize);


//            //注释文件图标部分，暂时使用pdf图标
//            // 给 ImageView 打标记，避免图片复用问题
//            String imgUrl = TextUtils.isEmpty(dataList.get(i).getFirstImg()) ? "http://img5.imgtn.bdimg.com/it/u=4004044022,1540768321&fm=27&gp=0.jpg" : dataList.get(i).getFirstImg();
//            if (!(dataList.get(i).getUrl() + i).equals(holder.imgPic.getTag(R.id.img_Pic))) {
//                Picasso.with(getActivity()).load(imgUrl).into(holder.imgPic);
//                holder.imgPic.setTag(R.id.img_Pic, dataList.get(i).getUrl() + i);
//            }

            return view;
        }

        class ViewHolder {
            private TextView txtTitle;
            private ImageView imgPic;
            private TextView dataTime;
            private TextView fileSize;
        }
    }

    private String formetFileSize(long fileS)
    {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize="0B";
        if(fileS==0){
            return wrongSize;
        }
        if (fileS < 1024){
            fileSizeString = df.format((double) fileS) + "B";
        }
        else if (fileS < 1048576){
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        }
        else if (fileS < 1073741824){
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        }
        else{
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    private  Date converToDate(String strDate)
    {
        DateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = format.parse(strDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
