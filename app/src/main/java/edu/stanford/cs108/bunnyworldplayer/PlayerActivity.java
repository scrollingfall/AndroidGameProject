package edu.stanford.cs108.bunnyworldplayer;

import android.app.Activity;
import android.os.Bundle;

public class PlayerActivity extends Activity {



    private PlayerView player;
    Game currentgame;
    DatabaseInstance databaseInstance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        player = (PlayerView)findViewById(R.id.playerView);
        databaseInstance = DatabaseInstance.getDBinstance(getApplicationContext());
        String currgamename = databaseInstance.getCurrentGameName();
        currentgame = databaseInstance.getGame(currgamename);
        


        player.setSize(player.getWidth(), player.getHeight());
    }
}
