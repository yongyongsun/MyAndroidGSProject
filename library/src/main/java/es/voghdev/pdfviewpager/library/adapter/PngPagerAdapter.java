package es.voghdev.pdfviewpager.library.adapter;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.pdf.PdfRenderer;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.davemorrissey.labs.subscaleview.ImageSource;
import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import es.voghdev.pdfviewpager.library.R;

public class PngPagerAdapter extends PagerAdapter {

    protected Context context;
    protected LayoutInflater inflater;
    private List<Bitmap> mListsBitmap;
    private String mPngPath;
    public PngPagerAdapter(Context context, String pngPath) {
        this.context = context;
        this.mPngPath = pngPath;
        inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        mListsBitmap = new ArrayList<Bitmap>();
        try {
            FileInputStream fis = new FileInputStream(mPngPath);
            Bitmap bitmap  = BitmapFactory.decodeStream(fis);
            mListsBitmap.add(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View v = inflater.inflate(R.layout.view_pdf_page, container, false);
        SubsamplingScaleImageView ssiv = v.findViewById(R.id.subsamplingImageView);

        Bitmap bitmap = mListsBitmap.get(position);

        ssiv.setImage(ImageSource.bitmap(bitmap));
        ((ViewPager) container).addView(v, 0);

        return v;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        // bitmap.recycle() causes crashes if called here.
        // All bitmaps are recycled in close().
    }

    @Override
    public int getCount() {
        return mListsBitmap.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == (View) object;
    }

    public void close() {
        mListsBitmap.clear();
    }
}
