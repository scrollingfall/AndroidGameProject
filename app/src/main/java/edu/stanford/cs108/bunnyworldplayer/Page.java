package edu.stanford.cs108.bunnyworldplayer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * Created by Jerry Chen on 3/5/2018.
 */


public class Page {
    private String name;
    private String owner;
    private Game game;
    private HashMap<String,Shape> shapes;
    private ArrayList<Shape> shapeList;
    private Shape selectedShape;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean starter;
    //private static HashMap<String, Shape> resources = new HashMap<String, Shape>();
    private boolean editorMode;
    public static float percentMainPage = 0.80f;
    private String pageID;

    public Page(String name, float width, float height, String gameOwner) {

        this.pageID = UUID.randomUUID().toString();

        this.owner = gameOwner;
        this.name = name;
        this.shapes = new HashMap<String, Shape>();
        this.shapeList = new ArrayList<Shape>();
        this.width = width;
        this.height = height; //how to pass these on?
        this.x = 0;
        this.y = 0;
        //System.out.println("page constructor called with " + name);
        this.starter = false;
        this.editorMode = false;
    }
    public void setGame(Game game) {
        this.game = game;
    }

    public String getPageId() { return pageID; }
    public void setPageId(String pageid) { this.pageID = pageid; }

    public String getOwner() {return owner;}
    public void setOwner(String gameOwner) { this.owner = gameOwner; }


    public void draw (Canvas canvas) {
        for (Shape s: shapeList) {
            s.draw(canvas);
            //System.out.println(name + " is drawing shape " + s.getName());
        }
        for (Shape s: game.getResources().values()) {
            s.draw(canvas);
            //System.out.println(name + " is drawing backpack shape " + s.getName());
        }
        //draw delimiter line
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStrokeWidth(1f);
        canvas.drawLine(0, canvas.getHeight() * percentMainPage,
                canvas.getWidth(), canvas.getHeight() * percentMainPage, paint);
    }
    public void setEditorMode (boolean editable) {
        editorMode = editable;
        for (Shape s : shapeList)
            s.setEditorMode(editable);
        if (game!=null)
        for (Shape s: game.getResources().values()) {
            s.setEditorMode(editable);
        }
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Shape getSelectedShape() {
        return selectedShape;
    }

    public void setSelectedShape(Shape shape) {
        selectedShape = shape;
    }

    public float getWidth() {
        return width;
    }

    public void setWidth(float width) {
        this.width = width;
    }

    public float getHeight() {
        return height;
    }

    public void setHeight(float height) {
        this.height = height;
    }

    public boolean isStarter() {
        return starter;
    }

    //returns an interable list of all shapes on screen, including those in backpack
    public ArrayList<Shape> getShapeList() {
        ArrayList<Shape> newList = new ArrayList<Shape>(shapeList);
        if(game!=null)
            newList.addAll(game.getResources().values());
        return newList;
    }

    public boolean addShape(Shape s) {
        if (shapes.containsKey(s.getName()))
            return false;
        shapes.put(s.getName(), s);
        shapeList.add(s);
        return true;
    }

    public boolean removeShape(String name) {
        if (!shapes.containsKey(name))
            return false;
        Shape rmShape = shapes.remove(name);
        for (int i = 0; i < shapeList.size(); i++) {
            if (shapeList.get(i).getName() == name) {
                shapeList.remove(i);
                break;
            }
        }
        if (shapeList.size() != shapes.size())
            throw new RuntimeException("improper removals");
        return false;
    }

    public void setStarter(boolean starter, float x, float y) {
        //System.out.println("starter set " + (starter?"true ":"false ") + "on page " + name);
        this.starter = starter;
        this.x = x;
        this.y = y;
    }

    public HashMap<String, Shape> getShapes() {
        return shapes;
    }

    public void changeShapeName(String s, Shape shape) {
        if (shapes.containsKey(shape.getName())) shapes.remove(shape.getName());
        shapes.put(s, shape);
    }

    public ArrayList<Shape> getDropTargets(Shape s) {
        ArrayList<Shape> targets = new ArrayList<Shape>();
        for (Shape s2 : shapeList)
            if (s2.containsOnDropFor(s.getName()))
                targets.add(s2);
        return targets;
    }

    public void onEnter() {
        for (Shape s : shapeList) {

            boolean action = s.performScriptAction("on-enter");
            if (!action) continue;

            //System.out.println("ACTION TAKEN BY: " + s.getName());

            ArrayList<String> toShow = s.getShownShapes();
            //System.out.println("TO SHOW IS: " + toShow.toString());

            if (toShow != null && !toShow.isEmpty()) {
                for (String currName : toShow) {
                    //System.out.println("SHOWING " + currName);
                    Shape currShape = game.getShape(currName);
                    if (currShape != null) currShape.setHidden(false);
                }
            }

            ArrayList<String> toHide = s.getHiddenShapes();
            //System.out.println("TO HIDE IS: " + toHide.toString());

            if (toHide != null && !toHide.isEmpty()) {
                for (String currName : toHide) {
                    Shape currShape = game.getShape(currName);
                    if (currShape != null) currShape.setHidden(true);
                }
            }

        }
    }

    public boolean moveToBackpack(String name) {
//        System.out.println("before");
//        System.out.println("shapes: " +shapes);
//        System.out.println("shapelist: "+shapeList);
//        System.out.println("backpack: "+game.getResources());
//        System.out.println("after");
        if (!shapes.containsKey(name))
            return false;
        Shape s = shapes.remove(name);
        //System.out.println(s.getName() +" is moved TO Backpack");
        removeFromList(s.getName());
        //System.out.println("shapes: " +shapes);
        //System.out.println("shapelist: "+shapeList);
        game.getResources().put(name, s);
        //System.out.println("backpack: "+game.getResources());
        s.setInBackpack(true);
        return true;
    }

    public boolean moveFromBackpack(String name) {
//        System.out.println("before");
//        System.out.println("shapes: " +shapes);
//        System.out.println("shapelist: "+shapeList);
//        System.out.println("backpack: "+game.getResources());
//        System.out.println("after");
        if (!game.getResources().containsKey(name))
            return false;
        Shape s = game.getResources().remove(name);
        //System.out.println(s.getName() +" is moved FROM Backpack");
        shapes.put(name, s);
        shapeList.add(s);
        //System.out.println("shapes: " +shapes);
        //System.out.println("shapelist: "+shapeList);
        //System.out.println("backpack: "+game.getResources());
        s.setInBackpack(false);
        return true;
    }

    public String toString() {
        return name;
    }

    private void removeFromList(String name) {
        for (int i = 0; i < shapeList.size(); i ++)
            if (shapeList.get(i).getName().equals(name)) {
                shapeList.remove(i);
                return;
            }
    }
}
