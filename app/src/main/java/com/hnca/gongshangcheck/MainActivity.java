package com.hnca.gongshangcheck;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

import com.hnca.gongshangcheck.fragment.Fragment1;
import com.hnca.gongshangcheck.fragment.Fragment2;
import com.hnca.gongshangcheck.fragment.Fragment3;
import com.hnca.gongshangcheck.fragment.Fragment4;
import com.hnca.gongshangcheck.fragment.LoginUserInfo;
import com.yechaoa.gongshangcheck.R;

public class MainActivity extends AppCompatActivity {
    private final static String TAG="MainActivity";
    private ViewPager mViewPager;
    private BottomNavigationView mNavigation;

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//
//        Log.i(TAG, "onActivityResult: ");
//        SimpleFragmentPagerAdapter adpter = (SimpleFragmentPagerAdapter)mViewPager.getAdapter();
//        adpter.getItem(2).onActivityResult(requestCode,resultCode,data);
//    }

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

        //数据部分
        LoginUserInfo userinfo = (LoginUserInfo)getIntent().getSerializableExtra("userinfo");
        //登录成功时候，从loginActivity获取收据，同时刷新fragment4数据。
        Fragment4 frag4 = (Fragment4)adapter.getItem(3);
        frag4.getLoginData(userinfo);

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
                case R.id.navigation_my:
                    mViewPager.setCurrentItem(3);
                    return true;
            }
            return false;
        }
    };


    private class SimpleFragmentPagerAdapter extends FragmentPagerAdapter {

        private Fragment[] mFragment = new Fragment[]{new Fragment1() , new Fragment2(), new Fragment3(), new Fragment4()};

        private SimpleFragmentPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragment[position];
        }

        @Override
        public int getCount() {
            return mFragment.length;
        }

    }


}
