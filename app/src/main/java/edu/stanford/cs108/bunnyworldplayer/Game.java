package edu.stanford.cs108.bunnyworldplayer;

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
    private ArrayList<String> resources;

    public Game(String name) {
        this.name = name;
        pages = new HashMap<String, Page>();
        posessions = new Page("posessions", 100, 100); //random values
    }

    private void initResources() {
        resources = new ArrayList<String>();
        //todo add all resources
    }

    public boolean addPage(String name, Page p) {
        //error checking
        pages.put(name, p);
        return true;
    }

    //todo parsing collection
}
