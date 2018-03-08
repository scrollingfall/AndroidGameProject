package edu.stanford.cs108.bunnyworldplayer;

import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by Singhi on 3/7/18.
 */

public class DatabaseInstance {



    private SQLiteDatabase database;

    public SQLiteDatabase getCurrentDatabase(){ return database; }

//    public ArrayList<Game> getAllGames(){
//
//
//    }

//    public ArrayList<String> getAllGamesString(DatabaseInstance game){
//        ArrayList<Game> games = game.getAllGames();
//        ArrayList<String> gameString = new ArrayList<>();
//        for(Game world : games)  gameString.add(world.toString());
//        return gameString;
//    }

}
