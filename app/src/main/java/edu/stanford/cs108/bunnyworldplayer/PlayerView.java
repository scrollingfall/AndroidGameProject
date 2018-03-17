package edu.stanford.cs108.bunnyworldplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
//import android.support.annotation.Nullable;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;

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
                //System.out.println("down: "+event.getX() + " " +event.getY());
                getTopAt(event.getX(), event.getY(), -1);
                if (currentlySelected != null) {
                    System.out.println(currentlySelected.getName());
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
                //System.out.println("move: "+event.getX() + " " +event.getY());
                if (currentlySelected != null && currentlySelected.isMoveable()) {
                    float dx = event.getX() - startx;
                    float dy = event.getY() - starty;
                    currentlySelected.move(oldSelectx + dx, oldSelecty + dy);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                //System.out.println("up: "+event.getX() + " " +event.getY());
                if (currentlySelected != null) {
                    if (Math.abs(event.getX() - startx) <= clickThreshold && Math.abs(event.getY() - starty) <= clickThreshold) { //counts as click if little movement occurred
                        if (currentlySelected.performScriptAction("on-click")) {
                            String transition = currentlySelected.getTransition();
                            if (!transition.isEmpty()) {
                                game.setCurrentPage(transition); //are we doing error checking on valid pages?
                                justentered = true;
                            }
                            hideOrShowShapes(currentlySelected);
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
                            hideOrShowShapes(currentlySelected);
                        } else if (currentlySelected != null){
                            oldSelect.move(startx, starty);
                        }
                        if (oldSelect.getY() >= oldPage.getHeight()* Page.percentMainPage
                                && oldSelecty < oldPage.getHeight() * Page.percentMainPage) {
                            //System.out.println(oldPage.getName() + " is moving " + oldSelect.getName() + " to backpack");
                            oldPage.moveToBackpack(oldSelect.getName());
                        } else if (oldSelect.getY() < oldPage.getHeight()* Page.percentMainPage
                                && oldSelecty >= oldPage.getHeight() * Page.percentMainPage) {
                            //System.out.println(oldPage.getName() + " is moving " + oldSelect.getName() + " from backpack");
                            oldPage.moveFromBackpack(oldSelect.getName());
                        }
                    }
                    currentlySelected = null;
                    currentlySelectedIndex = -1;
                    oldSelectx = -1;
                    oldSelecty = -1;
                    invalidate();
                }
        }
        return true;
    }

    private void hideOrShowShapes (Shape current) {
        if (current == null || game == null) return;
        ArrayList<String> shown = current.getShownShapes();
        ArrayList<String> hidden = current.getHiddenShapes();
        if (shown != null && !shown.isEmpty()) {
            for (String shapeName : shown) {
                Shape toShow = game.getShape(shapeName);
                if (toShow != null) toShow.setHidden(false);
            }
        }
        if (hidden != null && !hidden.isEmpty()) {
            for (String shapeName : hidden) {
                Shape toHide = game.getShape(shapeName);
                if (toHide != null) toHide.setHidden(true);
            }
        }
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

    private void giveToast(String msg) {
        Toast toast = Toast.makeText(
                getContext(),
                msg,
                Toast.LENGTH_LONG);
        toast.show();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (game != null) {
            if (justentered) {
                Page curr = game.getCurrentPage();
                if (curr == null) {
                    //System.out.println("PAGE WAS NULL");
                    game.setCurrentPage(game.getStarter()); // Try going back to starter
                    if (game.getCurrentPage() == null) { // Starter is null - nowhere to go from here
                        giveToast("There was an error with this page. Make sure it has a starter page assigned!");
                        ((Activity) getContext()).finish(); // Finish activity
                        return;
                    }
                }
                curr.onEnter();
                justentered = false;
            }
            game.getCurrentPage().draw(canvas);
            //Paint paint = new Paint();
            //paint.setColor(Color.BLACK);
            //paint.setStrokeWidth(10f);
            //canvas.drawLine(0, canvas.getHeight() * Page.percentMainPage, canvas.getWidth(), canvas.getHeight() * Page.percentMainPage, paint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        setSize(w,h);
    }
}
