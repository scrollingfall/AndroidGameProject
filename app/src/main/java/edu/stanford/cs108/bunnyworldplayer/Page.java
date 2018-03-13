package edu.stanford.cs108.bunnyworldplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Jerry Chen on 3/5/2018.
 */


public class Page {
    private String name;
    private String owner;
    private HashMap<String,Shape> shapes;
    private ArrayList<Shape> shapeList;
    private Shape selectedShape;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean starter;
    private static HashMap<String, Shape> resources = new HashMap<String, Shape>();
    private boolean editorMode;
    private static int pageID;

    public Page(String name, float width, float height, String gameOwner) {

        this.pageID = getPreviousPageId() + 1;
        setPreviousPageId(pageID);
        this.owner = gameOwner;
        this.name = name;
        this.shapes = new HashMap<String, Shape>();
        this.shapeList = new ArrayList<Shape>();
        this.width = width;
        this.height = height; //how to pass these on?
        this.x = 0;
        this.y = 0;
        this.starter = false;
        this.editorMode = false;
    }

    private int getPreviousPageId(){ return pageID;}
    private void setPreviousPageId(int pageid) { this.pageID = pageid; }
    public int getPageId() { return pageID; }
    public void setPageId(int pageid) { this.pageID = pageid; }

    public String getOwner() {return owner;}
    public void setOwner(String gameOwner) { this.owner = gameOwner; }

    public void setEditorMode (boolean editable) {
        editorMode = editable;
        for (Shape s : shapeList)
            s.setEditorMode(editable);
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

    public ArrayList<Shape> getShapeList() {
        return shapeList;
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

    public boolean removeFromBackpack(String name){
        if (!resources.containsKey(name))
            return false;
        resources.remove(name);
        return true;
    }

    public boolean addToBackpack(String name, Shape shape) {
        if (resources.containsKey(name))
            return false;
        resources.put(name, shape);
        return true;
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
            s.performScriptAction("on enter");
        }
    }

}
