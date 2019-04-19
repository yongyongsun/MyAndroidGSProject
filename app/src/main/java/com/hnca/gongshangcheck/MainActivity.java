package com.hnca.gongshangcheck;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.hnca.gongshangcheck.fragment.Fragment1;
import com.hnca.gongshangcheck.fragment.Fragment2;
import com.hnca.gongshangcheck.fragment.Fragment3;
import com.hnca.gongshangcheck.fragment.Fragment4;
import com.hnca.gongshangcheck.fragment.LoginUserInfo;
import com.hnca.gongshangcheck.R;
import com.yechaoa.yutils.YUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private final static String TAG="MainActivity";
    private ViewPager mViewPager;
    private BottomNavigationView mNavigation;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == 1001){
            SimpleFragmentPagerAdapter adpter = (SimpleFragmentPagerAdapter)mViewPager.getAdapter();
            adpter.updateFragment();
            adpter.notifyDataSetChanged();
        }
//        adpter.getItem(2).onActivityResult(requestCode,resultCode,data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        mNavigation = findViewById(R.id.navigation);
        mViewPager = findViewById(R.id.viewPager);
        SimpleFragmentPagerAdapter adapter = new SimpleFragmentPagerAdapter(getSupportFragmentManager());
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(mOnPageChangeListener);
        mNavigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        mNavigation.setSelectedItemId(mNavigation.getMenu().getItem(1).getItemId());

//        //数据部分
//        LoginUserInfo userinfo = (LoginUserInfo)getIntent().getSerializableExtra("userinfo");
//        //登录成功时候，从loginActivity获取收据，同时刷新fragment4数据。
//        Fragment3 frag3 = (Fragment3)adapter.getItem(2);
//        frag3.getLoginData(userinfo);

    }

    private ViewPager.OnPageChangeListener mOnPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            mNavigation.getMenu().getItem(position).setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

//
        }
    };


    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    mViewPager.setCurrentItem(0);
                    return true;
                case R.id.navigation_category:
                    mViewPager.setCurrentItem(1);
                    return true;
                case R.id.navigation_cart:
                    mViewPager.setCurrentItem(2);
                    return true;
//                case R.id.navigation_my:
//                    mViewPager.setCurrentItem(3);
//                    return true;
            }
            return false;
        }
    };


    private class SimpleFragmentPagerAdapter extends FragmentStatePagerAdapter {

        private FragmentManager fragmentmanager;
        private List<Fragment> mFragment;

        public SimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
            mFragment = new ArrayList<Fragment>();
            mFragment.add(new Fragment1());
            mFragment.add(new Fragment2());
            mFragment.add(new Fragment3());
            fragmentmanager = fm;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragment.get(position);
        }

        @Override
        public int getCount() {
            return mFragment.size();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        public void updateFragment(){
            FragmentTransaction ft = fragmentmanager.beginTransaction();
            for(Fragment fragment:this.mFragment){
                ft.remove(fragment);
            }
            ft.commit();
            fragmentmanager.executePendingTransactions();
            mFragment.clear();
            mFragment.add(new Fragment1());
            mFragment.add(new Fragment2());
            mFragment.add(new Fragment3());
            //notifyDataSetChanged();
        }
    }

}
