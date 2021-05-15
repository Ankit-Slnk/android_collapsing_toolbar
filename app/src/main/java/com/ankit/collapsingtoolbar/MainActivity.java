package com.ankit.collapsingtoolbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ankit.collapsingtoolbar.utility.AppBarStateChangeListener;
import com.ankit.collapsingtoolbar.utility.Utility;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;

public class MainActivity extends AppCompatActivity {

    private AppBarLayout mAppBarLayout;
    private TextView mToolbarTextView, mTitleTextView;
    private Toolbar mToolBar;
    private AppBarStateChangeListener mAppBarStateChangeListener;
    private final float[] mToolbarTextPoint = new float[2];
    private final float[] mTitleTextViewPoint = new float[2];
    private float mTitleTextSize;
    ImageView menuImageView;
    boolean isImage = true;
    CollapsingToolbarLayout collapsingToolbar;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAppBarLayout = findViewById(R.id.app_bar);
        mToolbarTextView = findViewById(R.id.toolbar_title);
        mTitleTextView = findViewById(R.id.textView_title);
        mToolBar = findViewById(R.id.menuToolbar);
        menuImageView = findViewById(R.id.menuImageView);
        collapsingToolbar = findViewById(R.id.collapsingToolbar);
        toolbar = findViewById(R.id.toolbar);

        mTitleTextSize = mTitleTextView.getTextSize();

        setUpToolbar();
        setUpAmazingAvatar();
        setSimpleToolbar();

        if (isImage) {
            collapsingToolbar.setVisibility(View.VISIBLE);
            toolbar.setVisibility(View.GONE);
        } else {
            collapsingToolbar.setVisibility(View.GONE);
            toolbar.setVisibility(View.VISIBLE);
        }
    }


    private void setSimpleToolbar() {
        toolbar.setTitle("Hungry Birds");
        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void setUpToolbar() {
        mAppBarLayout.requestLayout();

        setSupportActionBar(mToolBar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
    }

    private void setUpAmazingAvatar() {
        mAppBarStateChangeListener = new AppBarStateChangeListener() {

            @Override
            public void onStateChanged(AppBarLayout appBarLayout,
                                       AppBarStateChangeListener.State state) {
            }

            @Override
            public void onOffsetChanged(AppBarStateChangeListener.State state, float offset) {
                translationView(offset);
            }
        };
        mAppBarLayout.addOnOffsetChangedListener(mAppBarStateChangeListener);
    }

    private void translationView(float offset) {
        menuImageView.setAlpha(1 - offset);
        float newTextSize =
                mTitleTextSize - (mTitleTextSize - mToolbarTextView.getTextSize()) * offset;
        Paint paint = new Paint(mTitleTextView.getPaint());
        paint.setTextSize(newTextSize);
        float newTextWidth = Utility.getTextWidth(paint, mTitleTextView.getText().toString());
        paint.setTextSize(mTitleTextSize);
        float originTextWidth = Utility.getTextWidth(paint, mTitleTextView.getText().toString());
        float xTitleOffset = originTextWidth - newTextWidth;
        float yTitleOffset = (mToolbarTextPoint[1] - mTitleTextViewPoint[1]) * offset;
        mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
        mTitleTextView.setTranslationX(xTitleOffset);
        mTitleTextView.setTranslationY(yTitleOffset);
    }

    private void resetPoints(boolean isTextChanged) {
        final float offset = mAppBarStateChangeListener.getCurrentOffset();

        int[] toolbarTextPoint = new int[2];
        mToolbarTextView.getLocationOnScreen(toolbarTextPoint);
        mToolbarTextPoint[0] = toolbarTextPoint[0];
        mToolbarTextPoint[1] = toolbarTextPoint[1];

        Paint paint = new Paint(mTitleTextView.getPaint());
        float newTextWidth = Utility.getTextWidth(paint, mTitleTextView.getText().toString());
        paint.setTextSize(mTitleTextSize);
        float originTextWidth = Utility.getTextWidth(paint, mTitleTextView.getText().toString());
        int[] titleTextViewPoint = new int[2];
        mTitleTextView.getLocationOnScreen(titleTextViewPoint);
        mTitleTextViewPoint[0] = titleTextViewPoint[0] - mTitleTextView.getTranslationX() -
                (mToolbarTextView.getWidth() > newTextWidth ?
                        (originTextWidth - newTextWidth) / 2f : 0);
        mTitleTextViewPoint[1] = titleTextViewPoint[1] - mTitleTextView.getTranslationY();

        if (isTextChanged) {
            new Handler().post(new Runnable() {

                @Override
                public void run() {
                    translationView(offset);
                }
            });
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!hasFocus) {
            return;
        }
        resetPoints(false);
    }
}