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
    TextView pageName = (TextView) ((Activity) getContext()).findViewById(R.id.pageName);
    Page page = new Page("page1", 200,200);

    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ArrayList<Shape> shapes = page.getShapeList();
        for (int i = 0; i < shapes.size(); i++) {
            shapes.get(i).draw(canvas);
        }
    }

    public void drawShape(Page page) {
        this.page = page;
        invalidate();
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
        //System.out.println(pageName.getText());

        HashMap<String, Page> pages = EditorActivity.newGame.getPages();

        for (int i = 0; i < pages.size(); i++) {

        }

        return true;
    }
}
