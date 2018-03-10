package edu.stanford.cs108.bunnyworldplayer;

import android.content.Context;
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
    private String nameShape;
    private String owner;
    private String text;
    private String imageString;
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
    private Context context;


    // Constructor
    public Shape (String name, float x, float y, float width, float height) {
        this.nameShape = nameShape;
        this.owner = "";
        this.text = "";
        this.imageString = "";
        this.hidden = true;
        this.moveable = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.inBackpack = false;
    }
    public Shape (String nameShape, String owner, String text, String imageString, boolean hidden, boolean moveable,
                  float x, float y, float width, float height, boolean inBackpack, Context context) {
        this.nameShape = nameShape;
        this.owner = owner;
        this.text = text;
        this.imageString = imageString;
        this.hidden = hidden;
        this.moveable = moveable;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.inBackpack = inBackpack;
        this.context = context;
    }

    public String getShapeName() {
        return nameShape;
    }

    public void setShapeName(String nameShape) {
        this.nameShape = nameShape;
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
        return imageString;
    }

    public void setImage(String imageString) {
        this.imageString = imageString;
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
        if (isHidden()) return;
        else{
            this.canvas = canvas;
            if (imageString.isEmpty() && getText().isEmpty()){
                Paint grayRect = new Paint();
                grayRect.setColor(Color.LTGRAY);
                grayRect.setStyle(Paint.Style.FILL);
                RectF greyRectangle = new RectF(left, top, right, bottom);
                canvas.drawRect(greyRectangle, grayRect);
            }

            else if(!imageString.isEmpty()){
                Resources res = context.getResources();
                int resID = res.getIdentifier(imageString, "drawable", context.getPackageName());
                BitmapDrawable imageDrawable = (BitmapDrawable) context.getResources().getDrawable(resID, context.getTheme());
                image = imageDrawable.getBitmap();

                //rescale according to the rect
                int width = image.getWidth();
                int  height = image.getHeight();
                float scaleWidth = this.width() / width;
                float scaleHeight = this.height() / height;
                Matrix matrix = new Matrix();
                matrix.postScale(scaleWidth, scaleHeight);
                image = image.createBitmap(image, 0, 0, width, height, matrix, false);
                this.bottom = this.top + image.getHeight();
                this.right = this.left + image.getWidth();
                page.drawBitmap(image, left, top, null);

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
}
