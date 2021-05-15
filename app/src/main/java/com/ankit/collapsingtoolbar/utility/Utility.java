package com.ankit.collapsingtoolbar.utility;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.TextView;

import com.ankit.collapsingtoolbar.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utility {

    public static float getTextWidth(Paint paint, String text) {
        return paint.measureText(text);
    }
}
