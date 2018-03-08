package edu.stanford.cs108.bunnyworldplayer;

import android.os.Bundle;
import android.widget.ListView;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;



/**
 * Created by vsinghi on 3/7/18.
 */

public class GameListEdit extends AppCompatActivity{
    private ListView list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list_edit);
        list = (ListView) findViewById(R.id.game_list_edit);
    }

}
