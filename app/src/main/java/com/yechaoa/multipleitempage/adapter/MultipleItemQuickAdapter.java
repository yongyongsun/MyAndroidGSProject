package com.yechaoa.multipleitempage.adapter;

import android.support.v4.content.ContextCompat;
import android.view.View;

import com.chad.library.adapter.base.BaseMultiItemQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.yechaoa.multipleitempage.R;
import com.yechaoa.multipleitempage.bean.MultipleItem;
import com.yechaoa.multipleitempage.bean.item_view;

import java.util.List;

/**
 * Created by syy on 2019/03/28.
 * Describe :
 */

public class MultipleItemQuickAdapter extends BaseMultiItemQuickAdapter<MultipleItem, BaseViewHolder> {

    public MultipleItemQuickAdapter(List data) {
        super(data);
        addItemType(MultipleItem.TYPE_MY_SHOW, R.layout.layout_my_show);

    }

    @Override
    protected void convert(BaseViewHolder helper, MultipleItem item) {
        switch (helper.getItemViewType()) {
            case MultipleItem.TYPE_MY_SHOW:
                helper.setText(R.id.tv_myPhone,item.mStrPhone);
                helper.setText(R.id.tv_myRole,item.mStrRole);
                helper.setText(R.id.tv_myClass,item.mStrClass);
                helper.setText(R.id.tv_myStation,item.mStrStation);
                helper.addOnClickListener(R.id.iv_about);
                helper.addOnClickListener(R.id.iv_help);
                helper.addOnClickListener(R.id.iv_logout);
                break;
        }
    }

}
