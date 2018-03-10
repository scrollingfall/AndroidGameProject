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
    private Shape currentlySelected;

    private Game game;
    public void setGame(Game game) {
        this.game = game;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                currentlySelected = getTopAt(event.getX(), event.getY());
                if (currentlySelected != null) {
                    startx = event.getX();
                    starty = event.getY();
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                invalidate();
            case MotionEvent.ACTION_UP:
                invalidate();
        }
        return true;
    }

    private Shape getTopAt(float x, float y) {
        for (int i = game.getCurrentPage().getShapeList().size() - 1; i >= 0; i--)
            if (game.getCurrentPage().getShapeList().get(i).isTouched(x, y))
                return game.getCurrentPage().getShapeList().get(i);
        return null;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        for (Shape s: game.getCurrentPage().getShapeList())
            s.draw(canvas);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
