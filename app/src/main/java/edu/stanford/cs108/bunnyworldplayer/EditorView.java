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
    private int viewWidth = Integer.MAX_VALUE; // Will get overwritten
    private int viewHeight = Integer.MAX_VALUE;
    Shape touchedShape = null;
    private float origX, origY, origTouchX, origTouchY;
    Page page = new Page("page1", 200,200, "game1");

    public EditorView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        page.setEditorMode(true);
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

        ArrayList<Shape> shapes = this.page.getShapeList();

        TextView shapeNameField = (TextView) ((Activity) getContext()).findViewById(R.id.shapeNameField);
        TextView xField = (TextView) ((Activity) getContext()).findViewById(R.id.xField);
        TextView yField = (TextView) ((Activity) getContext()).findViewById(R.id.yField);
        TextView widthField = (TextView) ((Activity) getContext()).findViewById(R.id.widthField);
        TextView heightField = (TextView) ((Activity) getContext()).findViewById(R.id.heightField);

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                // find shape that was touched (if any was)
                for (int i = shapes.size() - 1; i >= 0; i--) {

                    float x = shapes.get(i).getX();
                    if (x < 0) x = 0;

                    float y = shapes.get(i).getY();
                    if (y < 0) y = 0;

                    float width = shapes.get(i).getWidth();
                    float height = shapes.get(i).getHeight();
                    if (touchX >= x && touchX <= x + width && touchY >= y && touchY <= y + height) {
                        touchedShape = shapes.get(i);
                        shapeNameField.setText(touchedShape.getName());
                        xField.setText(Float.toString(x));
                        yField.setText(Float.toString(y));
                        widthField.setText(Float.toString(width));
                        heightField.setText(Float.toString(height));
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
                    shapeNameField.setText("--");
                    xField.setText("--");
                    yField.setText("--");
                    widthField.setText("--");
                    heightField.setText("--");
                }
                break;

            case MotionEvent.ACTION_MOVE:
                if (touchedShape != null) {
                    float deltaX = event.getX() - origTouchX;
                    float deltaY = deltaY = event.getY() - origTouchY;
                    if (origX + deltaX <= viewWidth - touchedShape.getWidth()
                            && origY + deltaY <= viewHeight - touchedShape.getHeight()) {
                        touchedShape.move(origX + deltaX, origY + deltaY);
                    } else if (origX + deltaX > viewWidth - touchedShape.getWidth()
                            && origY + deltaY <= viewHeight - touchedShape.getHeight()) {
                        touchedShape.move(viewWidth - touchedShape.getWidth(), origY + deltaY);
                    } else if (origX + deltaX <= viewWidth - touchedShape.getWidth()
                            &&origY + deltaY > viewHeight - touchedShape.getHeight()) {
                        touchedShape.move(origX + deltaX, viewHeight - touchedShape.getHeight());
                    } else {
                        break;
                    }

                    if (origX + deltaX >= viewWidth - touchedShape.getWidth()) {
                        xField.setText(Float.toString(viewWidth - touchedShape.getWidth()));
                    } else if (origX + deltaX > 0) {
                        xField.setText(Float.toString(origX + deltaX));
                    } else
                        xField.setText("0");

                    if (origY + deltaY >= viewHeight - touchedShape.getHeight()) {
                        yField.setText(Float.toString(viewHeight - touchedShape.getHeight()));
                    } else if (origY + deltaY > 0) {
                        yField.setText(Float.toString(origY + deltaY));
                    } else {
                        yField.setText("0");
                    }
                    widthField.setText(Float.toString(touchedShape.getWidth()));
                    heightField.setText(Float.toString(touchedShape.getHeight()));
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
