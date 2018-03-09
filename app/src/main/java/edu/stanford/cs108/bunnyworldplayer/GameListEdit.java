package edu.stanford.cs108.bunnyworldplayer;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by vsinghi on 3/8/18.
 */

public class GameListEdit extends AppCompatActivity {
    private ListView list1;
    private DatabaseInstance recent;
    private SQLiteDatabase currentDatabase;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list_edit);
        list1 = (ListView) findViewById(R.id.game_list_edit);

        // get game and database from DatabaseInstance
        // recent = most recent instance of the whole application

        currentDatabase = recent.getCurrentDatabase();

        ArrayList<String> gameString = recent.getAllGamesString(recent);

        String [] gameString2 = new String[gameString.size()];
        gameString2 = gameString.toArray(gameString2);



        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.game_list_edit, gameString2);
        list1.setAdapter(adapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text = (TextView) view.findViewById(R.id.rowList);
                String gameName = text.getText().toString();

                // get information about the game from the gameName
                // set the game up and then call intent for launching editor class

                Intent intent = new Intent (getApplicationContext(), EditorActivity.class);
                startActivity(intent);
            }
        });

    }


}
