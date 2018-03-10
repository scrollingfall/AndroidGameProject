package edu.stanford.cs108.bunnyworldplayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jerry Chen on 3/5/2018.
 */


public class Page {
    private String name;
    private HashMap<String,Shape> shapes;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean starter;
    private static HashMap<String, Shape> resources = new HashMap<String, Shape>();
    public Page(String name, float width, float height) {
        this.name = name;
        this.shapes = new HashMap<String, Shape>();
        this.width = width;
        this.height = height; //how to pass these on?
        this.x = 0;
        this.y = 0;
        this.starter = false;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public void setStarter(boolean starter, float x, float y) {
        this.starter = starter;
        this.x = x;
        this.y = y;
    }

    public HashMap<String, Shape> getShapes() {
        return shapes;
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

}
