package com.yechaoa.multipleitempage.dialog.inf;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * 确认按钮点击的回调
 */
public interface OnDialogConfirmDataListener {
    /**
     * 确定按钮点击的回调
     * @param dialog 弹窗
     */
    void onDialogConfirmDataListener(Bundle str);
}
