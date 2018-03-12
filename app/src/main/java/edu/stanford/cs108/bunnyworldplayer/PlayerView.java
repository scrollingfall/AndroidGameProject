package edu.stanford.cs108.bunnyworldplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vsinghi on 3/11/18.
 */

public class PlayerView extends View {
    private Page page;

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }


    public Page getPageCurrentlyOn() { return this.page; }
    public void setPageCurrentlyOn(Page currentPage) { this.page = currentPage; }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);


        ArrayList<Shape> shapes = page.getShapeList();
        //System.out.println(shapes.toString());
        for (int i = 0; i < shapes.size(); i++) {
            //System.out.println(shapes.size());
            shapes.get(i).draw(canvas);
        }
    }

    public void drawPage(Page page) {
        this.page = page;
        invalidate();
    }



}
