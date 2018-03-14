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
import android.media.MediaPlayer;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by Jerry Chen on 3/5/2018.
 */

public class Shape extends RectF {
    private String name;
    private String owner;
    private String text;
    private String image;
    private boolean selected;
    private ArrayList<String> scripts;
    private boolean hidden;
    private boolean editorMode;
    private int colorRectangle = Color.LTGRAY;
    private boolean moveable;
    private float x;
    private float y;
    private float width;
    private float height;
    private int fontSize;
    private boolean inBackpack;
    private String onClick;
    private String onEnter;
    private HashMap<String, String> onDrop;
    private Canvas canvas;
    private Bitmap imagePic;
    private Context context;
    private HashMap<String, ArrayList<String>> MapOfScripts = new HashMap<String, ArrayList<String>>();
    private String transitionPage = "";
    public ArrayList<String> actionShowShapes = new ArrayList<String>();
    public ArrayList<String> actionHideShapes = new ArrayList<String>();
    public static int shapeID;
    private float textWidth;
    private float textHeight;

    // Constructor
    public Shape (Context context, String name, String owner, float x, float y, float width, float height) {
        this.shapeID = getPreviousShapeId() + 1;
        setPreviousShapeId(shapeID);
        this.name = name;
        this.owner = owner;
        this.text = "";
        this.image = "";
        this.selected = true;
        this.hidden = false;
        this.moveable = false;
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.inBackpack = false;
        this.onClick = "";
        this.onEnter = "";
        this.onDrop = new HashMap<String, String>();
        this.editorMode = true;
        this.context = context;
        this.fontSize = 20;
    }
    public int getPreviousShapeId(){ return shapeID;}
    private void setPreviousShapeId(int shapeID) { this.shapeID = shapeID; }
    public int getShapeId() { return shapeID; }
    public void setShapeId(int shapeid) { this.shapeID = shapeid; }

    public void setEditorMode (boolean editable) {
        editorMode = editable;
    }

    public void setScriptList(String scriptString){
        if (scriptString != null) {
            StringTokenizer st = new StringTokenizer(scriptString, ";");
            while (st.hasMoreTokens()) scripts.add(st.nextToken());
        }
    }

    public String getScript(){
        if (scripts == null || scripts.isEmpty()) return "";
        String scriptString = "";
        for (String script : scripts ){
            scriptString += script;
        }
        return scriptString;
    }

//    public Shape (Context context, String name, String owner, String text, String image, boolean hidden, boolean moveable,
//                  float x, float y, float width, float height, boolean inBackpack) {
//        this.shapeID = getPreviousShapeId() + 1;
//        setPreviousShapeId(shapeID);
//        this.name = name;
//        this.owner = owner;
//        this.text = text;
//        this.image = image;
//        this.hidden = hidden;
//        this.moveable = moveable;
//        this.x = x;
//        this.y = y;
//        this.width = width;
//        this.height = height;
//        this.inBackpack = inBackpack;
//        this.onClick = "";
//        this.onEnter = "";
//        this.onDrop = new HashMap<String, String>();
//        this.editorMode = false;
//        this.context = context;
//        this.fontSize = 20;
//    }

    public int getFontSize(){ return (int) fontSize;}

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
        if (text != null)
            this.text = text;
        else this.text = "";
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        if (image != null)
            this.image = image;
        else this.image = "";
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean turnOn) {
        if (turnOn) {
            selected = true;
        } else {
            selected = false;
        }
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

    public void setScripts(ArrayList<String> scripts) { this.scripts = scripts; }

    public void move(float x, float y) {
        if (moveable || editorMode) {
            this.x = x;
            this.y = y;
        }
    }

    public boolean isEditable() {
        return this.editorMode;
    }

    public float getY() {
        return y;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void setFontSize(int size) {
        this.fontSize = size;
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

    public void setHeight(float height) { this.height = height; }

    public void setWidth(float width) { this.width = width; }

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

    //todo: if(editorMode && isHidden()) then draw things at 50% opacitiy
    public void draw(Canvas canvas) {

        if (!editorMode && isHidden()) return;
        //todo (editor mode and is hidden) draw things in 50% opacity
        else {
            this.canvas = canvas;

            if (x < 0) x = 0;
            if (y < 0) y = 0;

            if ((getText().isEmpty() && image.isEmpty()) || (!image.isEmpty())) {

                Paint grayPaintFill = new Paint();
                grayPaintFill.setColor(Color.LTGRAY);
                grayPaintFill.setStyle(Paint.Style.FILL);
                RectF greyRectangle = new RectF(x, y, x+getWidth(), y+getHeight());

                if (editorMode && hidden) {
                    grayPaintFill.setAlpha(90);
                }

                if (selected) {
                    Paint blackPaintBorder = new Paint();
                    blackPaintBorder.setStrokeWidth(15.0f);
                    blackPaintBorder.setColor(editorMode ? Color.BLACK : Color.GREEN);
                    blackPaintBorder.setStyle(Paint.Style.STROKE);
                    canvas.drawRect(greyRectangle, blackPaintBorder);
                }
                canvas.drawRect(greyRectangle, grayPaintFill);

            }

            if (!image.isEmpty()) {
                Resources resource = context.getResources();
                int resourceIdentifier = resource.getIdentifier(image, "drawable", context.getPackageName());
                BitmapDrawable bitmapImageDrawable = (BitmapDrawable) context.getResources().getDrawable(resourceIdentifier);
                imagePic = bitmapImageDrawable.getBitmap();

                Matrix m1 = new Matrix();
                m1.postScale(this.getWidth() / (float) imagePic.getWidth(), (float) this.getHeight() / (float) imagePic.getHeight());

                int picWidth = imagePic.getWidth();
                int picHeight = imagePic.getHeight();

                imagePic = Bitmap.createBitmap(imagePic, 0, 0, picWidth, picHeight, m1, false);
                this.bottom = this.top + imagePic.getHeight();
                this.right = this.left + imagePic.getWidth();

                if (editorMode && hidden) {
                    Paint paint = new Paint();
                    paint.setAlpha(60);
                    canvas.drawBitmap(imagePic, getX(), getY(), paint);
                } else {
                    canvas.drawBitmap(imagePic, getX(), getY(), null);
                }

            }
            else if (!getText().isEmpty()){

                Paint whitePaint = new Paint();
                whitePaint.setColor(Color.WHITE);
                whitePaint.setStyle(Paint.Style.FILL);

                Paint textStyle = new Paint();
                textStyle.setColor(Color.BLACK);
                textStyle.setStyle(Paint.Style.FILL);
                textStyle.setTextSize(fontSize);

                if (selected) {
                    Paint blackPaintBorder = new Paint();
                    blackPaintBorder.setStrokeWidth(5.0f);
                    blackPaintBorder.setColor(editorMode ? Color.BLACK : Color.GREEN);
                    canvas.drawRect(x - 10f, y - textHeight - 10f, x + textWidth + 10f, y + 10f, blackPaintBorder);
                }

                canvas.drawRect(x, y - textHeight, x + textWidth, y, whitePaint);

                canvas.drawText(getText(), getX(), getY(), textStyle);

            }
        }
    }

    public void setScriptMap(){
        for (String script: scripts) {
            ArrayList<String> scriptWords = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(script, " ");
            while (st.hasMoreTokens()) scriptWords.add(st.nextToken());
            String triggerWords = scriptWords.get(0) + " " + scriptWords.get(1);
            scriptWords.remove(scriptWords.get(0));
            scriptWords.remove(scriptWords.get(0));
            if (triggerWords.equals("on drop")){
                triggerWords += " " + scriptWords.get(0);
                scriptWords.remove(scriptWords.get(0));
            }



            ArrayList<String> actions = new ArrayList<>();
            for(int i = 0; i< scriptWords.size(); i++) {
                String actionWord = scriptWords.get(i);
                if (actionWord.equals("hide") || actionWord.equals("show") || actionWord.equals("goto") || actionWord.equals("play") || actionWord.equals("transform")) {
                    actions.add(actionWord + " " + scriptWords.get(i + 1));
                }
            }
            MapOfScripts.put(triggerWords, actions);
        }
    }


    public boolean performScriptAction(String triggerWords){
        if (isHidden() || !MapOfScripts.containsKey(triggerWords)) return false;
        if (!triggerWords.equals("on click") || !triggerWords.equals("on enter") || !(triggerWords.contains("on drop"))) return false;

        ArrayList<String> actions = MapOfScripts.get(triggerWords);
        for (String action: actions) {
            // execute action
            String[] words = action.split(" ");
            if (words[0].equals("goto")) {
                transitionPage = words[1];
            } else if (words[0].equals("hide")) actionHideShapes.add(words[1]);
            else if (words[0].equals("show")) actionShowShapes.add(words[1]);
            else if (words[0].equals("play")) {
                MediaPlayer soundPlayer = MediaPlayer.create(context, context.getResources().getIdentifier(words[1], "raw", context.getPackageName()));
                soundPlayer.start();

                // Citation: https://stackoverflow.com/questions/24326269/setoncompletionlistener-mediaplayer-oncompletionlistener-in-the-type-mediaplay
                soundPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mediaPlayer) {
                        mediaPlayer.release();
                    }
                });


            }
        }
        return true;
    }

    public void setTextBounds(float textWidth, float textHeight) {

        this.textWidth = textWidth;
        this.textHeight = textHeight;

    }


    public boolean isTouched (float xq, float yq) {
        if (image.isEmpty() && !text.isEmpty()) {
            return !hidden && xq >= x && xq <= x + textWidth && yq >= (y - textHeight) && yq <= y;
        }
        return !hidden && xq >= x && xq <= x + width && yq >= y && yq <= y + height;
    }

    public String getTransition() {
        if (!transitionPage.isEmpty())
        {
            String result = transitionPage;
            transitionPage = "";
            return result;
        }
        return "";
    }
}