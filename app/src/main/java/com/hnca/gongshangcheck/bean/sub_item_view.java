package com.hnca.gongshangcheck.bean;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.Nullable;
import android.text.InputType;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hnca.gongshangcheck.R;

/**
 * 创建人: syy
 * 创建时间:2019-04-03
 * 功能描述:
 */

public class sub_item_view extends LinearLayout {
    public static final String TAG_CLASS_TEXT = "TAG_CLASS_TEXT";//普通文本
    public static final String TAG_CLASS_NUM = "TAG_CLASS_NUM";//数字
    public static final String TAG_CLASS_PHONE = "TAG_CLASS_PHONE";//电话号码
    private TextView textView;//item的文字
    private EditText editText;
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

        editText = findViewById(R.id.et_input);
        String strtype = ta.getString(R.styleable.layout_sub_item_input_type);
        if (strtype != null) {
            if (strtype.equals(TAG_CLASS_TEXT)) {
                editText.setInputType(InputType.TYPE_CLASS_TEXT);
            } else if (strtype.equals(TAG_CLASS_NUM)) {
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
            } else if (strtype.equals(TAG_CLASS_PHONE)) {
                editText.setInputType(InputType.TYPE_CLASS_PHONE);
            }
        }
        ta.recycle();
    }

}
