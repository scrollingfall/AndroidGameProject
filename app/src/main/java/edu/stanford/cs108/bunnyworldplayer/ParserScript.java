package edu.stanford.cs108.bunnyworldplayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.StringTokenizer;

/**
 * Created by vsinghi on 3/9/18.
 */

public class ParserScript {

    private HashMap<String, ArrayList<String>> MapOfScripts;


    //Constructor
    public ParserScript(ArrayList<String> actionList){
        MapOfScripts = new HashMap<String, ArrayList<String>>();

        //parse the strings
        for (String action: actionList){


        }






    }

    public HashMap<String, ArrayList<String>> getMap(){
        return MapOfScripts;
    }
}

