package edu.stanford.cs108.bunnyworldplayer;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.BitmapDrawable;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Jerry Chen on 3/5/2018.
 */

public class Game {
    private String name;
    private boolean valid = false; //might need to be appropriately updated upon adding anything
    private HashMap<String,Page> pages = new HashMap<String, Page>();
    private ArrayList<Page> pageList = new ArrayList<>();
    private String starter;
    private String currentPage;
    private HashMap<String, Shape> resources = new HashMap<String, Shape>();
    private Context context;
    private HashMap<String, BitmapDrawable> imgResources;
    private HashMap<String, Integer> musicResources;
    private boolean editorMode;
    private HashMap<String, String> idToName = new HashMap<String, String>();
    private HashMap<String, Shape> allShapes = new HashMap<String, Shape>();


    public Game(String name, Context context) {
        this.name = name;
        this.editorMode = false;
        this.context = context;
        initResources();
    }

    public String pageIDtoName (String pageID) {
        if (idToName.containsKey(pageID)) return idToName.get(pageID);
        return pageID;
    }

    public void linkIDtoName (String pageID, String pageName) {
        idToName.put(pageID, pageName);
    }

    public Game(String name, Page firstPage, Context context) {
        this.name = name;
        this.starter = firstPage.getName();
        this.currentPage = firstPage.getName();
        this.context = context;
//        pages = new HashMap<String, Page>();
//        pageList = new ArrayList<>();
        pages.put(firstPage.getName(), firstPage);
        pageList.add(firstPage);
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
        imgResources.put("carrot", (BitmapDrawable) context.getResources().getDrawable(R.drawable.carrot));
        imgResources.put("carrot2", (BitmapDrawable) context.getResources().getDrawable(R.drawable.carrot2));
        imgResources.put("death", (BitmapDrawable) context.getResources().getDrawable(R.drawable.death));
        imgResources.put("duck", (BitmapDrawable) context.getResources().getDrawable(R.drawable.duck));
        imgResources.put("fire", (BitmapDrawable) context.getResources().getDrawable(R.drawable.fire));
        imgResources.put("mystic", (BitmapDrawable) context.getResources().getDrawable(R.drawable.mystic));
        musicResources = new HashMap<String, Integer>();
        musicResources.put("carrotcarrotcarrot", R.raw.carrotcarrotcarrot);
        musicResources.put("evillaugh", R.raw.evillaugh);
        musicResources.put("fire", R.raw.fire);
        musicResources.put("hooray", R.raw.hooray);
        musicResources.put("munch", R.raw.munch);
        musicResources.put("munching", R.raw.munching);
        musicResources.put("woof", R.raw.woof);
    }

    public void addPage(String name, Page p) {

        pages.put(name, p);
        pageList.add(p);
    }

    public void setSize(float x, float y) {
        for (Page p : pageList) {
            p.setWidth(x);
            p.setHeight(y);
        }
    }

    public void removePage(String name, Page p) {
        pages.remove(name);
        pageList.remove(p);
    }

    public Page getPage(String name) {
        return pages.get(name);
    }

    public HashMap<String, Page> getPages() {
        return pages;
    }
    public ArrayList<Page> getPageList() {
        return pageList;
    }

    public void setPages(HashMap<String, Page> pages) {
        this.pages = pages;

        this.pageList.clear();
        for (Page page : pages.values()) {
            this.pageList.add(page);
        }
    }

    public void setAllShapes() {
        for (Page currPage : getPageList()) {
            if (currPage == null) continue;
            if (allShapes == null) allShapes = new HashMap<String, Shape>();
            for (Shape currShape : currPage.getShapeList()) {
                if (currShape == null) continue;
                allShapes.put(currShape.getName(), currShape);
            }
        }
    }

    public Shape getShape(String name) {
        if (allShapes != null && allShapes.containsKey(name)) return allShapes.get(name);
        return null;
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

    public String getStarter() {
        return starter;
    }

    public void setStarter(String starter) {
        this.starter = starter;
    }

    public Page getCurrentPage() {
        if (pages.containsKey(currentPage)) {
            //System.out.println("GETTING PAGE WITH GIVEN KEY, WHICH IS: " + currentPage);
            return(pages.get(currentPage));
        } else {
            String name = pageIDtoName(currentPage);
            if (pages.containsKey(name)) {
                //System.out.println("GETTING PAGE WITH NAME, WHICH IS: " + name);
            return (pages.get(name));
            } else {
                //System.out.println("GETTING STARTER, WHICH IS: " + starter);
                return pages.get(starter);
            }
        }
    }

    public void setCurrentPage(String currentPage) {
        this.currentPage = currentPage;
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

    public HashMap<String, Shape> getResources() {
        return resources;
    }

    //todo parsing collection
}
