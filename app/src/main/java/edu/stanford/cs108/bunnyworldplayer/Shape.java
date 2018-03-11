package edu.stanford.cs108.bunnyworldplayer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jerry Chen on 3/5/2018.
 */

public class Shape extends RectF {
    private String name;
    private String owner;
    private String text;
    private String image;
    private ArrayList<String> scripts;
    private boolean hidden;
    private boolean editorMode;
    private int colorRectangle = Color.LTGRAY;
    private boolean moveable;
    private float x;
    private float y;
    private float width;
    private float height;
    private boolean inBackpack;
    private String onClick;
    private String onEnter;
    private HashMap<String, String> onDrop;
    private Canvas canvas;
    private Bitmap imagePic;
    private Context context;


    // Constructor
    public Shape (Context context, String name, float x, float y, float width, float height) {
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
        this.onClick = "";
        this.onEnter = "";
        this.onDrop = new HashMap<String, String>();
        this.editorMode = false;
        this.context = context;
    }

    public void setEditorMode (boolean editable){
        editorMode = editable;
    }

    public Shape (Context context, String name, String owner, String text, String image, boolean hidden, boolean moveable,
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
        this.onClick = "";
        this.onEnter = "";
        this.onDrop = new HashMap<String, String>();
        this.editorMode = false;
        this.context = context;
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

    public ArrayList<String> getScripts() {
        return scripts;
    }

    public void addScript(String script) {
        scripts.add(script);
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

    public void move(float x, float y) {
        if (moveable || editorMode) {
            this.x = x;
            this.y = y;
        }
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

    public void resize(float width, float height) {
        this.width = width;
        this.height = height;
    }

    public float getHeight() {
        return height;
    }

    public boolean isInBackpack() {
        return inBackpack;
    }

    public void setInBackpack(boolean inBackpack) {
        this.inBackpack = inBackpack;
    }

    public String getOnClick() {
        return onClick;
    }

    public void setOnClick(String onClick) {
        this.onClick = onClick;
    }

    public String getOnEnter() {
        return onEnter;
    }

    public void setOnEnter(String onEnter) {
        this.onEnter = onEnter;
    }

    public HashMap<String, String> getOnDrop() {
        return onDrop;
    }

    public boolean containsOnDropFor(String name) {
        return onDrop.containsKey(name);
    }

    public void draw(Canvas canvas) {
        if (isHidden()) return;
        else{
            this.canvas = canvas;
            if (image.isEmpty() && getText().isEmpty()){
                Paint grayRect = new Paint();
                grayRect.setColor(Color.LTGRAY);
                grayRect.setStyle(Paint.Style.FILL);
                RectF greyRectangle = new RectF(left, top, right, bottom);
                canvas.drawRect(greyRectangle, grayRect);
            }

            else if(!image.isEmpty()){
                Resources resource = context.getResources();
                int resourceIdentifier = resource.getIdentifier(image, "drawable", context.getPackageName());
                BitmapDrawable bitmapImageDrawable = (BitmapDrawable) context.getResources().getDrawable(resourceIdentifier);
                imagePic = bitmapImageDrawable.getBitmap();

                Matrix m1 = new Matrix();
                m1.postScale((float) this.width() / imagePic.getWidth(), (float) this.height() / imagePic.getHeight());
                imagePic = imagePic.createBitmap(imagePic, 0, 0, imagePic.getWidth() , imagePic.getHeight(), m1, false);
                this.bottom = this.top + imagePic.getHeight();
                this.right = this.left + imagePic.getWidth();
                canvas.drawBitmap(imagePic, left, top, null);

            }
            else if (!getText().isEmpty()){
                Paint textStyle = new Paint();
                textStyle.setColor(Color.BLACK);
                textStyle.setTextSize(20);
                textStyle.setStyle(Paint.Style.FILL);
                canvas.drawText(text, left, top, textStyle);

            }
        }
    }

    public boolean isTouched (float xq, float yq) {
        return xq >= x && xq <= x + width && yq >= y && yq <= y + height;
    }
}