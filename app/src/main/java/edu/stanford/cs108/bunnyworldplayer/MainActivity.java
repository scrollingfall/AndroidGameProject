package edu.stanford.cs108.bunnyworldplayer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.io.File;

public class MainActivity extends AppCompatActivity {


    private void openFile(File file){
        //read lines

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);
    }

    public void createGame(View view){
        Intent intent = new Intent(this, Editor.class);
        startActivity(intent);
    }

    public void editGame(View view){

        Intent intent = new Intent(this, Editor.class);
        startActivity(intent);
    }

    public void playGame(View view){


        Intent intent = new Intent(this, Game.class);
        startActivity(intent);
    }
}
