package com.yechaoa.multipleitempage.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import com.google.gson.Gson;
import com.yechaoa.multipleitempage.DialogActivity;
import com.yechaoa.multipleitempage.R;
import com.yechaoa.multipleitempage.RemotePDFActivity;
import com.yechaoa.multipleitempage.dialog.DialogHelper;
import com.yechaoa.multipleitempage.dialog.inf.OnDialogCancelListener;
import com.yechaoa.multipleitempage.dialog.inf.OnDialogConfirmDataListener;
import com.yechaoa.yutils.YUtils;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class Fragment2 extends Fragment implements OnDialogCancelListener {

    private DialogHelper mDialogHelper;
    private ProgressDialog mSaveProgressDlg;
    private static final int MSG_SAVE_SUCCESS = 1;
    private static final int MSG_SAVE_FAILED = 2;
    private static final int BITMAP_LEFT_TOP = 101;
    private static final int BITMAP_LEFT_BOOTOM = 102;
    private static final int BITMAP_RIGHT_TOP = 103;
    private static final int BITMAP_RIGHT_BOTTOM = 104;
    private ImageView mLeftTopPrintView;
    private ImageView mLeftBottomPrintView;
    private ImageView mRightTopPrintView;
    private ImageView mRightBottomPrintView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (mDialogHelper == null) {
            mDialogHelper = new DialogHelper(this.getActivity(), this);
        }
        return inflater.inflate(R.layout.fragment_fragment2, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        TextView tv_submit_view = getActivity().findViewById(R.id.tv_submit);
        tv_submit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FromBeanInfo formInfo = new FromBeanInfo();
                formInfo.setSampleName(findViewByEditTextId(R.id.sub_iv_sample_name));
                formInfo.setTrademark(findViewByEditTextId(R.id.sub_iv_mark));
                formInfo.setGrade(findViewByEditTextId(R.id.sub_iv_level));
                formInfo.setSpecificationsModels(findViewByEditTextId(R.id.sub_iv_mode));
                formInfo.setProductStandard(findViewByEditTextId(R.id.sub_iv_product_standard));
                formInfo.setManufactureDate(findViewByEditTextId(R.id.sub_iv_manufacture_data));
                formInfo.setSampleNo(findViewByEditTextId(R.id.sub_iv_smaple_no));
                formInfo.setSalePrice(findViewByEditTextId(R.id.sub_iv_sale_price));
                formInfo.setPurchaseVolume(findViewByEditTextId(R.id.sub_iv_purchase_volume));
                formInfo.setSalesVolume(findViewByEditTextId(R.id.sub_iv_sale_volume));
                formInfo.setInventory(findViewByEditTextId(R.id.sub_iv_inventory));
                formInfo.setCheckSampleCount(findViewByEditTextId(R.id.sub_iv_check_sample_count));
                formInfo.setBackupSampleCount(findViewByEditTextId(R.id.sub_iv_backup_sample_count));
                formInfo.setBackupSampleSealupAddress(findViewByEditTextId(R.id.sub_iv_backup_sample_a));
                formInfo.setInventoryAddress(findViewByEditTextId(R.id.sub_iv_inventory_a));
                formInfo.setOperator(findViewByEditTextId(R.id.sub_iv_operator));
                formInfo.setOperatorAddress(findViewByEditTextId(R.id.sub_iv_operator_a));
                formInfo.setOperatorPostalcode(findViewByEditTextId(R.id.sub_iv_operator_postalcode));
                formInfo.setOperatorLegalRepresentative(findViewByEditTextId(R.id.sub_iv_operator_owner));
                formInfo.setOperatorPhone(findViewByEditTextId(R.id.sub_iv_operator_owner_phone));
                formInfo.setOperatorFax(findViewByEditTextId(R.id.sub_iv_operator_owner_fax));
                formInfo.setOperatorContacts(findViewByEditTextId(R.id.sub_iv_operator_contacts));
                formInfo.setOperatorMobile(findViewByEditTextId(R.id.sub_iv_operator_mobile_phone));
                formInfo.setOperatorEmail(findViewByEditTextId(R.id.sub_iv_operator_mail));
                //formInfo.setOperatorLocation(findViewByEditTextId(R.id.sub_iv));radiobutton
                //formInfo.setCheckPlace(findViewByEditTextId(R.id.sub_iv));radionbutton
                formInfo.setProducerSupplier(findViewByEditTextId(R.id.sub_iv_product_supplier));
                formInfo.setProducerSupplierAddress(findViewByEditTextId(R.id.sub_iv_product_supplier_addi));
                formInfo.setProducerSupplierContacts(findViewByEditTextId(R.id.sub_iv_product_supplier_contacts));
                formInfo.setProducerSupplierPhone(findViewByEditTextId(R.id.sub_iv_product_supplier_phone));
                formInfo.setProducerSupplierFax(findViewByEditTextId(R.id.sub_iv_product_supplier_fax));
//                formInfo.setNoticeYear(findViewByEditTextId(R.id.sub_iv));
//                formInfo.setSampleName(findViewByEditTextId(R.id.sub_iv));
//                formInfo.setSampleName(findViewByEditTextId(R.id.sub_iv));
//                formInfo.setSampleName(findViewByEditTextId(R.id.sub_iv));


//                Intent intent = new Intent();
//                intent.setClass(getActivity(), DialogActivity.class);
//                startActivityForResult(intent, 101);

                startActivity(new Intent(getActivity(), RemotePDFActivity.class));



//                if (mSaveProgressDlg == null) {
//                    initSaveProgressDlg();
//                }
//                mSaveProgressDlg.show();
//
//
//                OkHttpClient client = new OkHttpClient();
//                // 聚合数据
//                String apiUrl = "www.bing.com";
//                Request request = new Request.Builder().url(apiUrl).build();
//                client.newCall(request).enqueue(new Callback() {
//                    @Override
//                    public void onFailure(Call call, IOException e) {
//                        mSaveProgressDlg.dismiss();
//                        mHandler.obtainMessage(MSG_SAVE_FAILED).sendToTarget();
//                    }
//
//                    @Override
//                    public void onResponse(Call call, Response response) throws IOException {
//                        mSaveProgressDlg.dismiss();
//                        String result = response.body().string();
//                        if (!TextUtils.isEmpty(result)) {
//                            Gson gson = new Gson();
//                            EntityInfo entityInfo = gson.fromJson(result, EntityInfo.class);
//
//                        }
//                    }
//                });



//                YUtils.showToast(ss);
//                showConfirmDialog();


            }
        });


        mLeftTopPrintView = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_left_top_control).findViewById(R.id.tv_sign_img);
        mLeftTopPrintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strtFinterActivity(BITMAP_LEFT_TOP);
            }
        });

        mLeftBottomPrintView = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_left_bottom_control).findViewById(R.id.tv_sign_img);
        mLeftBottomPrintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strtFinterActivity(BITMAP_LEFT_BOOTOM);
            }
        });

        mRightTopPrintView = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_right_top_control).findViewById(R.id.tv_sign_img);
        mRightTopPrintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strtFinterActivity(BITMAP_RIGHT_TOP);
            }
        });

        mRightBottomPrintView = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_right_bottom_control).findViewById(R.id.tv_sign_img);
        mRightBottomPrintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strtFinterActivity(BITMAP_RIGHT_BOTTOM);
            }
        });
    }

    private String findViewByEditTextId(int id){
        EditText ed = (EditText) getActivity().findViewById(id).findViewById(R.id.et_input);
        return ed.getText().toString();
    }
    @Override
    public void onDialogCancelListener(AlertDialog dialog) {

        YUtils.showToast( "弹窗关闭");
    }

    public void showConfirmDialog() {
        mDialogHelper.showConfirmDataDialog("ConfirmDialog", "确定", "取消", new OnDialogConfirmDataListener() {
            @Override
            public void onDialogConfirmDataListener(Bundle data) {
                String str = data.getString("string");
                YUtils.showToast(str);
            }
        }, new OnDialogCancelListener() {
            @Override
            public void onDialogCancelListener(AlertDialog dialog) {
                YUtils.showToast("cancel");
            }
        });
    }

    private void strtFinterActivity(int requestCode){
        Intent intent = new Intent();
        intent.setClass(getActivity(), DialogActivity.class);
        startActivityForResult(intent, requestCode);
    }
    private void initSaveProgressDlg() {
        mSaveProgressDlg = new ProgressDialog(getContext());
        mSaveProgressDlg.setMessage("正在提交,请稍候...");
        mSaveProgressDlg.setCancelable(false);
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_SAVE_FAILED:
                    mSaveProgressDlg.dismiss();
                    YUtils.showToast("提交失败");
                    break;
                case MSG_SAVE_SUCCESS:

                    break;
                default:
                    break;
            }
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ){
            Bundle bundle = data.getBundleExtra("bundle");
            Bitmap bitmap = bundle.getParcelable("bitmap");

            if (bitmap != null) {
                if (BITMAP_LEFT_TOP == requestCode) {
                    mLeftTopPrintView.setImageBitmap(bitmap);
                }else if (BITMAP_LEFT_BOOTOM == requestCode){
                    mLeftBottomPrintView.setImageBitmap(bitmap);
                }else if (BITMAP_RIGHT_TOP == requestCode){
                    mRightTopPrintView.setImageBitmap(bitmap);
                }else if (BITMAP_RIGHT_BOTTOM == requestCode){
                    mRightBottomPrintView.setImageBitmap(bitmap);
                }
            }
        }
    }
}


//mImageView.setDrawingCacheEnabled(true);
//        Bitmap bitmap = Bitmap.createBitmap(mImageView.getDrawingCache());
//        mImageView.setDrawingCacheEnabled(false);
