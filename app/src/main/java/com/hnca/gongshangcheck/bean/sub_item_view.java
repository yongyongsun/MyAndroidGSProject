package com.hnca.gongshangcheck.bean;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnca.gongshangcheck.R;

/**
 * 创建人: syy
 * 创建时间:2019-04-03
 * 功能描述:
 */

public class sub_item_view extends LinearLayout {
    private TextView textView;//item的文字
    public sub_item_view(Context context) {
        this(context,null);
    }

    public sub_item_view(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public sub_item_view(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
       LayoutInflater.from(getContext()).inflate(R.layout.layout_sub_item,this);

        TypedArray ta=context.obtainStyledAttributes(attrs,R.styleable.layout_sub_item);
        textView=findViewById(R.id.tv_label);

        textView.setText(ta.getString(R.styleable.layout_sub_item_show_label));
        ta.recycle();
    }

}
