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
    private DatabaseInstance databaseinstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_list_play);
        list1 = (ListView) findViewById(R.id.game_list_play);
        databaseinstance = (DatabaseInstance) DatabaseInstance.getDBinstance(getApplicationContext());
        ArrayList<String> gameString = databaseinstance.getAllGamesString();

        // source: https://medium.com/mindorks/custom-array-adapters-made-easy-b6c4930560dd
        ListAdapter adapter = new ArrayAdapter<>(this, R.layout.games_row, R.id.rowList, gameString);
        list1.setAdapter(adapter);


        //source: https://stackoverflow.com/questions/8615417/how-can-i-set-onclicklistener-on-arrayadapter
        list1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                TextView text = (TextView) view.findViewById(R.id.rowList);
                databaseinstance.setCurrentGameName(text.getText().toString());
                databaseinstance.setPageid(databaseinstance.getGame(text.getText().toString()).getPageList().get(0).getPageId());
                Intent intent = new Intent (getApplicationContext(), PlayerActivity.class);
                startActivity(intent);
            }
        });
    }

    public void onMainMenu(View view) {
        Intent intent = new Intent(GameListPlay.this, MainActivity.class);
        startActivity(intent);
    }

}
