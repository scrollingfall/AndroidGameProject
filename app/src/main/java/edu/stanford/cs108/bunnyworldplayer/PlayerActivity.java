package edu.stanford.cs108.bunnyworldplayer;

import android.app.Activity;
import android.os.Bundle;

public class PlayerActivity extends Activity {

    private PlayerView player;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        player = (PlayerView)findViewById(R.id.playerView);
        // todo
        //player.setGame(<game passed in from intent>) important this goes before the next line
        player.setSize(player.getWidth(), player.getHeight());
    }
}
