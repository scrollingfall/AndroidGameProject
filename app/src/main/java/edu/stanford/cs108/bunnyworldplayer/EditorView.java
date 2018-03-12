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
    private float origX, origY;
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
        Shape touchedShape = null;
        boolean touched = false;

        for (int i = shapes.size() - 1; i >= 0; i--) {

            float x = shapes.get(i).getX();
            float y = shapes.get(i).getY();
            float width = shapes.get(i).getWidth();
            float height = shapes.get(i).getHeight();
            if (touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height) {
                touchedShape = shapes.get(i);
                touched = true;
                break;
            }
        }

        if (touched) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    origX = touchedShape.getX();
                    origY = touchedShape.getY();
                    Shape selectedShape = this.page.getSelectedShape();
                    if (selectedShape != null) selectedShape.setSelected(false); // unselect old
                    this.page.setSelectedShape(touchedShape); // select new
                    touchedShape.setSelected(true);
                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = event.getX() - origX;
                    float deltaY = event.getY() - origY;
                    System.out.println(deltaX);
                    System.out.println(deltaX);
                    touchedShape.move(origX + deltaX, origY + deltaY);
                    break;

            }
        } else {
            // clear any existing selection
            Shape selectedShape = this.page.getSelectedShape();
            if (selectedShape != null) {
                selectedShape.setSelected(false);
                this.page.setSelectedShape(null);
            }
        }

        invalidate();
        return true;
    }
}
