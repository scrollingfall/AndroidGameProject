package edu.stanford.cs108.bunnyworldplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private int gameCounter = 0;
    private static String currGameName;

    private void openFile (File file) {
        //read lines

    }

    /* ---------- Sam's content for testing (should be in EditorActivity.java ------------- */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void createGame(View view){
        gameCounter += 1;
        currGameName = "game" + Integer.toString(gameCounter);

        Intent intent = new Intent(MainActivity.this, EditorActivity.class);
        startActivity(intent);
    }

    public void editGame(View view){
        Intent intent = new Intent(MainActivity.this, GameListEdit.class);
        startActivity(intent);
    }

    public void playGame(View view){
        Intent intent = new Intent(MainActivity.this, GameListPlay.class);
        startActivity(intent);
    }

    public static String getCurrGameName() {
        return currGameName;
    }
}
