package edu.stanford.cs108.bunnyworldplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
//import android.support.annotation.Nullable;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;

/**
 * Created by Jerry Chen on 3/6/2018.
 */

public class PlayerView extends View {
    public PlayerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private float startx, starty;
    private float oldSelectx = -1;
    private float oldSelecty = -1;
    private Shape currentlySelected;
    private int currentlySelectedIndex;

    private boolean justentered;
    private Game game;

    private static int clickThreshold = 0;
    public void setGame(Game game) {
        this.game = game;
        justentered = true;
        this.game.setEditorMode(false);
    }

    public void setSize(float x, float y) {
        game.setSize(x, y);
    }

    @Override
    //todo backpack
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                getTopAt(event.getX(), event.getY(), -1);
                if (currentlySelected != null) {
                    startx = event.getX();
                    starty = event.getY();
                    oldSelectx = currentlySelected.getX();
                    oldSelecty = currentlySelected.getY();
                    if (currentlySelected.isMoveable()) {
                        for (Shape s: game.getCurrentPage().getDropTargets(currentlySelected)) {
                            s.setSelected(true);
                        }
                        invalidate();
                    }
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (currentlySelected != null && currentlySelected.isMoveable()) {
                    float dx = event.getX() - startx;
                    float dy = event.getY() - starty;
                    currentlySelected.move(oldSelectx + dx, oldSelecty + dy);
                    invalidate();
                }
            case MotionEvent.ACTION_UP:
                if (currentlySelected != null) {
                    if (Math.abs(event.getX() - startx) <= clickThreshold && Math.abs(event.getY() - starty) <= clickThreshold) { //counts as click if little movement occurred
                        if (currentlySelected.performScriptAction("on-click")) {
                            String transition = currentlySelected.getTransition();
                            if (!transition.isEmpty()) {
                                game.setCurrentPage(transition); //are we doing error checking on valid pages?
                                justentered = true;
                            }
                        }
                    } else if (currentlySelected.isMoveable()) { //otherwise counts as drag
                        Page oldPage = game.getCurrentPage();
                        for (Shape s : game.getCurrentPage().getDropTargets(currentlySelected)) {
                            s.setSelected(false);
                        }
                        Shape oldSelect = currentlySelected;
                        getTopAt(event.getX(), event.getY(), currentlySelectedIndex);
                        if (currentlySelected != null && currentlySelected.performScriptAction("on-drop " + oldSelect.getName())) {
                            String transition = currentlySelected.getTransition();
                            if (!transition.isEmpty()) {
                                game.setCurrentPage(transition); //are we doing error checking on valid pages?
                                justentered = true;
                            }
                        } else if (currentlySelected != null){
                            oldSelect.move(startx, starty);
                        }
                        if (oldSelect.getY() >= oldPage.getHeight()* Page.percentMainPage
                                && oldSelecty < oldPage.getHeight() * Page.percentMainPage)
                            oldPage.moveToBackpack(oldSelect.getName());
                        else if (oldSelect.getY() < oldPage.getHeight()* Page.percentMainPage
                                && oldSelecty >= oldPage.getHeight() * Page.percentMainPage)
                            oldPage.moveFromBackpack(oldSelect.getName());
                    }
                    currentlySelected = null;
                    currentlySelectedIndex = -1;
                    invalidate();
                }
        }
        return true;
    }

    private void getTopAt(float x, float y, int avoid) {
        for (int i = game.getCurrentPage().getShapeList().size() - 1; i >= 0; i--) {
            if (i != avoid && game.getCurrentPage().getShapeList().get(i).isTouched(x, y)) {
                currentlySelected = game.getCurrentPage().getShapeList().get(i);
                currentlySelectedIndex = i;
                return;
            }
        }
        currentlySelected = null;
        currentlySelectedIndex = -1;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (game != null) {
            if (justentered) {
                game.getCurrentPage().onEnter();
                justentered = false;
            }
            game.getCurrentPage().draw(canvas);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }
}
