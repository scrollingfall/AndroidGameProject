package edu.stanford.cs108.bunnyworldplayer;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by vsinghi on 3/9/18.
 */

public class ParserScript {

    private HashMap<String, ArrayList<String>> MapOfScripts;
    private String scriptToParse;

    //Constructor
    public ParserScript(String scriptToParse){
        this.scriptToParse = scriptToParse;
        MapOfScripts = new HashMap<String, ArrayList<String>>();

        //parse the strings




    }

    public HashMap<String, ArrayList<String>> getMap(){
        return MapOfScripts;
    }
}

