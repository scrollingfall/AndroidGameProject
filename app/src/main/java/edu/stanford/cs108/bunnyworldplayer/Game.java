package edu.stanford.cs108.bunnyworldplayer;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jerry Chen on 3/5/2018.
 */

public class Game {
    private String name;
    private boolean valid = false; //might need to be appropriately updated upon adding anything
    private HashMap<String,Page> pages;
    private Page posessions;
    private Activity activity;
    private HashMap<String, BitmapDrawable> imgResources;
    private HashMap<String, Integer> musicResources;

    public Game(String name, Activity activity) {
        this.name = name;
        this.activity = activity;
        pages = new HashMap<String, Page>();
        posessions = new Page("posessions", 100, 100); //random values
    }

    private void initResources() {
        imgResources = new HashMap<String, BitmapDrawable>();
        imgResources.put("carrot", (BitmapDrawable) activity.getResources().getDrawable(R.drawable.carrot));
        imgResources.put("carrot2", (BitmapDrawable) activity.getResources().getDrawable(R.drawable.carrot2));
        imgResources.put("death", (BitmapDrawable) activity.getResources().getDrawable(R.drawable.death));
        imgResources.put("duck", (BitmapDrawable) activity.getResources().getDrawable(R.drawable.duck));
        imgResources.put("fire", (BitmapDrawable) activity.getResources().getDrawable(R.drawable.fire));
        imgResources.put("mystic", (BitmapDrawable) activity.getResources().getDrawable(R.drawable.mystic));
        musicResources = new HashMap<String, Integer>();
        musicResources.put("carrotcarrotcarrot", R.raw.carrotcarrotcarrot);
        musicResources.put("evillaugh", R.raw.evillaugh);
        musicResources.put("fire", R.raw.fire);
        musicResources.put("hooray", R.raw.hooray);
        musicResources.put("munch", R.raw.munch);
        musicResources.put("munching", R.raw.munching);
        musicResources.put("woof", R.raw.woof);
    }

    public boolean addPage(String name, Page p) {
        //error checking
        pages.put(name, p);
        return true;
    }

    //todo parsing collection
}
