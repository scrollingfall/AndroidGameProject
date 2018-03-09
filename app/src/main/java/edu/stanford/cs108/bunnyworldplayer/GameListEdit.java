package edu.stanford.cs108.bunnyworldplayer;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleCursorAdapter;

import java.util.ArrayList;


/**
 * Created by vsinghi on 3/7/18.
 */

public class GameListEdit extends AppCompatActivity{
    private ListView list;
    private DatabaseInstance recent;
    private SQLiteDatabase currentDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list_edit);
        list = (ListView) findViewById(R.id.game_list_edit);

        // get game and database from DatabaseInstance
        // recent = most recent instance of the whole application

        currentDatabase = recent.getCurrentDatabase();

        //ArrayList<String> gameString = recent.getAllGamesString(recent);
//        ListAdapter adapter = new SimpleCursorAdapter(this, gameString);
//        list.setAdapter(adapter);
    }

}
