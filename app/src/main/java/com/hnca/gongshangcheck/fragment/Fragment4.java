package com.hnca.gongshangcheck.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hnca.gongshangcheck.PenPrintActivity;
import com.hnca.gongshangcheck.R;

import static android.app.Activity.RESULT_OK;

public class Fragment4 extends Fragment {
    private final static String TAG = "Fragment3";
    TextView texttest;
    ImageView imgPreview;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_fragment4, container, false);
    }

    @Override
    public void onStart() {
        super.onStart();
        texttest = getActivity().findViewById(R.id.tv_showsign);
        imgPreview = getActivity().findViewById(R.id.iv_previewimg);

        texttest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), PenPrintActivity.class);

                startActivityForResult(intent, 1001);

            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK ){
            Log.i(TAG, "onActivityResult: ");
            Bundle bundle = data.getBundleExtra("bundle");
            Bitmap bitmap = bundle.getParcelable("bitmap");
            //String savePath = data.getStringExtra(PenConfig.SAVE_PATH);
            //texttest.setText(savePath);
            //Bitmap bitmap = BitmapFactory.decodeFile(savePath);
            if (bitmap != null) {
                Log.i(TAG, "setImageBitmap is call: ");
                imgPreview.setImageBitmap(bitmap);
            }
        }
    }
}
