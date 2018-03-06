package edu.stanford.cs108.bunnyworldplayer;

import java.util.ArrayList;

/**
 * Created by Jerry Chen on 3/5/2018.
 */

public class Game {
    private String name;
    private boolean valid = false; //might need to be appropriately updated upon adding anything
    private ArrayList<Page> pages;
    private Page posessions;
    private ArrayList<String> resources;

    public Game(String name) {
        this.name = name;
        pages = new ArrayList<Page>();
        posessions = new Page("posessions", 100, 100); //random values
    }

    private void initResources() {
        resources = new ArrayList<String>();
        //todo add all resources
    }

    public boolean addPage(Page p) {
        //error checking
        pages.add(p);
        return true;
    }

    //todo parsing collection
}
