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
    Shape touchedShape = null;
    private float origX, origY, origTouchX, origTouchY;
    Page page = new Page("page1", 200,200, "game1");

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

    public void drawPage(Page page) {
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
        float touchX = event.getX();
        float touchY = event.getY();

        TextView pageName = (TextView) ((Activity) getContext()).findViewById(R.id.pageName);
        Page p = EditorActivity.newGame.getPage((String)pageName.getText());
        ArrayList<Shape> shapes = p.getShapeList();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // find shape that was touched (if any was)
                for (int i = shapes.size() - 1; i >= 0; i--) {
                    float x = shapes.get(i).getX();
                    float y = shapes.get(i).getY();
                    float width = shapes.get(i).getWidth();
                    float height = shapes.get(i).getHeight();
                    if (touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height) {
                        touchedShape = shapes.get(i);
                        break;
                    }
                }
                if (touchedShape != null) {
                    origX = touchedShape.getX();
                    origY = touchedShape.getY();
                    origTouchX = touchX;
                    origTouchY = touchY;
                    Shape selectedShape = this.page.getSelectedShape();
                    if (selectedShape != null) selectedShape.setSelected(false); // unselect old
                    this.page.setSelectedShape(touchedShape); // select new
                    touchedShape.setSelected(true);
                } else {
                    // clear any existing selection
                    Shape selectedShape = this.page.getSelectedShape();
                    if (selectedShape != null) {
                        selectedShape.setSelected(false);
                        this.page.setSelectedShape(null);
                    }
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (touchedShape != null) {
                    float deltaX = event.getX() - origTouchX;
                    float deltaY = event.getY() - origTouchY;
                    System.out.println("Before move:");
                    System.out.println(touchedShape.getX());
                    System.out.println(touchedShape.getY());
                    touchedShape.move(origX + deltaX, origY + deltaY);
                    System.out.println("After move:");
                    System.out.println(touchedShape.getX());
                    System.out.println(touchedShape.getY());
                }
                break;

            case MotionEvent.ACTION_UP:
                touchedShape = null;
                break;
        }

        invalidate();
        return true;
    }
}
