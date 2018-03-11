/**
 * Created by uzair on 3/10/18.
 */


package edu.stanford.cs108.bunnyworldplayer;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import java.io.*;
import java.util.*;

public class ShapeEditor extends AppCompatActivity {

    ArrayList<String> allSounds;
    ArrayList<String> pageNames;
    ArrayList<String> allShapes;
    Game currGame;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shape_editor);

        currGame = EditorActivity.newGame;
        if (currGame == null) {
            System.out.println("WARNING: newGame object is NULL");
            return;
        }

        HashMap<String, Page> pagesInGame = currGame.getPages();

        // All pages: goto
        pageNames = new ArrayList<>();
        pageNames.addAll(pagesInGame.keySet());

        // All shapes: show/hide
        allShapes = new ArrayList<>();

        for (Page curr : pagesInGame.values()) {
            HashMap<String, Shape> shapesOnPage = curr.getShapes();
            allShapes.addAll(shapesOnPage.keySet());
        }

        // All sounds: play
        allSounds = new ArrayList<>();
        allSounds.addAll(currGame.getMusicResources().keySet());

        

    }

}
