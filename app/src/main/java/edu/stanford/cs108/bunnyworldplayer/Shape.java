package edu.stanford.cs108.bunnyworldplayer;

import android.graphics.Canvas;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

/**
 * Created by Jerry Chen on 3/5/2018.
 */

public class Shape extends RectF {
    private String name;
    private String owner;
    private String text;
    private String imageText;
    private boolean hidden;
    private int colorRectangle = Color.LTGRAY;
    private boolean moveable;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean inBackpack;
    private Canvas canvas;
    private Bitmap image;


    // Constructor
    public Shape (String name, float x, float y, float width, float height) {
        this.name = name;
        this.owner = "";
        this.text = "";
        this.imageText = "";
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
        this.imageText = image;
        this.hidden = hidden;
        this.moveable = moveable;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.inBackpack = inBackpack;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isHidden() {
        return hidden;
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isMoveable() {
        return moveable;
    }

    public void setMoveable(boolean moveable) {
        this.moveable = moveable;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
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

    public boolean isInBackpack() {
        return inBackpack;
    }

    public void setInBackpack(boolean inBackpack) {
        this.inBackpack = inBackpack;
    }

    public void draw(Canvas canvas) {
        //todo
    }
}
