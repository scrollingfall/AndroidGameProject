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
    private String starter;
    private String currentPage;
    private Page posessions;
    private Activity activity;
    private HashMap<String, BitmapDrawable> imgResources;
    private HashMap<String, Integer> musicResources;
    private boolean editorMode;

    public Game(String name, Activity activity, String starter) {
        this.name = name;
        this.starter = starter;
        this.currentPage = starter;
        this.activity = activity;
        pages = new HashMap<String, Page>();
        posessions = new Page("posessions", 100, 100); //random values
        this.editorMode = false;
        initResources();
    }

    public void setEditorMode(boolean editorMode) {
        this.editorMode = editorMode;
        for (Page p : pages.values())
            p.setEditorMode(editorMode);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public HashMap<String, Page> getPages() {
        return pages;
    }

    public void setPages(HashMap<String, Page> pages) {
        this.pages = pages;
    }

    public String getStarter() {
        return starter;
    }

    public void setStarter(String starter) {
        this.starter = starter;
    }

    public Page getCurrentPage() {
        return pages.get(currentPage);
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    public HashMap<String, BitmapDrawable> getImgResources() {
        return imgResources;
    }

    public void setImgResources(HashMap<String, BitmapDrawable> imgResources) {
        this.imgResources = imgResources;
    }

    public HashMap<String, Integer> getMusicResources() {
        return musicResources;
    }

    public void setMusicResources(HashMap<String, Integer> musicResources) {
        this.musicResources = musicResources;
    }

    //todo parsing collection
}
