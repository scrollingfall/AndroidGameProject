package edu.stanford.cs108.bunnyworldplayer;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
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


/**
 * Created by vsinghi on 3/7/18.
 */

public class GameListEdit extends AppCompatActivity{
    private ListView list;
    private DatabaseInstance recent;
    private SQLiteDatabase currentDatabase;
    private DatabaseInstance databaseinstance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list_edit);
        list = (ListView) findViewById(R.id.game_list_edit);
        System.out.println("created list");


        databaseinstance = (DatabaseInstance) DatabaseInstance.getDBinstance(getApplicationContext());
        System.out.println("got db instance");
        currentDatabase = databaseinstance.getCurrentDatabase();
        System.out.println("got db ");

        ArrayList<String> gameString = databaseinstance.getAllGamesString();
        System.out.println("got all game names ");


        for (String gameName : gameString) System.out.println(gameName);


        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.games_row, R.id.rowList, gameString);
        System.out.println("here ");
        list.setAdapter(adapter);
        System.out.println("set the adapter ");

        // todo find the source for this code.
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text = (TextView) view.findViewById(R.id.rowList);
                String gameName = text.getText().toString();

                databaseinstance.setCurrentGameName(gameName);
                databaseinstance.setPageid(databaseinstance.getGame(gameName).getPageList().get(0).getPageId());

                // reset inventory
                Intent intent = new Intent (getApplicationContext(), EditorActivity.class);
                startActivity(intent);
            }
        });
    }

//    public void editGame(View view){
//        TextView text = (TextView) view.findViewById(R.id.rowList);
//        String gameName = text.getText().toString();
//
//        databaseinstance.setCurrentGameName(gameName);
//        databaseinstance.setPageid(databaseinstance.getGame(gameName).getPageList().get(0).getPageId());
//        Intent intent = new Intent (getApplicationContext(), EditorActivity.class);
//        startActivity(intent);
//
//    }


}
