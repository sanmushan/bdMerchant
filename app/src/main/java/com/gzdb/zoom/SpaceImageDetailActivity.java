package com.gzdb.zoom;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;


public class SpaceImageDetailActivity extends Activity {

    private ArrayList<String> mDatas;
    private int mPosition;
    private int mLocationX;
    private int mLocationY;
    private int mWidth;
    private int mHeight;
    SmoothImageView imageView = null;

    boolean num = false;
    float scaleWidth;
    float scaleHeight;
    Bitmap bitmap = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mDatas = (ArrayList<String>) getIntent().getSerializableExtra("images");

        mPosition = getIntent().getIntExtra("position", 0);
        mLocationX = getIntent().getIntExtra("locationX", 0);
        mLocationY = getIntent().getIntExtra("locationY", 0);
        mWidth = getIntent().getIntExtra("width", 0);
        mHeight = getIntent().getIntExtra("height", 0);

        imageView = new SmoothImageView(this);
        imageView.setOriginalInfo(mWidth, mHeight, mLocationX, mLocationY);
        imageView.transformIn();
        imageView.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
        imageView.setScaleType(ScaleType.FIT_CENTER);
        setContentView(imageView);
        ImageLoader.getInstance().displayImage(mDatas.get(mPosition), imageView);
        Log.e("linbin", "---------" + mDatas.get(mPosition));

    }

    @Override
    public void onBackPressed() {
        imageView.setOnTransformListener(new SmoothImageView.TransformListener() {
            @Override
            public void onTransformComplete(int mode) {
                if (mode == 2) {
                    finish();
                }
            }
        });
        imageView.transformOut();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (isFinishing()) {
            overridePendingTransition(0, 0);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (num == true) {
                    Matrix matrix = new Matrix();
                    matrix.postScale(scaleWidth, scaleHeight);
//					Bitmap bitmap = Bitmap.createBitmap(bitmap,0,0,bitmap.getWidth(),bitmap.getHeight(),matrix,true);
//					imageView.setImageBitmap(bitmap);
                    num = false;
					finish();
                } else {

                    Matrix matrix = new Matrix();
                    matrix.postScale(1.0f, 1.0f);
//					Bitmap newBitmap=Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
//					imageView.setImageBitmap(newBitmap);
                    num = true;
//                    finish();
                }
                break;
        }
        return super.onTouchEvent(event);
    }
}
