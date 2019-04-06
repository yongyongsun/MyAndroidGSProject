package com.yechaoa.multipleitempage.dialog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.support.v7.app.AlertDialog;

public class MyAlertDialog extends AlertDialog {
    public MyAlertDialog(@NonNull Context context) {
        this(context, 0);
    }

    public MyAlertDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    public MyAlertDialog(@NonNull Context context, boolean cancelable,
                          @Nullable OnCancelListener cancelListener) {
        this(context, 0);
    }
}
