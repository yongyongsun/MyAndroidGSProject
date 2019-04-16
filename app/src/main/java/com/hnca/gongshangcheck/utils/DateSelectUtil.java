package com.hnca.gongshangcheck.utils;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.text.TextUtils;
import android.widget.DatePicker;

public class DateSelectUtil {
    private static DatePickerDialog mDatePickerDialog;//日期选择器

    /**
     * 显示日期选择器, 默认白色背景
     */
    public static DateSelectUtil showDatePickerDialog(Context context, String title, int year, int month, int day, OnDatePickerListener onDateTimePickerListener) {
        return showDatePickerDialog(context, AlertDialog.THEME_HOLO_LIGHT, title, year, month, day, onDateTimePickerListener);
    }
    public static DateSelectUtil showDatePickerDialog(Context context, int themeId, String title, int year, int month, int day,
                                                       final OnDatePickerListener onDateTimePickerListener) {
        mDatePickerDialog = new DatePickerDialog(context, themeId, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month + 1;//月份加一
                if (onDateTimePickerListener != null) {
                    onDateTimePickerListener.onConfirm(year, month, dayOfMonth);
                }
            }

        }, year, month - 1, day);//月份减一

        mDatePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                if (onDateTimePickerListener != null) {
                    onDateTimePickerListener.onCancel();
                }
            }
        });

        if (!TextUtils.isEmpty(title)) {
            mDatePickerDialog.setTitle(title);
        }
        mDatePickerDialog.show();
        return new DateSelectUtil();
    }


    /**
     * 日期选择器监听
     */
    public interface OnDatePickerListener {
        void onConfirm(int year, int month, int dayOfMonth);

        void onCancel();
    }


}
