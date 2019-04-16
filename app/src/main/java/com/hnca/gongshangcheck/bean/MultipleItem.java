package com.hnca.gongshangcheck.bean;

import com.chad.library.adapter.base.entity.MultiItemEntity;

/**
 * Describe :
 */

public class MultipleItem implements MultiItemEntity {

    public static final int TYPE_MY_INFO = 1;
    public static final int TYPE_MY_SHOW = 2;
    public static final int TYPE_MY_SETTING = 3;
    private int itemType;
    private int spanSize;

    public MultipleItem(int itemType, int spanSize) {
        this.itemType = itemType;
        this.spanSize = spanSize;
    }

    @Override
    public int getItemType() {
        return itemType;
    }

    public int getSpanSize() {
        return spanSize;
    }

    public void setSpanSize(int spanSize) {
        this.spanSize = spanSize;
    }


    public String mStrPhone;//电话
    public String mStrRole;//角色
    public String mStrClass;//部门
    public String mStrStation;//岗位

    public boolean isShow;

    public int count;

}
