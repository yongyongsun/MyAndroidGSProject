package com.yechaoa.multipleitempage.fragment;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.yechaoa.multipleitempage.AndroidAsyncHttpHelper;
import com.yechaoa.multipleitempage.R;
import com.yechaoa.multipleitempage.adapter.MultipleItemQuickAdapter;
import com.yechaoa.multipleitempage.bean.MultipleItem;
import com.yechaoa.multipleitempage.widget.CircleImageView;
import com.yechaoa.yutils.LogUtil;
import com.yechaoa.yutils.YUtils;

import org.apache.http.Header;

import java.util.ArrayList;
import java.util.List;

public class Fragment4 extends Fragment {

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;

    private MultipleItem multipleItem = null;

    private List<MultipleItem> itemDataList;

    private MultipleItemQuickAdapter multipleItemQuickAdapter;

    private LoginUserInfo mUserInfo;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment4, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();

        mSwipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.swipeRefreshLayout);
        mRecyclerView = (RecyclerView) getActivity().findViewById(R.id.recyclerView);

        initSwipeRefreshLayout();

        initItemData();

        initRecyclerView();

        initListener();

    }


    private void initSwipeRefreshLayout() {
        mSwipeRefreshLayout.setColorSchemeResources(
                android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light);

        //暂时不处理刷新 by syy
//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//                new Handler().postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        multipleItemQuickAdapter.notifyDataSetChanged();
//                        mSwipeRefreshLayout.setRefreshing(false);
//                        YUtils.showToast("刷新完成");
//                    }
//                }, 2000);
//            }
//        });
    }


    private void initItemData() {
        itemDataList = new ArrayList<>();
        multipleItem = new MultipleItem(MultipleItem.TYPE_MY_SHOW, 5);
        multipleItem.mStrPhone = mUserInfo.getMobile();
        multipleItem.mStrRole = "角色";
        multipleItem.mStrClass = "工程部";
        multipleItem.mStrStation = "工程师";
        itemDataList.add(multipleItem);
    }


    private void initRecyclerView() {
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 5);
        mRecyclerView.setLayoutManager(gridLayoutManager);

        multipleItemQuickAdapter = new MultipleItemQuickAdapter(itemDataList);

        View headerView = getHeaderView(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.my_header_image:
                        YUtils.showToast("你点击了头像");
                        break;
                    case R.id.my_header_settings:
                        YUtils.showToast("你点击了设置");

                        //add by syy
//                        AndroidAsyncHttpHelper.getInstance().get(getContext(), "http://www.baidu.com", new AsyncHttpResponseHandler() {
//                            @Override
//                            public void onSuccess(int i, Header[] headers, byte[] bytes) {
//                                String str = new String(bytes);
//                                YUtils.showToast(i + ":" + str);
//
//                            }
//
//                            @Override
//                            public void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {
//                                YUtils.showToast("失败");
//                                String str = new String(bytes);
//                                YUtils.showToast( "失败：" + str);
//
//                            }
//                        });
                        break;
                }
            }
        });

        multipleItemQuickAdapter.addHeaderView(headerView);

        mRecyclerView.setAdapter(multipleItemQuickAdapter);
    }


    private View getHeaderView(View.OnClickListener listener) {
        View headerView = getLayoutInflater().inflate(R.layout.layout_my_header, (ViewGroup) mRecyclerView.getParent(), false);

        CircleImageView myHeaderImage = (CircleImageView) headerView.findViewById(R.id.my_header_image);
        myHeaderImage.setImageResource(R.drawable.header_image);
        myHeaderImage.setOnClickListener(listener);

        TextView myHeaderName = (TextView) headerView.findViewById(R.id.my_header_name);
        myHeaderName.setText(mUserInfo.getName());

        TextView myHeaderMobile = (TextView) headerView.findViewById(R.id.my_header_mobile);
        myHeaderMobile.setText("生日:" +mUserInfo.getBirthday());

        ImageView myHeaderSettings = (ImageView) headerView.findViewById(R.id.my_header_settings);
        myHeaderSettings.setOnClickListener(listener);

        return headerView;
    }


    private void initListener() {
        multipleItemQuickAdapter.setSpanSizeLookup(new BaseQuickAdapter.SpanSizeLookup() {
            @Override
            public int getSpanSize(GridLayoutManager gridLayoutManager, int position) {
                return itemDataList.get(position).getSpanSize();
            }
        });

        multipleItemQuickAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                //YUtils.showToast("第  " + position);

            }
        });

        multipleItemQuickAdapter.setOnItemChildClickListener(new BaseQuickAdapter.OnItemChildClickListener() {
            @Override
            public void onItemChildClick(BaseQuickAdapter adapter, View view, int position) {
                switch (view.getId()) {
                    case R.id.iv_about:
                        YUtils.showToast("关于");
                        break;
                    case R.id.iv_help:
                        YUtils.showToast("帮助");
                        break;
                    case R.id.iv_logout:
                        YUtils.showToast("退出");

                        break;
                }
            }
        });

    }

    public void getLoginData(LoginUserInfo userinfo){
        if (userinfo != null) {
            mUserInfo = userinfo;
        }else {
            mUserInfo = new LoginUserInfo();
        }
    }


}
