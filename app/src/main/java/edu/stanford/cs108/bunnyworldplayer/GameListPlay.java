package edu.stanford.cs108.bunnyworldplayer;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by vsinghi on 3/7/18.
 */

public class GameListPlay extends AppCompatActivity{
    private ListView list1;
    private DatabaseInstance recent;
    private SQLiteDatabase currentDatabase;
    private DatabaseInstance databaseinstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list_edit);
        list1 = (ListView) findViewById(R.id.game_list_edit);

        databaseinstance = (DatabaseInstance) DatabaseInstance.getDBinstance(getApplicationContext());
        currentDatabase = databaseinstance.getCurrentDatabase();

        ArrayList<String> gameString = databaseinstance.getAllGamesString();


        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.game_list_play, R.id.rowList, gameString);
        list1.setAdapter(adapter);
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text = (TextView) view.findViewById(R.id.rowList);
                String gameName = text.getText().toString();

                // reset inventory
                Intent intent = new Intent (getApplicationContext(), Game.class);
                startActivity(intent);
            }
        });

    }

}
