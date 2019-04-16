package com.hnca.gongshangcheck.fragment;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.king.signature.PaintActivity;
import android.king.signature.config.PenConfig;
import android.king.signature.util.BitmapUtil;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


import com.google.gson.Gson;
import com.hnca.gongshangcheck.App;
import com.hnca.gongshangcheck.DialogActivity;
import com.hnca.gongshangcheck.R;
import com.hnca.gongshangcheck.RemotePDFActivity;
import com.hnca.gongshangcheck.dialog.DialogHelper;
import com.hnca.gongshangcheck.dialog.inf.OnDialogCancelListener;
import com.hnca.gongshangcheck.dialog.inf.OnDialogConfirmDataListener;
import com.hnca.gongshangcheck.utils.DateSelectUtil;
import com.yechaoa.yutils.SpUtil;
import com.yechaoa.yutils.YUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.app.Activity.RESULT_OK;

public class Fragment2 extends Fragment implements OnDialogCancelListener {

    private DialogHelper mDialogHelper;
    private ProgressDialog mSaveProgressDlg;
    private static final int MSG_SAVE_SUCCESS = 1;
    private static final int MSG_SAVE_FAILED = 2;
    private static final int BITMAP_LEFT_TOP1 = 101;
    private static final int BITMAP_LEFT_TOP2 = 102;
    private static final int BITMAP_LEFT_BOOTOM1 = 103;
    private static final int BITMAP_LEFT_BOOTOM2 = 104;
    private static final int BITMAP_RIGHT_TOP1 = 105;
    private static final int BITMAP_RIGHT_TOP2 = 106;
    private static final int BITMAP_RIGHT_BOTTOM1 = 107;
    private static final int BITMAP_RIGHT_BOTTOM2 = 108;
    private ImageView mLeftTopPrintView;
    private ImageView mLeftTopPrintView2;
    private ImageView mLeftBottomPrintView;
    private ImageView mLeftBottomPrintView2;
    private ImageView mRightTopPrintView;
    private ImageView mRightTopPrintView2;
    private ImageView mRightBottomPrintView;
    private ImageView mRightBottomPrintView2;
    private  Spinner sp_head_class;
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

        //产商品质量抽查类型
        sp_head_class = getActivity().findViewById(R.id.layout_form_header).findViewById(R.id.spinner_head_class);
        //数据
        List<String> data_list = new ArrayList<String>();
        data_list.add("服装、鞋帽");
        data_list.add("布料、毛线");
        data_list.add("家具用品");
        data_list.add("儿童用品");
        data_list.add("家用电器");
        data_list.add("计算机产品");
        data_list.add("通讯产品");
        data_list.add("装修建材");
        data_list.add("照摄像产品");
        data_list.add("卫生用品");
        data_list.add("出版物");
        data_list.add("文化、运动用品");
        data_list.add("宠物及宠物用品");
        data_list.add("首饰");
        data_list.add("五金交电");
        data_list.add("交通工具");
        data_list.add("燃料");
        data_list.add("殡葬用品");
        data_list.add("农资用品");
        data_list.add("成品油");
        data_list.add("其他商品");
        //适配器
        ArrayAdapter<String> arr_adapter= new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, data_list);
        //设置样式
        arr_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //加载适配器
        sp_head_class.setAdapter(arr_adapter);

        //时间选择器
        TextView tv_date_select = getActivity().findViewById(R.id.tv_manufacture_data);
        tv_date_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showYearMonthDayPicker((TextView)tv_date_select);
            }
        });

        TextView tv_submit_view = getActivity().findViewById(R.id.tv_submit);
        tv_submit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FromBeanInfo formInfo = new FromBeanInfo();
                formInfo.setUserId(SpUtil.getString("userId"));
                formInfo.setToken(SpUtil.getString("token"));

                EditText et = getActivity().findViewById(R.id.layout_form_header).findViewById(R.id.sub_iv_head_year).findViewById(R.id.et_input);
                formInfo.setYear(et.getText().toString());
                String  strSpanter = sp_head_class.getSelectedItem().toString();
                formInfo.setGoodsClass(strSpanter);

                formInfo.setSampleName(findViewByEditTextId(R.id.sub_iv_sample_name));
                formInfo.setTrademark(findViewByEditTextId(R.id.sub_iv_mark));
                formInfo.setGrade(findViewByEditTextId(R.id.sub_iv_level));
                formInfo.setSpecificationsModels(findViewByEditTextId(R.id.sub_iv_mode));
                formInfo.setProductStandard(findViewByEditTextId(R.id.sub_iv_product_standard));
                TextView tv_date_select = getActivity().findViewById(R.id.tv_manufacture_data);
                formInfo.setManufactureDate(tv_date_select.getText().toString());
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
                //radiobutton part
                RadioGroup group1 = getActivity().findViewById(R.id.rd_group_operator_location);
                String strRadio1 = getActivity().findViewById(group1.getCheckedRadioButtonId()).getTag().toString();
                formInfo.setOperatorLocation(strRadio1);
                RadioGroup group2 = getActivity().findViewById(R.id.rd_group_check_place);
                String strRadio2 = getActivity().findViewById(group2.getCheckedRadioButtonId()).getTag().toString();
                formInfo.setCheckPlace(strRadio2);

                formInfo.setProducerSupplier(findViewByEditTextId(R.id.sub_iv_product_supplier));
                formInfo.setProducerSupplierAddress(findViewByEditTextId(R.id.sub_iv_product_supplier_addi));
                formInfo.setProducerSupplierContacts(findViewByEditTextId(R.id.sub_iv_product_supplier_contacts));
                formInfo.setProducerSupplierPhone(findViewByEditTextId(R.id.sub_iv_product_supplier_phone));
                formInfo.setProducerSupplierFax(findViewByEditTextId(R.id.sub_iv_product_supplier_fax));
                formInfo.setNoticeYear(findViewByEditTextId(R.id.iv_notice_number));
                formInfo.setNoticeWorkNo(findViewByEditTextId(R.id.iv_check_number));

                ImageView iv_left_top1 = getActivity().findViewById(R.id.ly_sign_bottom_view)
                        .findViewById(R.id.sign_left_top_control).findViewById(R.id.tv_sign_img);
                formInfo.setOperatorOfficialSeals(getViewBitmapBase64(iv_left_top1));

                ImageView iv_left_top2 = getActivity().findViewById(R.id.ly_sign_bottom_view)
                        .findViewById(R.id.sign_left_top_control).findViewById(R.id.tv_sign_print_img);
                formInfo.setOperatorSign(getViewBitmapBase64(iv_left_top2));

                //签章日期
                formInfo.setOperatorSignDate(getCurSysDate());

                ImageView iv_left_bottom1 = getActivity().findViewById(R.id.ly_sign_bottom_view)
                        .findViewById(R.id.sign_left_bottom_control).findViewById(R.id.tv_sign_img);
                formInfo.setSponsorUnitOS(getViewBitmapBase64(iv_left_bottom1));

                ImageView iv_left_bottom2 = getActivity().findViewById(R.id.ly_sign_bottom_view)
                        .findViewById(R.id.sign_left_bottom_control).findViewById(R.id.tv_sign_print_img);
                formInfo.setSponsorUnitSign(getViewBitmapBase64(iv_left_bottom2));
                //市场主办签章日期
                formInfo.setSponsorUnitSignDate(getCurSysDate());

                //市场监督管理执法人员部分
                ImageView iv_right_top = getActivity().findViewById(R.id.ly_sign_bottom_view)
                        .findViewById(R.id.sign_right_top_control).findViewById(R.id.tv_sign_print_img);
                formInfo.setLeoSign(getViewBitmapBase64(iv_right_top));

                formInfo.setLeoSignDate(getCurSysDate());

                //备份样品接收记录
                ImageView iv_right_bottom = getActivity().findViewById(R.id.ly_sign_bottom_view)
                        .findViewById(R.id.sign_right_bottom_control).findViewById(R.id.tv_sign_print_img);
                formInfo.setBsReceiverSign(getViewBitmapBase64(iv_right_bottom));

                formInfo.setBsReceiverSignDate(getCurSysDate());

                //Log.i("Fragment2, formInfo",formInfo.toString());



                Gson g = new Gson();
                String strJson = g.toJson(formInfo);
                //Log.i("Fragment2, strJson",strJson);
                MediaType MEDIA_TYPE_JSON= MediaType.parse("application/json; charset=utf-8");
                OkHttpClient client = new OkHttpClient();
                App appState = (App)getContext().getApplicationContext();
                String apiUrl = appState.getURLForWeekSheet();
                RequestBody requestBody= RequestBody.create(MEDIA_TYPE_JSON,strJson);
                Request request = new Request.Builder().url(apiUrl).post(requestBody).build();
                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        mSaveProgressDlg.dismiss();
                        mHandler.obtainMessage(MSG_SAVE_FAILED).sendToTarget();
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        mSaveProgressDlg.dismiss();
                        String result = response.body().string();
                        if (response.isSuccessful()) {
                            Gson g = new Gson();
                            FormSheetResponseInfo info = g.fromJson(result,FormSheetResponseInfo.class);
                            String strUrlpdf = info.getWorkSheetPDFurl();
                            Log.i("fragment2 pdfurl = ",strUrlpdf);
                            Intent intent = new Intent(getActivity(),RemotePDFActivity.class);
                            intent.putExtra("pdfurl",strUrlpdf);
                            startActivity(intent);

                        }else {
                            Gson g = new Gson();
                            FormSheetResponseInfo info = g.fromJson(result,FormSheetResponseInfo.class);
                            YUtils.showToast("请求失败，错误信息:" + info.getCode());

                        }
                    }
                });

                if (mSaveProgressDlg == null) {
                    initSaveProgressDlg();
                }
                mSaveProgressDlg.show();




            }
        });


        TextView tv_left_top_finter = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_left_top_control).findViewById(R.id.tv_sign_finter_text);
        tv_left_top_finter.setText("经营者(公章)");
        mLeftTopPrintView = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_left_top_control).findViewById(R.id.tv_sign_img);
        mLeftTopPrintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFinterActivity(BITMAP_LEFT_TOP1);
            }
        });
        TextView tv_left_top_print = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_left_top_control).findViewById(R.id.tv_sign_print_text);
        tv_left_top_print.setText("负责任签字");
        mLeftTopPrintView2 = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_left_top_control).findViewById(R.id.tv_sign_print_img);
        mLeftTopPrintView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPaintActivity(BITMAP_LEFT_TOP2);
            }
        });


        TextView tv_left_bottom_finter = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_left_bottom_control).findViewById(R.id.tv_sign_finter_text);
        tv_left_bottom_finter.setText("市场主办单位(公章)");
        mLeftBottomPrintView = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_left_bottom_control).findViewById(R.id.tv_sign_img);
        mLeftBottomPrintView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startFinterActivity(BITMAP_LEFT_BOOTOM1);
            }
        });
        mLeftBottomPrintView2 = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_left_bottom_control).findViewById(R.id.tv_sign_print_img);
        mLeftBottomPrintView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPaintActivity(BITMAP_LEFT_BOOTOM2);
            }
        });


        FrameLayout fl_right_top_finter = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_right_top_control).findViewById(R.id.fl_sign_finter_id);
        fl_right_top_finter.setVisibility(View.GONE);

        TextView tv_right_top_print = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_right_top_control).findViewById(R.id.tv_sign_print_text);
        tv_right_top_print.setText("市场监督管理执法人员签字");
        mRightTopPrintView2 = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_right_top_control).findViewById(R.id.tv_sign_print_img);
        mRightTopPrintView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPaintActivity(BITMAP_RIGHT_TOP2);
            }
        });


        FrameLayout fl_right_bottom_finter = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_right_bottom_control).findViewById(R.id.fl_sign_finter_id);
        fl_right_bottom_finter.setVisibility(View.GONE);

        TextView tv_right_bottom_print = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_right_bottom_control).findViewById(R.id.tv_sign_print_text);
        tv_right_bottom_print.setText("备份样品接收记录签字");
        mRightBottomPrintView2 = getActivity().findViewById(R.id.ly_sign_bottom_view)
                .findViewById(R.id.sign_right_bottom_control).findViewById(R.id.tv_sign_print_img);
        mRightBottomPrintView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startPaintActivity(BITMAP_RIGHT_BOTTOM2);
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

    private void startFinterActivity(int requestCode){
        Intent intent = new Intent();
        intent.setClass(getActivity(), DialogActivity.class);
        startActivityForResult(intent, requestCode);
    }

    private void  startPaintActivity(int requestCode){
        Intent intent = new Intent();
        intent.setClass(getActivity(), PaintActivity.class);

        intent.putExtra("background", Color.WHITE);//画布背景色，默认透明，也是最终生成图片的背景色
//        intent.putExtra("width", 800); //画布宽度，最大值3000，默认占满布局
//        intent.putExtra("height", 800);//画布高度，最大值3000，默认占满布局
        intent.putExtra("crop", true);   //最终的图片是否只截取文字区域
        intent.putExtra("format", PenConfig.FORMAT_PNG); //图片格式
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
            if (BITMAP_LEFT_TOP1 == requestCode) {
                Bundle bundle = data.getBundleExtra("bundle");
                Bitmap bitmap = bundle.getParcelable("bitmap");
                mLeftTopPrintView.setBackground(new BitmapDrawable(bitmap));

            }if (BITMAP_LEFT_TOP2 == requestCode) {
                Bitmap bit = BitmapUtil.getBundlerBitmap();
                Bitmap bit11 = BitmapUtil.zoomImg(bit,mLeftTopPrintView2.getWidth());
                mLeftTopPrintView2.setBackground(new BitmapDrawable(bit11));

            }else if (BITMAP_LEFT_BOOTOM1 == requestCode){
                Bundle bundle = data.getBundleExtra("bundle");
                Bitmap bitmap = bundle.getParcelable("bitmap");
                mLeftBottomPrintView.setBackground(new BitmapDrawable(bitmap));
            }else if (BITMAP_LEFT_BOOTOM2 == requestCode){
                Bitmap bit = BitmapUtil.getBundlerBitmap();
                mLeftBottomPrintView2.setBackground(new BitmapDrawable(BitmapUtil.zoomImg(bit,mLeftBottomPrintView2.getWidth())));
            }else if (BITMAP_RIGHT_TOP2 == requestCode){
                Bitmap bit = BitmapUtil.getBundlerBitmap();
                mRightTopPrintView2.setBackground(new BitmapDrawable(BitmapUtil.zoomImg(bit,mRightTopPrintView2.getWidth())));
            }else if (BITMAP_RIGHT_BOTTOM2 == requestCode){
                Bitmap bit = BitmapUtil.getBundlerBitmap();
                mRightBottomPrintView2.setBackground(new BitmapDrawable(BitmapUtil.zoomImg(bit,mRightBottomPrintView2.getWidth())));
            }

        }
    }

    /*
    获取当前view的bitmap
     */
    private String getViewBitmapBase64(View view){
        view.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        String strBase64 = Base64.encodeToString(Bitmap2Bytes(bitmap), Base64.DEFAULT);
        //strBase64 = strBase64.replaceAll("[\\s*\t\n\r]", "");
        return strBase64;
    }
    private byte[] Bitmap2Bytes(Bitmap bm) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
        return baos.toByteArray();
    }

    private String getCurSysDate(){
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date(System.currentTimeMillis());
        return simpleDateFormat.format(date);
    }

    class FormSheetResponseInfo{
        private  String success;
        private String code;
        private String message;
        private  String workSheetPDFurl;

        public String getSuccess() {
            return success;
        }

        public String getCode() {
            return code;
        }

        public String getMessage() {
            return message;
        }

        public String getWorkSheetPDFurl() {
            return workSheetPDFurl;
        }
    }

    /**
     * 年月日选择
     */
    private void showYearMonthDayPicker(TextView view) {

        Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);
        DateSelectUtil.showDatePickerDialog(getContext(), android.app.AlertDialog.THEME_HOLO_LIGHT,
                "请选择年月日", year, month, day, new DateSelectUtil.OnDatePickerListener(){

            @Override
            public void onConfirm(int year, int month, int dayOfMonth) {
                //YUtils.showToast(year + "-" + month + "-" + dayOfMonth);
                view.setText(year + "年" + month + "月" + dayOfMonth + "日");
            }

            @Override
            public void onCancel() {
                //YUtils.showToast("cancle");
            }
        });
    }
}
