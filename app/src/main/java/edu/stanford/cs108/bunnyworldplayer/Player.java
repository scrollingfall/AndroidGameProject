package edu.stanford.cs108.bunnyworldplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by Jerry Chen on 3/6/2018.
 */

public class Player extends View {
    public Player(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private float startx, starty;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                startx = event.getX();
                starty = event.getY();
                invalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
            case MotionEvent.ACTION_UP:
                invalidate();
        }
        return true;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
