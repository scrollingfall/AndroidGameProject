package edu.stanford.cs108.bunnyworldplayer;

/**
 * Created by Jerry Chen on 3/5/2018.
 */

public class Shape {
    private String name;
    private String owner;
    private String text;
    private String image;
    private boolean hidden;
    private boolean moveable;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean inBackpack;
    public Shape (String name, float x, float y, float width, float height) {
        this.name = name;
        this.owner = "";
        this.text = "";
        this.image = "";
        this.hidden = true;
        this.moveable = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.inBackpack = false;
    }
    public Shape (String name, String owner, String text, String image, boolean hidden, boolean moveable,
                  float x, float y, float width, float height, boolean inBackpack) {
        this.name = name;
        this.owner = owner;
        this.text = text;
        this.image = image;
        this.hidden = hidden;
        this.moveable = moveable;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.inBackpack = inBackpack;
    }
}
