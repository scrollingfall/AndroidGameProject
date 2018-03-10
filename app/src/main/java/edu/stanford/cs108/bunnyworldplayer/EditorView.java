package edu.stanford.cs108.bunnyworldplayer;

/**
 * Created by sam on 3/9/18.
 */

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import java.util.*;

public class EditorView extends View {
    private int viewWidth, viewHeight;

    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // check if touching coordinates that a shape contains
        float shapeX = event.getX();
        float shapeY = event.getY();
        // how do I know which page I'm looking at, so I can look at this page's shapes???
        //for (int i = 0; i < )

        return true;
    }
}
